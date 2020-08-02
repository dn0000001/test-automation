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

public class MoveClockOnceTest extends TestNGBase {
    private Long ticket;
    private String testId;

    @Features("Framework")
    @Stories("Validate moving the clock once")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({
            "test-id",
            "plus-days",
            "before-clock-move-actions-time",
            "after-clock-move-actions-time",
            "url"
    })
    @Test
    public void testMovingTheClock(
            @Optional("aaa") String id,
            @Optional("0") String plusDays,
            @Optional("1000") String beforeClockMoveActionsTime,
            @Optional("1000") String afterClockMoveActionsTime,
            @Optional("https://the-internet.herokuapp.com/tables") String url
    ) {
        // Indicate that test needs to move the clock
        testId = id;
        ticket = FakeDateManager.getInstance()
                .withContext(getContext())
                .openReservationForDays(testId, NumberUtils.toInt(plusDays, 0));
        Date startDate = FakeDateManager.getInstance().getCurrentSystemDate();

        // Perform whatever actions until the clock needs to be moved
        performBeforeClockMoveActions(beforeClockMoveActionsTime);

        // Wait for the reservation to be completed which moves the clock
        FakeDateManager.getInstance().waitForReservation(testId, ticket);

        Date endDate = FakeDateManager.getInstance().getSystemDate(ticket);
        validateMoveClockDays(startDate, endDate, plusDays);

        // Perform whatever actions after the clock was moved to complete the test
        performAfterClockMoveActions(url, afterClockMoveActionsTime);
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

    @Step("Perform After Clock Move Actions")
    private void performAfterClockMoveActions(String url, String afterClockMoveActionsTime) {
        getContext().getDriver().get(url);
        Utils.sleep(NumberUtils.toInt(afterClockMoveActionsTime, 1000));
    }

    @Test(dependsOnMethods = "testMovingTheClock", alwaysRun = true)
    private void cleanup() {
        //
        // This should be using the annotation AfterTest but I want to always see the output log entries
        // and AfterTest does not display this if it passes
        //
        FakeDateManager.getInstance().closeReservation(testId, ticket);
        FakeDateManager.getInstance().outputLogEntries();
    }

}
