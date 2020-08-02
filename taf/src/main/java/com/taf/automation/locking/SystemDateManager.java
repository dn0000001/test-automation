package com.taf.automation.locking;

import com.taf.automation.ui.support.BasicClock;
import com.taf.automation.ui.support.DateActions;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;

/**
 * Ensure that multiple threads that needs to move the system clock do not collide
 */
public class SystemDateManager {
    private static final Long NO_RESERVATION = 0L;
    private static final int DEFAULT_RESERVATION_PERIOD = 1;
    private static final int DEFAULT_MAX_RESERVATIONS = 5;
    private static final int MAX_RESERVATIONS = getMaxReservations();
    private static final BasicClock clock = new BasicClock();
    private static final ReentrantLock moveClock = new ReentrantLock();
    private static final ReentrantLock action = new ReentrantLock();
    private static final Condition openReservations = action.newCondition();
    private static final StampedLock stamped = new StampedLock();
    private static final ReentrantReadWriteLock reservation = new ReentrantReadWriteLock();
    private static final Map<Long, ReservationInfo> storedReservations = new HashMap<>();

    private boolean manuallyClosedReservations;
    private Long endReservationTime;
    private int currentReservations = 0;
    private int pollOpenReservations = 1000;
    private int pollSafeToMoveClock = 5000;
    private BiConsumer<Long, Integer> moveClockAction;

    private enum ReservationState {
        /**
         * The initial state that indicates there is an open ticket for moving the clock.<BR>
         * At this point, the test is executing the actions before moving the clock is necessary.
         */
        OPENED,

        /**
         * This state indicates that the test has completed the actions and it is now waiting for the clock to be moved
         */
        WAITING,

        /**
         * This state indicates that a test has multiple move clock actions and it is reserving one for the future
         */
        RESERVED,

        /**
         * This state indicates that the clock has been moved for the ticket.<BR>
         * At this point, the test continues to execution the actions after moving the clock.
         */
        CLOCK_MOVED
    }

    private static class ReservationInfo {
        private Integer plusDays;
        private ReservationState state;

        private Integer getPlusDays() {
            return plusDays;
        }

        private void setPlusDays(Integer plusDays) {
            this.plusDays = plusDays;
        }

        private ReservationState getState() {
            return state;
        }

        private void setState(ReservationState state) {
            this.state = state;
        }

    }

    private SystemDateManager() {
        //
    }

    private static class LazyHolder {
        private static final SystemDateManager INSTANCE = new SystemDateManager();
    }

    public static SystemDateManager getInstance() {
        return SystemDateManager.LazyHolder.INSTANCE;
    }

    private static int getMaxReservations() {
        int reservations = DEFAULT_MAX_RESERVATIONS;
        if (TestProperties.getInstance().getThreadCount() != null) {
            reservations = TestProperties.getInstance().getThreadCount();
        }

        return reservations;
    }

    private Long getEndReservationTime() {
        action.lock();
        try {
            if (endReservationTime == null) {
                endReservationTime = clock.laterBy(TimeUnit.MINUTES.toMillis(DEFAULT_RESERVATION_PERIOD));
            }

            return endReservationTime;
        } finally {
            action.unlock();
        }
    }

    /**
     * Reset the End Reservation Time to open the reservation window
     */
    private void resetEndReservationTime() {
        endReservationTime = null;
    }

    /**
     * Set the poll interval for open reservations
     *
     * @param pollOpenReservations - Poll interval for open reservations in milliseconds
     * @return SystemDateManager
     */
    public SystemDateManager withPollOpenReservations(int pollOpenReservations) {
        action.lock();
        this.pollOpenReservations = pollOpenReservations;
        action.unlock();
        return this;
    }

    /**
     * Set the poll interval for safe to move clock
     *
     * @param pollSafeToMoveClock - Poll interval for safe to move clock in milliseconds
     * @return SystemDateManager
     */
    public SystemDateManager withPollSafeToMoveClock(int pollSafeToMoveClock) {
        action.lock();
        this.pollSafeToMoveClock = pollSafeToMoveClock;
        action.unlock();
        return this;
    }

    /**
     * Set the lambda expression that takes a long (ticket) and an integer as the parameters
     *
     * @param moveClockAction - Lambda expression that takes a long (ticket) and an integer that will move the clock
     * @return SystemDateManager
     */
    @SuppressWarnings("squid:S4276")
    public SystemDateManager withMoveClockAction(BiConsumer<Long, Integer> moveClockAction) {
        return withMoveClockAction(moveClockAction, false);
    }

    /**
     * Set the lambda expression that takes a long (ticket) and an integer as the parameters
     *
     * @param moveClockAction - Lambda expression that takes a long (ticket) and an integer that will move the clock
     * @param forceOverride   - true to always set moveClockAction even if previously set
     * @return SystemDateManager
     */
    @SuppressWarnings("squid:S4276")
    public SystemDateManager withMoveClockAction(BiConsumer<Long, Integer> moveClockAction, boolean forceOverride) {
        action.lock();
        if (forceOverride || this.moveClockAction == null) {
            this.moveClockAction = moveClockAction;
        }

        action.unlock();
        return this;
    }

    /**
     * Open a reservation for plus specified days in relation to the start date of the test<BR>
     * <B>Note: </B> If plus days is less than one, then 0 is returned which indicates no reservation ticket
     *
     * @param plusDays - Number of days to move clock forward in relation to the start date
     * @return reservation ticket
     */
    public Long openReservationForDays(int plusDays) {
        if (plusDays < 1) {
            return NO_RESERVATION;
        }

        // Ensure the end reservation time gets set even if at maximum reservations
        getEndReservationTime();

        while (!isOpenReservation()) {
            try {
                openReservations.await();
            } catch (Exception ex) {
                //
            }
        }

        action.lock();
        try {
            currentReservations++;
            Long ticket = stamped.readLock();
            ReservationInfo reservationInfo = new ReservationInfo();
            reservationInfo.setPlusDays(plusDays);
            reservationInfo.setState(ReservationState.OPENED);
            reservation.writeLock().lock();
            try {
                storedReservations.put(ticket, reservationInfo);
            } finally {
                reservation.writeLock().unlock();
            }

            return ticket;
        } finally {
            action.unlock();
        }
    }

    /**
     * Open a reservation for plus specified weeks in relation to the start date of the test<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>If weeks is less than one, then 0 is returned which indicates no reservation ticket</LI>
     * <LI>If any actions are necessary to get the start date, then it is necessary to ensure
     * they do not affect the system or the test</LI>
     * </OL>
     *
     * @param weeks     - Number of weeks to move clock forward in relation to the start date
     * @param startDate - The start date from which to add the weeks
     * @return reservation ticket
     */
    public Long openReservationForWeeks(int weeks, Date startDate) {
        if (weeks < 1) {
            return NO_RESERVATION;
        }

        Date endDate = DateUtils.addWeeks(startDate, weeks);
        Long noOfDaysBetween = DateActions.daysBetween(startDate, endDate);
        return openReservationForDays(noOfDaysBetween.intValue());
    }

    /**
     * Open a reservation for plus specified months in relation to the start date of the test<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>If months is less than one, then 0 is returned which indicates no reservation ticket</LI>
     * <LI>If any actions are necessary to get the start date, then it is necessary to ensure
     * they do not affect the system or the test</LI>
     * </OL>
     *
     * @param months    - Number of months to move clock forward in relation to the start date
     * @param startDate - The start date from which to add the months
     * @return reservation ticket
     */
    public Long openReservationForMonths(int months, Date startDate) {
        if (months < 1) {
            return NO_RESERVATION;
        }

        Date endDate = DateUtils.addMonths(startDate, months);
        Long noOfDaysBetween = DateActions.daysBetween(startDate, endDate);
        return openReservationForDays(noOfDaysBetween.intValue());
    }

    /**
     * Open an additional reservation for plus specified days in relation to the start date of the test<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>If plus days is less than one or existing ticket is not valid or
     * plus days is less than equal to the existing ticket's days,
     * then 0 is returned which indicates no reservation ticket</LI>
     * <LI>This method should only be used if the test requires multiple clock moves
     * as the initial state of the ticket will not block other clock moves</LI>
     * <LI>The additional reservation will not be added until the reservation window closes</LI>
     * </OL>
     *
     * @param existingTicket - Existing Reservation Ticket
     * @param plusDays       - Number of days to move clock forward in relation to the start date
     * @return reservation ticket
     */
    public Long additionalReservationForDays(Long existingTicket, int plusDays) {
        if (plusDays < 1 || !stamped.validate(existingTicket)) {
            return NO_RESERVATION;
        }

        reservation.readLock().lock();
        try {
            if (plusDays <= storedReservations.get(existingTicket).getPlusDays()) {
                return NO_RESERVATION;
            }
        } finally {
            reservation.readLock().unlock();
        }

        waitForReservationWindowClosed();
        action.lock();
        try {
            currentReservations++;
            Long ticket = stamped.readLock();
            ReservationInfo reservationInfo = new ReservationInfo();
            reservationInfo.setPlusDays(plusDays);
            reservationInfo.setState(ReservationState.RESERVED);
            reservation.writeLock().lock();
            try {
                storedReservations.put(ticket, reservationInfo);
            } finally {
                reservation.writeLock().unlock();
            }

            return ticket;
        } finally {
            action.unlock();
        }
    }

    /**
     * Open an additional reservation for plus specified weeks in relation to the start date of the test<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>If weeks is less than one or existing ticket is not valid or
     * plus days is less than equal to the existing ticket's days,
     * then 0 is returned which indicates no reservation ticket</LI>
     * <LI>If any actions are necessary to get the start date, then it is necessary to ensure
     * they do not affect the system or the test</LI>
     * <LI>This method should only be used if the test requires multiple clock moves
     * as the initial state of the ticket will not block other clock moves</LI>
     * <LI>The additional reservation will not be added until the reservation window closes</LI>
     * </OL>
     *
     * @param existingTicket - Existing Reservation Ticket
     * @param weeks          - Number of weeks to move clock forward in relation to the start date
     * @param startDate      - The start date from which to add the weeks
     * @return reservation ticket
     */
    public Long additionalReservationForWeeks(Long existingTicket, int weeks, Date startDate) {
        Date endDate = DateUtils.addWeeks(startDate, weeks);
        Long noOfDaysBetween = DateActions.daysBetween(startDate, endDate);
        return additionalReservationForDays(existingTicket, noOfDaysBetween.intValue());
    }

    /**
     * Open an additional reservation for plus specified months in relation to the start date of the test<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>If months is less than one or existing ticket is not valid or
     * plus days is less than equal to the existing ticket's days,
     * then 0 is returned which indicates no reservation ticket</LI>
     * <LI>If any actions are necessary to get the start date, then it is necessary to ensure
     * they do not affect the system or the test</LI>
     * <LI>This method should only be used if the test requires multiple clock moves
     * as the initial state of the ticket will not block other clock moves</LI>
     * <LI>The additional reservation will not be added until the reservation window closes</LI>
     * </OL>
     *
     * @param existingTicket - Existing Reservation Ticket
     * @param months         - Number of months to move clock forward in relation to the start date
     * @param startDate      - The start date from which to add the months
     * @return reservation ticket
     */
    public Long additionalReservationForMonths(Long existingTicket, int months, Date startDate) {
        Date endDate = DateUtils.addMonths(startDate, months);
        Long noOfDaysBetween = DateActions.daysBetween(startDate, endDate);
        return additionalReservationForDays(existingTicket, noOfDaysBetween.intValue());
    }

    /**
     * @return true if ticket reservations are being accepted else false
     */
    private boolean isOpenReservation() {
        // Handle case in which the current time is before the end reservation time but the open reservation window
        // was closed early as max reservations was reached.
        if (manuallyClosedReservations) {
            return false;
        }

        if (currentReservations >= MAX_RESERVATIONS) {
            manuallyClosedReservations = true;  // Close reservations early as max reservations reached
            return false;
        }

        return clock.isNowBefore(getEndReservationTime());
    }

    /**
     * Wait for the reservation ticket to be served which moves the clock<BR>
     * <B>Note: </B> A valid ticket can be generated by calling the method <B>openReservationForDays</B>
     *
     * @param ticket - Reservation Ticket
     * @return true if the reservation caused the clock to be moved, false this specific reservation did not cause
     * the clock to moved but another one did which fulfilled this reservation at the same time
     */
    public boolean waitForReservation(Long ticket) {
        boolean causedClockMove = false;
        if (ticket == null || ticket.equals(NO_RESERVATION)) {
            return causedClockMove;
        }

        ReservationInfo reservationInfo = updateToWaiting(ticket);
        waitForReservationWindowClosed();
        waitForSafeToMoveClock(ticket);
        waitForLeastAddDays(reservationInfo);

        moveClock.lock();
        try {
            // Before we got the lock a previous moving of the clock may have made this no longer safe
            // Note:  This could happen if multiple tests want to move a different number of days
            waitForSafeToMoveClock(ticket);

            // Before we got the lock a previous moving of the clock may have handled it
            // Note:  This could happen if multiple tests want to move the same number of days
            reservation.readLock().lock();
            if (reservationInfo.getState() == ReservationState.WAITING) {
                reservation.readLock().unlock();
                moveTheClock(ticket, reservationInfo);
                updateReservations(reservationInfo);
                causedClockMove = true;
            } else {
                reservation.readLock().unlock();
            }

            return causedClockMove;
        } finally {
            moveClock.unlock();
        }
    }

    /**
     * Update the ticket to waiting state
     *
     * @param ticket - Reservation Ticket
     * @return ReservationInfo
     */
    private ReservationInfo updateToWaiting(Long ticket) {
        reservation.writeLock().lock();
        try {
            ReservationInfo reservationInfo = storedReservations.get(ticket);
            if (reservationInfo.getState() != ReservationState.CLOCK_MOVED) {
                reservationInfo.setState(ReservationState.WAITING);
            }

            storedReservations.put(ticket, reservationInfo);
            return reservationInfo;
        } finally {
            reservation.writeLock().unlock();
        }
    }

    /**
     * Wait for it to be safe to move the clock for this ticket
     */
    private void waitForSafeToMoveClock(Long ticket) {
        while (!isSafeToMoveClock(ticket)) {
            Utils.sleep(pollSafeToMoveClock);
        }
    }

    /**
     * Wait for the reservation window to be closed
     */
    private void waitForReservationWindowClosed() {
        while (isOpenReservation()) {
            Utils.sleep(pollOpenReservations);
        }
    }

    /**
     * Determine if it is save to move the clock for this ticket
     *
     * @param ticket - Reservation Ticket
     * @return true indicates that it is safe to move the clock else false
     */
    private boolean isSafeToMoveClock(Long ticket) {
        reservation.readLock().lock();
        try {
            if (ticket != null) {
                ReservationInfo reservationInfo = storedReservations.get(ticket);
                if (reservationInfo != null && reservationInfo.getState() == ReservationState.CLOCK_MOVED) {
                    // The ticket to move the clock has already been completed as such the clock will not be moved again
                    return true;
                }
            }

            for (Map.Entry<Long, ReservationInfo> entry : storedReservations.entrySet()) {
                if (isNonSafeState(entry.getValue().getState())) {
                    return false;
                }
            }

            return true;
        } finally {
            reservation.readLock().unlock();
        }
    }

    /**
     * @param state - Reservation State to check against
     * @return true if the Reservation State indicates it is safe to move the clock
     */
    private boolean isNonSafeState(ReservationState state) {
        //
        // CASE 1: We do not want to move the clock if a ticket has the state of OPENED as this indicates a test has not
        // gotten to a point where it has stopped and ready for the clock to be moved.
        //
        // CASE 2: We do not want to move the clock if a ticket has the state of CLOCK_MOVED as this indicates a test
        // has had the clock moved and it is executing the actions after which could be affected by another clock move.
        //
        // CASE 3: All tests have stopped if the ticket is WAITING (or completed in which case
        // the ticket was already removed)
        //
        // CASE 4: There is a future reservation if the state is RESERVED as such it is still safe to move the clock
        //
        return state == ReservationState.OPENED || state == ReservationState.CLOCK_MOVED;
    }

    /**
     * Wait for the reservation to be the least days to add
     *
     * @param reservationInfo - Reservation Information to check against
     */
    private void waitForLeastAddDays(ReservationInfo reservationInfo) {
        while (!isLeastAddDays(reservationInfo)) {
            Utils.sleep(pollSafeToMoveClock);
        }
    }

    /**
     * @param reservationInfo - Reservation Information to check against
     * @return true if the Reservation Information is the least number of days to be added else false
     */
    private boolean isLeastAddDays(ReservationInfo reservationInfo) {
        reservation.readLock().lock();
        try {
            if (reservationInfo.getState() == ReservationState.CLOCK_MOVED) {
                // The ticket to move the clock has already been completed as such the clock will not be moved again
                return true;
            }

            for (Map.Entry<Long, ReservationInfo> entry : storedReservations.entrySet()) {
                ReservationState state = entry.getValue().getState();
                if (state == ReservationState.CLOCK_MOVED) {
                    // Any existing reservation that is clock moved must be less than this reservation
                    return false;
                }

                Integer addDays = entry.getValue().getPlusDays();
                if ((state == ReservationState.WAITING || state == ReservationState.RESERVED)
                        && addDays != null && addDays < reservationInfo.getPlusDays()
                ) {
                    return false;
                }
            }

            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            reservation.readLock().unlock();
        }
    }

    /**
     * Update the existing reservations to account for the processed reservation
     *
     * @param reservationInfo - Reservation that was processed
     */
    private void updateReservations(ReservationInfo reservationInfo) {
        reservation.writeLock().lock();
        try {
            for (Map.Entry<Long, ReservationInfo> entry : storedReservations.entrySet()) {
                if (reservationInfo.getPlusDays().equals(entry.getValue().getPlusDays())) {
                    entry.getValue().setState(ReservationState.CLOCK_MOVED);
                } else if (entry.getValue().getState() == ReservationState.WAITING ||
                        entry.getValue().getState() == ReservationState.RESERVED
                ) {
                    // Note:  We are only moving the clock on the smallest number of days as such this is always positive
                    entry.getValue().setPlusDays(entry.getValue().getPlusDays() - reservationInfo.getPlusDays());
                }
            }
        } finally {
            reservation.writeLock().unlock();
        }
    }

    /**
     * Move the clock for this reservation
     *
     * @param ticket          - Ticket that the clock is being moved for
     * @param reservationInfo - Reservation to be move the clock
     */
    private void moveTheClock(Long ticket, ReservationInfo reservationInfo) {
        action.lock();
        try {
            moveClockAction.accept(ticket, reservationInfo.getPlusDays());
        } finally {
            action.unlock();
        }
    }

    /**
     * Close the reservation ticket
     *
     * @param ticket - Ticket to be closed
     * @return true if ticket removed, else false
     */
    public boolean closeReservation(Long ticket) {
        if (ticket == null || ticket.equals(NO_RESERVATION)) {
            return false;
        }

        action.lock();
        try {
            if (!validate(ticket)) {
                return false;
            }

            currentReservations--;
            stamped.unlock(ticket);
            reservation.writeLock().lock();
            storedReservations.remove(ticket);
            reservation.writeLock().unlock();
            if (storedReservations.isEmpty() || currentReservations == 0) {
                resetEndReservationTime();
                manuallyClosedReservations = false;
                openReservations.signalAll();
            }

            return true;
        } finally {
            action.unlock();
        }
    }

    /**
     * Validate ticket
     *
     * @param ticket - Ticket to be validated
     * @return true the ticket is valid else false
     */
    public boolean validate(Long ticket) {
        try {
            return stamped.validate(ticket) && storedReservations.containsKey(ticket);
        } catch (Exception ex) {
            return false;
        }
    }

}
