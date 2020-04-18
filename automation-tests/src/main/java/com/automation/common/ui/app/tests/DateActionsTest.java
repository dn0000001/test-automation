package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.DateActions;
import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

}
