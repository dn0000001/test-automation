package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.DateActions;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.Helper;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayType;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@Listeners(AllureTestNGListener.class)
public class DateActionsTest {
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final Date april18anchor = DateActions.parseDateStrictly("04/18/2019", DATE_FORMAT);
    private static final Date april19anchor = DateActions.parseDateStrictly("04/19/2019", DATE_FORMAT);
    private static final Date april20anchor = DateActions.parseDateStrictly("04/20/2019", DATE_FORMAT);
    private static final Date jun30anchor = DateActions.parseDateStrictly("06/30/2019", DATE_FORMAT);
    private static final Date jul1anchor = DateActions.parseDateStrictly("07/01/2019", DATE_FORMAT);
    private static final Date jul2anchor = DateActions.parseDateStrictly("07/02/2019", DATE_FORMAT);
    private static final Date sept15anchor = DateActions.parseDateStrictly("09/15/2019", DATE_FORMAT);
    private static final Date sept18anchor = DateActions.parseDateStrictly("09/18/2019", DATE_FORMAT);
    private static final Date oct13anchor = DateActions.parseDateStrictly("10/13/2019", DATE_FORMAT);
    private static final Date oct14anchor = DateActions.parseDateStrictly("10/14/2019", DATE_FORMAT);
    private static final Date oct15anchor = DateActions.parseDateStrictly("10/15/2019", DATE_FORMAT);
    private static final Date dec24anchor = DateActions.parseDateStrictly("12/24/2019", DATE_FORMAT);
    private static final Date dec25anchor = DateActions.parseDateStrictly("12/25/2019", DATE_FORMAT);
    private static final Date dec26anchor = DateActions.parseDateStrictly("12/26/2019", DATE_FORMAT);

    private static final Date april22 = DateActions.parseDateStrictly("04/22/2019", DATE_FORMAT);
    private static final Date jun28 = DateActions.parseDateStrictly("06/28/2019", DATE_FORMAT);
    private static final Date sept13 = DateActions.parseDateStrictly("09/13/2019", DATE_FORMAT);
    private static final Date sept16 = DateActions.parseDateStrictly("09/16/2019", DATE_FORMAT);
    private static final Date sept17 = DateActions.parseDateStrictly("09/17/2019", DATE_FORMAT);
    private static final Date oct11 = DateActions.parseDateStrictly("10/11/2019", DATE_FORMAT);
    private static final Date sept19 = DateActions.parseDateStrictly("09/19/2019", DATE_FORMAT);
    private static final Date sept23 = DateActions.parseDateStrictly("09/23/2019", DATE_FORMAT);
    private static final Date dec24 = DateActions.parseDateStrictly("12/24/2019", DATE_FORMAT);
    private static final Date dec27 = DateActions.parseDateStrictly("12/27/2019", DATE_FORMAT);
    private static final Date sept11 = DateActions.parseDateStrictly("09/11/2019", DATE_FORMAT);
    private static final Date sept25 = DateActions.parseDateStrictly("09/25/2019", DATE_FORMAT);

    @Features("DateActions")
    @Stories("Result Date Is Business Day")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsBusinessDayTest() {
        DateActions dateActions = new DateActions();
        Date actual = dateActions.nextBusinessDay(sept18anchor, 0);
        assertThat("Next Business Day - No Days Added", actual, equalTo(sept18anchor));

        actual = dateActions.previousBusinessDay(sept18anchor, 0);
        assertThat("Previous Business Day - No Days Added", actual, equalTo(sept18anchor));

        actual = dateActions.nextBusinessDay(sept18anchor, 1);
        assertThat("Next Business Day - 1 Day After", actual, equalTo(sept19));

        actual = dateActions.previousBusinessDay(sept18anchor, -1);
        assertThat("Previous Business Day - 1 Day Before", actual, equalTo(sept17));

        actual = dateActions.nextBusinessDay(sept18anchor, 7);
        assertThat("Next Business Day - 1 Week After", actual, equalTo(sept25));

        actual = dateActions.previousBusinessDay(sept18anchor, -7);
        assertThat("Previous Business Day - 1 Week Before", actual, equalTo(sept11));

        actual = dateActions.onlyBusinessDaysAfter(sept18anchor, 0);
        assertThat("Only Business Days After - No Days Added", actual, equalTo(sept18anchor));

        actual = dateActions.onlyBusinessDaysBefore(sept18anchor, 0);
        assertThat("Only Business Days Before - No Days Added", actual, equalTo(sept18anchor));

        actual = dateActions.onlyBusinessDaysAfter(sept18anchor, 1);
        assertThat("Only Business Days After - 1 Day After", actual, equalTo(sept19));

        actual = dateActions.onlyBusinessDaysBefore(sept18anchor, -1);
        assertThat("Only Business Days Before - 1 Day Before", actual, equalTo(sept17));

        actual = dateActions.onlyBusinessDaysAfter(sept18anchor, 5);
        assertThat("Only Business Days After - 1 Week After", actual, equalTo(sept25));

        actual = dateActions.onlyBusinessDaysBefore(sept18anchor, -5);
        assertThat("Only Business Days Before - 1 Week Before", actual, equalTo(sept11));
    }

    @Features("DateActions")
    @Stories("Result Date Is Non-Business Day")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsNonBusinessDayTest() {
        DateActions dateActions = new DateActions();
        Date actual = dateActions.nextBusinessDay(sept15anchor, 0);
        assertThat("Next Business Day - No Days Added", actual, equalTo(sept16));

        actual = dateActions.previousBusinessDay(sept15anchor, 0);
        assertThat("Previous Business Day - No Days Added", actual, equalTo(sept13));

        actual = dateActions.nextBusinessDay(sept18anchor, 3);
        assertThat("Next Business Day - 3 Days After", actual, equalTo(sept23));

        actual = dateActions.previousBusinessDay(sept18anchor, -3);
        assertThat("Previous Business Day - 3 Days Before", actual, equalTo(sept13));

        actual = dateActions.onlyBusinessDaysAfter(sept15anchor, 0);
        assertThat("Only Business Days After - No Days Added", actual, equalTo(sept16));

        actual = dateActions.onlyBusinessDaysBefore(sept15anchor, 0);
        assertThat("Only Business Days Before - No Days Added", actual, equalTo(sept13));

        actual = dateActions.onlyBusinessDaysAfter(sept18anchor, 3);
        assertThat("Only Business Days After - 3 Days After", actual, equalTo(sept23));

        actual = dateActions.onlyBusinessDaysBefore(sept18anchor, -3);
        assertThat("Only Business Days Before - 3 Days Before", actual, equalTo(sept13));
    }

    @Features("DateActions")
    @Stories("Result Date Is Christmas")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsChristmasTest() {
        DateActions dateActions = new DateActions().withRegions(new String[]{"on"});
        Date actual = dateActions.nextBusinessDay(dec25anchor, 0);
        assertThat("Christmas - Next Business Day - No Days Added", actual, equalTo(dec27));

        actual = dateActions.previousBusinessDay(dec25anchor, 0);
        assertThat("Christmas - Previous Business Day - No Days Added", actual, equalTo(dec24));

        actual = dateActions.nextBusinessDay(dec24anchor, 1);
        assertThat("Christmas - Next Business Day - 1 Day After", actual, equalTo(dec27));

        actual = dateActions.previousBusinessDay(dec26anchor, -1);
        assertThat("Christmas - Previous Business Day - 1 Day Before", actual, equalTo(dec24));

        actual = dateActions.onlyBusinessDaysAfter(dec25anchor, 0);
        assertThat("Christmas - Only Business Days After - No Days Added", actual, equalTo(dec27));

        actual = dateActions.onlyBusinessDaysBefore(dec25anchor, 0);
        assertThat("Christmas - Only Business Days Before - No Days Added", actual, equalTo(dec24));

        actual = dateActions.onlyBusinessDaysAfter(dec24anchor, 1);
        assertThat("Christmas - Only Business Days After - 1 Day After", actual, equalTo(dec27));

        actual = dateActions.onlyBusinessDaysBefore(dec26anchor, -1);
        assertThat("Christmas - Only Business Days Before - 1 Day Before", actual, equalTo(dec24));
    }

    @Features("DateActions")
    @Stories("Result Date Is Good Friday")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsGoodFridayTest() {
        DateActions dateActions = new DateActions();
        Date actual = dateActions.nextBusinessDay(april19anchor, 0);
        assertThat("Good Friday - Next Business Day - No Days Added", actual, equalTo(april22));

        actual = dateActions.previousBusinessDay(april19anchor, 0);
        assertThat("Good Friday - Previous Business Day - No Days Added", actual, equalTo(april18anchor));

        actual = dateActions.nextBusinessDay(april18anchor, 1);
        assertThat("Good Friday - Next Business Day - 1 Day After", actual, equalTo(april22));

        actual = dateActions.previousBusinessDay(april20anchor, -1);
        assertThat("Good Friday - Previous Business Day - 1 Day Before", actual, equalTo(april18anchor));

        actual = dateActions.onlyBusinessDaysAfter(april19anchor, 0);
        assertThat("Good Friday - Only Business Days After - No Days Added", actual, equalTo(april22));

        actual = dateActions.onlyBusinessDaysBefore(april19anchor, 0);
        assertThat("Good Friday - Only Business Days Before - No Days Added", actual, equalTo(april18anchor));

        actual = dateActions.onlyBusinessDaysAfter(april18anchor, 1);
        assertThat("Good Friday - Only Business Days After - 1 Day After", actual, equalTo(april22));

        actual = dateActions.onlyBusinessDaysBefore(april20anchor, -1);
        assertThat("Good Friday - Only Business Days Before - 1 Day Before", actual, equalTo(april18anchor));
    }

    @Features("DateActions")
    @Stories("Result Date Is Canada Day")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsCanadaDayTest() {
        DateActions dateActions = new DateActions();
        Date actual = dateActions.nextBusinessDay(jul1anchor, 0);
        assertThat("Canada Day - Next Business Day - No Days Added", actual, equalTo(jul2anchor));

        actual = dateActions.previousBusinessDay(jul1anchor, 0);
        assertThat("Canada Day - Previous Business Day - No Days Added", actual, equalTo(jun28));

        actual = dateActions.nextBusinessDay(jun30anchor, 1);
        assertThat("Canada Day - Next Business Day - 1 Day After", actual, equalTo(jul2anchor));

        actual = dateActions.previousBusinessDay(jul2anchor, -1);
        assertThat("Canada Day - Previous Business Day - 1 Day Before", actual, equalTo(jun28));

        actual = dateActions.onlyBusinessDaysAfter(jul1anchor, 0);
        assertThat("Canada Day - Only Business Days After - No Days Added", actual, equalTo(jul2anchor));

        actual = dateActions.onlyBusinessDaysBefore(jul1anchor, 0);
        assertThat("Canada Day - Only Business Days Before - No Days Added", actual, equalTo(jun28));

        actual = dateActions.onlyBusinessDaysAfter(jun30anchor, 1);
        assertThat("Canada Day - Only Business Days After - 1 Day After", actual, equalTo(jul2anchor));

        actual = dateActions.onlyBusinessDaysBefore(jul2anchor, -1);
        assertThat("Canada Day - Only Business Days Before - 1 Day Before", actual, equalTo(jun28));
    }

    @Features("DateActions")
    @Stories("Result Date Is Thanksgiving")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsThanksgivingTest() {
        DateActions dateActions = new DateActions().withRegions(new String[]{"on"});
        Date actual = dateActions.nextBusinessDay(oct14anchor, 0);
        assertThat("Thanksgiving - Next Business Day - No Days Added", actual, equalTo(oct15anchor));

        actual = dateActions.previousBusinessDay(oct14anchor, 0);
        assertThat("Thanksgiving - Previous Business Day - No Days Added", actual, equalTo(oct11));

        actual = dateActions.nextBusinessDay(oct13anchor, 1);
        assertThat("Thanksgiving - Next Business Day - 1 Day After", actual, equalTo(oct15anchor));

        actual = dateActions.previousBusinessDay(oct15anchor, -1);
        assertThat("Thanksgiving - Previous Business Day - 1 Day Before", actual, equalTo(oct11));

        actual = dateActions.onlyBusinessDaysAfter(oct14anchor, 0);
        assertThat("Thanksgiving - Only Business Days After - No Days Added", actual, equalTo(oct15anchor));

        actual = dateActions.onlyBusinessDaysBefore(oct14anchor, 0);
        assertThat("Thanksgiving - Only Business Days Before - No Days Added", actual, equalTo(oct11));

        actual = dateActions.onlyBusinessDaysAfter(oct13anchor, 1);
        assertThat("Thanksgiving - Only Business Days After - 1 Day After", actual, equalTo(oct15anchor));

        actual = dateActions.onlyBusinessDaysBefore(oct15anchor, -1);
        assertThat("Thanksgiving - Only Business Days Before - 1 Day Before", actual, equalTo(oct11));
    }

    @Features("DateActions")
    @Stories("Result Date Is Independence Day Shifted to Friday")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsIndependenceDayShiftedToFridayTest() {
        DateActions dateActions = new DateActions().withHolidayCalendar(HolidayCalendar.UNITED_STATES);
        // Independence Day in this case is July 3 because July 4th is a Saturday
        Date july3 = DateActions.parseDateStrictly("07/03/2020", DATE_FORMAT);

        Date july2 = DateActions.parseDateStrictly("07/02/2020", DATE_FORMAT);
        Date july5 = DateActions.parseDateStrictly("07/05/2020", DATE_FORMAT);
        Date july6 = DateActions.parseDateStrictly("07/06/2020", DATE_FORMAT);

        Date actual = dateActions.nextBusinessDay(july3, 0);
        assertThat("Independence Day - Next Business Day - No Days Added", actual, equalTo(july6));

        actual = dateActions.previousBusinessDay(july3, 0);
        assertThat("Independence Day - Previous Business Day - No Days Added", actual, equalTo(july2));

        actual = dateActions.nextBusinessDay(july2, 1);
        assertThat("Independence Day - Next Business Day - 1 Day After", actual, equalTo(july6));

        actual = dateActions.previousBusinessDay(july5, -1);
        assertThat("Independence Day - Previous Business Day - 1 Day Before", actual, equalTo(july2));

        actual = dateActions.onlyBusinessDaysAfter(july3, 0);
        assertThat("Independence Day - Only Business Days After - No Days Added", actual, equalTo(july6));

        actual = dateActions.onlyBusinessDaysBefore(july3, 0);
        assertThat("Independence Day - Only Business Days Before - No Days Added", actual, equalTo(july2));

        actual = dateActions.onlyBusinessDaysAfter(july2, 1);
        assertThat("Independence Day - Only Business Days After - 1 Day After", actual, equalTo(july6));

        actual = dateActions.onlyBusinessDaysBefore(july5, -1);
        assertThat("Independence Day - Only Business Days Before - 1 Day Before", actual, equalTo(july2));
    }

    @Features("DateActions")
    @Stories("Result Date Is Independence Day Shifted to Monday")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsIndependenceDayShiftedToMondayTest() {
        DateActions dateActions = new DateActions().withHolidayCalendar(HolidayCalendar.UNITED_STATES);
        // Independence Day in this case is July 5 because July 4th is a Sunday
        Date july5 = DateActions.parseDateStrictly("07/05/2021", DATE_FORMAT);

        Date july2 = DateActions.parseDateStrictly("07/02/2021", DATE_FORMAT);
        Date july6 = DateActions.parseDateStrictly("07/06/2021", DATE_FORMAT);

        Date actual = dateActions.nextBusinessDay(july5, 0);
        assertThat("Independence Day - Next Business Day - No Days Added", actual, equalTo(july6));

        actual = dateActions.previousBusinessDay(july5, 0);
        assertThat("Independence Day - Previous Business Day - No Days Added", actual, equalTo(july2));

        actual = dateActions.nextBusinessDay(july2, 1);
        assertThat("Independence Day - Next Business Day - 1 Day After", actual, equalTo(july6));

        actual = dateActions.previousBusinessDay(july6, -1);
        assertThat("Independence Day - Previous Business Day - 1 Day Before", actual, equalTo(july2));

        actual = dateActions.onlyBusinessDaysAfter(july5, 0);
        assertThat("Independence Day - Only Business Days After - No Days Added", actual, equalTo(july6));

        actual = dateActions.onlyBusinessDaysBefore(july5, 0);
        assertThat("Independence Day - Only Business Days Before - No Days Added", actual, equalTo(july2));

        actual = dateActions.onlyBusinessDaysAfter(july2, 1);
        assertThat("Independence Day - Only Business Days After - 1 Day After", actual, equalTo(july6));

        actual = dateActions.onlyBusinessDaysBefore(july6, -1);
        assertThat("Independence Day - Only Business Days Before - 1 Day Before", actual, equalTo(july2));
    }

    @Features("DateActions")
    @Stories("Canada - Result Date Is New Years Day Shifted to Monday when it lands on Saturday")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsNewYearsShiftedToMondayFromSaturdayTest() {
        DateActions dateActions = new DateActions();
        // New Years Day in this case is January 3 because January 1 is a Saturday
        Date jan3 = DateActions.parseDateStrictly("01/03/2000", DATE_FORMAT);

        Date dec31 = DateActions.parseDateStrictly("12/31/1999", DATE_FORMAT);
        Date jan4 = DateActions.parseDateStrictly("01/04/2000", DATE_FORMAT);

        Date actual = dateActions.nextBusinessDay(jan3, 0);
        assertThat("New Years Day - Next Business Day - No Days Added", actual, equalTo(jan4));

        actual = dateActions.previousBusinessDay(jan3, 0);
        assertThat("New Years Day - Previous Business Day - No Days Added", actual, equalTo(dec31));

        actual = dateActions.nextBusinessDay(dec31, 1);
        assertThat("New Years Day - Next Business Day - 1 Day After", actual, equalTo(jan4));

        actual = dateActions.previousBusinessDay(jan4, -1);
        assertThat("New Years Day - Previous Business Day - 1 Day Before", actual, equalTo(dec31));

        actual = dateActions.onlyBusinessDaysAfter(jan3, 0);
        assertThat("New Years Day - Only Business Days After - No Days Added", actual, equalTo(jan4));

        actual = dateActions.onlyBusinessDaysBefore(jan3, 0);
        assertThat("New Years Day - Only Business Days Before - No Days Added", actual, equalTo(dec31));

        actual = dateActions.onlyBusinessDaysAfter(dec31, 1);
        assertThat("New Years Day - Only Business Days After - 1 Day After", actual, equalTo(jan4));

        actual = dateActions.onlyBusinessDaysBefore(jan4, -1);
        assertThat("New Years Day - Only Business Days Before - 1 Day Before", actual, equalTo(dec31));
    }

    @Features("DateActions")
    @Stories("Canada - Result Date Is New Years Day Shifted to Monday when it lands on Sunday")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performResultDateIsNewYearsShiftedToMondayFromSundayTest() {
        DateActions dateActions = new DateActions();
        // New Years Day in this case is January 2 because January 1 is a Sunday
        Date jan2 = DateActions.parseDateStrictly("01/02/2017", DATE_FORMAT);

        Date dec30 = DateActions.parseDateStrictly("12/30/2016", DATE_FORMAT);
        Date jan3 = DateActions.parseDateStrictly("01/03/2017", DATE_FORMAT);

        Date actual = dateActions.nextBusinessDay(jan2, 0);
        assertThat("New Years Day - Next Business Day - No Days Added", actual, equalTo(jan3));

        actual = dateActions.previousBusinessDay(jan2, 0);
        assertThat("New Years Day - Previous Business Day - No Days Added", actual, equalTo(dec30));

        actual = dateActions.nextBusinessDay(dec30, 1);
        assertThat("New Years Day - Next Business Day - 1 Day After", actual, equalTo(jan3));

        actual = dateActions.previousBusinessDay(jan3, -1);
        assertThat("New Years Day - Previous Business Day - 1 Day Before", actual, equalTo(dec30));

        actual = dateActions.onlyBusinessDaysAfter(jan2, 0);
        assertThat("New Years Day - Only Business Days After - No Days Added", actual, equalTo(jan3));

        actual = dateActions.onlyBusinessDaysBefore(jan2, 0);
        assertThat("New Years Day - Only Business Days Before - No Days Added", actual, equalTo(dec30));

        actual = dateActions.onlyBusinessDaysAfter(dec30, 1);
        assertThat("New Years Day - Only Business Days After - 1 Day After", actual, equalTo(jan3));

        actual = dateActions.onlyBusinessDaysBefore(jan3, -1);
        assertThat("New Years Day - Only Business Days Before - 1 Day Before", actual, equalTo(dec30));
    }

    @Features("DateActions")
    @Stories("Validate methods used by Jexl")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performJexlTest() {
        DateActions original = new DateActions()
                .withHolidayCalendar(HolidayCalendar.UNITED_STATES.toString())
                .withHolidayType(HolidayType.OFFICIAL_HOLIDAY.toString())
                .withRegion("ca")
                .withZone("US/Pacific")
                .withNonBusinessDay(DayOfWeek.MONDAY.toString());
        DateActions deepCopy = original.copy();
        validateCopy("Validate Deep Copy Equals Original", deepCopy, original);

        DateActions modifiedOriginal = new DateActions()
                .withHolidayCalendar(HolidayCalendar.CANADA.toString())
                .withHolidayType(HolidayType.UNOFFICIAL_HOLIDAY.toString())
                .withRegion("bc")
                .withZone("Canada/Pacific")
                .withNonBusinessDay(DayOfWeek.TUESDAY.toString());

        // Change original to be equal to modifiedOriginal
        original.withHolidayCalendar(HolidayCalendar.CANADA.toString())
                .withHolidayType(HolidayType.UNOFFICIAL_HOLIDAY.toString())
                .withRegions(new String[0])
                .withRegion("bc")
                .withZone("Canada/Pacific")
                .withNonBusinessDays(new HashSet<>())
                .withNonBusinessDay(DayOfWeek.SATURDAY.toString())
                .withNonBusinessDay(DayOfWeek.SUNDAY.toString())
                .withNonBusinessDay(DayOfWeek.TUESDAY.toString());
        validateCopy("Validate Original Equals Modified", modifiedOriginal, original);
        validateDifferent("Validate Deep Copy is different than original #1", deepCopy, original);

        DateActions modifiedDeepCopy = new DateActions()
                .withHolidayCalendar(HolidayCalendar.IRELAND.toString())
                .withHolidayType(HolidayType.UNOFFICIAL_HOLIDAY.toString())
                .withRegions(null)
                .withZone("Europe/Dublin")
                .withNonBusinessDay(DayOfWeek.FRIDAY.toString());

        // Change deepCopy to be equal to modifiedDeepCopy
        deepCopy.withHolidayCalendar(HolidayCalendar.IRELAND.toString())
                .withHolidayType(HolidayType.UNOFFICIAL_HOLIDAY.toString())
                .withRegions(null)
                .withZone("Europe/Dublin")
                .withNonBusinessDays(new HashSet<>())
                .withNonBusinessDay(DayOfWeek.SATURDAY.toString())
                .withNonBusinessDay(DayOfWeek.SUNDAY.toString())
                .withNonBusinessDay(DayOfWeek.FRIDAY.toString());
        validateCopy("Validate Deep Copy Equals Modified", modifiedDeepCopy, deepCopy);
        validateDifferent("Validate Deep Copy is different than original #2", deepCopy, original);
    }

    @Step("{0}")
    private void validateCopy(String log, DateActions actual, DateActions expected) {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);
        Helper.assertThat(aggregator, actual, expected);
        Helper.assertThat(aggregator);
    }

    @Step("{0}")
    private void validateDifferent(String log, DateActions actual, DateActions expected) {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);
        Helper.assertThat(aggregator, actual, expected);
        assertThat("There were no differences", aggregator.getFailureCount(), greaterThan(0));
    }

}
