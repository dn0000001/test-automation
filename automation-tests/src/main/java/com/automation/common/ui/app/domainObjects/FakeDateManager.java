package com.automation.common.ui.app.domainObjects;

import com.taf.automation.locking.SystemDateManager;
import com.taf.automation.ui.support.TestContext;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

/**
 * Wrapper class for using/testing of the SystemDateManager class
 */
public class FakeDateManager {
    private static final Logger LOG = LoggerFactory.getLogger(FakeDateManager.class);
    private static final String OPEN_RESERVATION = "Open Reservation";
    private static final String ADDITIONAL_RESERVATION = "Additional Reservation";
    private static final String MOVE_CLOCK = "Move Clock";
    private static final String WAIT_FOR_RESERVATION = "Wait For Reservation";
    private static final String CLOSE_RESERVATION = "Close Reservation";
    private static final ReentrantLock action = new ReentrantLock();
    private Date systemDate = new Date();
    private Map<Long, Date> moveClockLog = new HashMap<>();
    private List<String> logOfActions = new ArrayList<>();
    private Map<Long, TestContext> storedContext = new HashMap<>();

    private FakeDateManager() {
        SystemDateManager.getInstance().withMoveClockAction(this::performMoveClock);
    }

    private static class LazyHolder {
        private static final FakeDateManager INSTANCE = new FakeDateManager();
    }

    public static FakeDateManager getInstance() {
        return FakeDateManager.LazyHolder.INSTANCE;
    }

    public FakeDateManager withContext(TestContext context) {
        return withContext(Thread.currentThread().getId(), context);
    }

    public FakeDateManager withContext(Long threadId, TestContext context) {
        this.storedContext.put(threadId, context);
        return this;
    }

    private TestContext getStoredContext() {
        return storedContext.get(Thread.currentThread().getId());
    }

    private void performMoveClock(Long ticket, Integer days) {
        // These are my actions to simulate moving the clock.  (These would be replaced with real actions that move the clock)
        assertThat("Ticket (" + ticket + ") - Invalid Add Days:  " + days, days, greaterThan(0));
        Date start = new Date();
        getStoredContext().getDriver().get("https://www.google.ca");
        systemDate = DateUtils.addDays(systemDate, days);
        Date end = new Date();

        // For testing purposes, this is creating a log.
        action.lock();
        moveClockLog.put(ticket, systemDate);
        action.unlock();
        addLogEntry(MOVE_CLOCK + " - Days (" + days + ")", ticket, start, end);
    }

    public Date getCurrentSystemDate() {
        try {
            action.lock();
            return systemDate;
        } finally {
            action.unlock();
        }
    }

    public Date getSystemDate(Long ticket) {
        try {
            action.lock();
            return moveClockLog.get(ticket);
        } finally {
            action.unlock();
        }
    }

    public Long openReservationForDays(String testId, int plusDays) {
        Date start = new Date();
        Long ticket = SystemDateManager.getInstance().openReservationForDays(plusDays);
        assertThat(testId + " - No Open Ticket", ticket, not(equalTo(0L)));
        Date end = new Date();
        addLogEntry(testId + " - " + OPEN_RESERVATION, ticket, start, end);
        return ticket;
    }

    public Long openReservationForWeeks(String testId, int weeks, Date startDate) {
        Date start = new Date();
        Long ticket = SystemDateManager.getInstance().openReservationForWeeks(weeks, startDate);
        assertThat(testId + " - No Open Ticket (Weeks)", ticket, not(equalTo(0L)));
        Date end = new Date();
        addLogEntry(testId + " - " + OPEN_RESERVATION, ticket, start, end);
        return ticket;
    }

    public Long openReservationForMonths(String testId, int months, Date startDate) {
        Date start = new Date();
        Long ticket = SystemDateManager.getInstance().openReservationForMonths(months, startDate);
        assertThat(testId + " - No Open Ticket (Months)", ticket, not(equalTo(0L)));
        Date end = new Date();
        addLogEntry(testId + " - " + OPEN_RESERVATION, ticket, start, end);
        return ticket;
    }

    public Long additionalReservationForDays(String testId, Long existingTicket, int plusDays) {
        Date start = new Date();
        Long ticket = SystemDateManager.getInstance().additionalReservationForDays(existingTicket, plusDays);
        assertThat(testId + " - No Reserved Ticket", ticket, not(equalTo(0L)));
        Date end = new Date();
        addLogEntry(testId + " - " + ADDITIONAL_RESERVATION, ticket, start, end);
        return ticket;
    }

    public Long additionalReservationForWeeks(String testId, Long existingTicket, int weeks, Date startDate) {
        Date start = new Date();
        Long ticket = SystemDateManager.getInstance().additionalReservationForWeeks(existingTicket, weeks, startDate);
        assertThat(testId + " - No Open Ticket (Weeks)", ticket, not(equalTo(0L)));
        Date end = new Date();
        addLogEntry(testId + " - " + ADDITIONAL_RESERVATION, ticket, start, end);
        return ticket;
    }

    public Long additionalReservationForMonths(String testId, Long existingTicket, int months, Date startDate) {
        Date start = new Date();
        Long ticket = SystemDateManager.getInstance().additionalReservationForMonths(existingTicket, months, startDate);
        assertThat(testId + " - No Open Ticket (Months)", ticket, not(equalTo(0L)));
        Date end = new Date();
        addLogEntry(testId + " - " + ADDITIONAL_RESERVATION, ticket, start, end);
        return ticket;
    }

    public void waitForReservation(String testId, Long ticket) {
        Date start = new Date();
        boolean causedClockMove = SystemDateManager.getInstance().waitForReservation(ticket);
        if (!causedClockMove) {
            action.lock();
            moveClockLog.put(ticket, getCurrentSystemDate());
            action.unlock();
        }

        Date end = new Date();
        addLogEntry(testId + " - " + WAIT_FOR_RESERVATION, ticket, start, end);
    }

    public boolean closeReservation(String testId, Long ticket) {
        Date start = new Date();
        boolean result = SystemDateManager.getInstance().closeReservation(ticket);
        Date end = new Date();
        addLogEntry(testId + " - " + CLOSE_RESERVATION + " (" + result + ")", ticket, start, end);
        return result;
    }

    public boolean validate(Long ticket) {
        return SystemDateManager.getInstance().validate(ticket);
    }

    private void addLogEntry(String log, Long ticket, Date start, Date end) {
        action.lock();
        String entry = "%s - Ticket (%s) - Started at %s, Ended at %s";
        String message = String.format(entry, log, ticket, start, end);
        logOfActions.add(message);
        LOG.info(message);
        action.unlock();
    }

    @Step("Log Entries")
    public void outputLogEntries() {
        action.lock();

        for (String entry : logOfActions) {
            outputEntry(entry);
        }

        action.unlock();
    }

    @Step("{0}")
    private void outputEntry(String entry) {
        //
    }

}
