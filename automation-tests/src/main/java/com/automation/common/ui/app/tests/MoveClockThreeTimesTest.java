package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.FakeDateManager;
import com.taf.automation.ui.support.DateActions;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MoveClockThreeTimesTest extends TestNGBase {
    private Long ticket1;
    private Long ticket2;
    private Long ticket3;
    private String testId;

    @SuppressWarnings("java:S107")
    @Features("Framework")
    @Stories("Validate moving the clock three times")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({
            "test-id",
            "plus-days-1",
            "plus-days-2",
            "plus-days-3",
            "before-clock-move-actions-time",
            "after-clock-move-actions-time-1",
            "after-clock-move-actions-time-2",
            "after-clock-move-actions-time-3",
            "url"
    })
    @Test
    public void testMovingTheClock(
            @Optional("aaa") String id,
            @Optional("0") String plusDays1,
            @Optional("0") String plusDays2,
            @Optional("0") String plusDays3,
            @Optional("1000") String beforeClockMoveActionsTime,
            @Optional("1000") String afterClockMoveActionsTime1,
            @Optional("1000") String afterClockMoveActionsTime2,
            @Optional("1000") String afterClockMoveActionsTime3,
            @Optional("https://the-internet.herokuapp.com/tables") String url
    ) {
        // Indicate that test needs to move the clock
        testId = id;
        ticket1 = FakeDateManager.getInstance()
                .withContext(getContext())
                .openReservationForDays(testId, NumberUtils.toInt(plusDays1, 0));
        ticket2 = FakeDateManager.getInstance()
                .additionalReservationForDays(testId, ticket1, NumberUtils.toInt(plusDays2, 0));
        ticket3 = FakeDateManager.getInstance()
                .additionalReservationForDays(testId, ticket2, NumberUtils.toInt(plusDays3, 0));

        Date startDate = FakeDateManager.getInstance().getCurrentSystemDate();

        // Perform whatever actions until the clock needs to be moved
        performBeforeClockMoveActions(beforeClockMoveActionsTime);

        // Wait for the reservation to be completed which moves the clock
        FakeDateManager.getInstance().waitForReservation(testId, ticket1);

        Date endDate = FakeDateManager.getInstance().getSystemDate(ticket1);
        validateMoveClockDays(startDate, endDate, plusDays1);

        // Perform whatever actions after the clock was moved the 1st time
        performAfterClockMoveActions("1", url, afterClockMoveActionsTime1);

        // Close the first reservation after the validations are complete
        // This is necessary to unblock moving the clock
        boolean closed = FakeDateManager.getInstance().closeReservation(testId, ticket1);
        assertThat("Close Reservation (" + ticket1 + ")", closed);

        // Wait for the reservation to be completed which moves the clock the 2nd time
        FakeDateManager.getInstance().waitForReservation(testId, ticket2);

        endDate = FakeDateManager.getInstance().getSystemDate(ticket2);
        validateMoveClockDays(startDate, endDate, plusDays2);

        // Perform whatever actions after the clock was moved the 2nd time
        performAfterClockMoveActions("2", url, afterClockMoveActionsTime2);

        // Close the second reservation after the validations are complete
        // This is necessary to unblock moving the clock
        closed = FakeDateManager.getInstance().closeReservation(testId, ticket2);
        assertThat("Close Reservation (" + ticket2 + ")", closed);

        // Wait for the reservation to be completed which moves the clock the 3rd time
        FakeDateManager.getInstance().waitForReservation(testId, ticket3);

        endDate = FakeDateManager.getInstance().getSystemDate(ticket3);
        validateMoveClockDays(startDate, endDate, plusDays3);

        // Perform whatever actions after the clock was moved the 3rd time
        performAfterClockMoveActions("3", url, afterClockMoveActionsTime3);
    }

    @Step("Validate Move Clock Days between {0} and {1} is {2}")
    private void validateMoveClockDays(Date startDate, Date endDate, String plusDays) {
        Long noOfDaysBetween = DateActions.daysBetween(startDate, endDate);
        assertThat("Move Clock Days", noOfDaysBetween, equalTo(Long.valueOf(plusDays)));
    }

    @Step("Perform Before Clock Move Actions")
    private void performBeforeClockMoveActions(String beforeClockMoveActionsTime) {
        Utils.sleep(NumberUtils.toInt(beforeClockMoveActionsTime, 1000));
    }

    @Step("Perform After Clock Move Actions #{0}")
    private void performAfterClockMoveActions(String num, String url, String afterClockMoveActionsTime) {
        getContext().getDriver().get(url);
        Utils.sleep(NumberUtils.toInt(afterClockMoveActionsTime, 1000));
    }

    @Test(dependsOnMethods = "testMovingTheClock", alwaysRun = true)
    private void cleanup() {
        //
        // This should be using the annotation AfterTest but I want to always see the output log entries
        // and AfterTest does not display this if it passes
        //
        if (FakeDateManager.getInstance().validate(ticket1)) {
            FakeDateManager.getInstance().closeReservation(testId, ticket1);
        }

        if (FakeDateManager.getInstance().validate(ticket2)) {
            FakeDateManager.getInstance().closeReservation(testId, ticket2);
        }

        FakeDateManager.getInstance().closeReservation(testId, ticket3);
        FakeDateManager.getInstance().outputLogEntries();
    }

}
