package com.taf.automation.ui.support;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.HolidayType;
import de.jollyday.ManagerParameters;
import de.jollyday.util.CalendarUtil;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

/**
 * This class provides some actions on dates that may be useful
 */
public class DateActions {
    private HolidayCalendar holidayCalendar;
    private HolidayManager holidayManager;
    private HolidayType holidayType;
    private String[] regions;
    private ZoneId zone;
    private Set<DayOfWeek> nonBusinessDays;

    public DateActions() {
        holidayCalendar = HolidayCalendar.CANADA;
        holidayManager = null;
        holidayType = null; // Both official and unofficial
        regions = null; // Only common holidays across regions
        zone = ZoneId.systemDefault();
        nonBusinessDays = getStandardNonBusinessDays();
    }

    public DateActions withHolidayCalendar(HolidayCalendar holidayCalendar) {
        this.holidayCalendar = holidayCalendar;

        // If the holiday calendar is changed, then holiday manager needs to be re-initialized
        holidayManager = null;

        return this;
    }

    public DateActions withZone(ZoneId zone) {
        this.zone = zone;
        return this;
    }

    public DateActions withNonBusinessDays(Set<DayOfWeek> nonBusinessDays) {
        this.nonBusinessDays = nonBusinessDays;
        return this;
    }

    public DateActions withHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
        return this;
    }

    public DateActions withRegions(String[] regions) {
        this.regions = regions;
        return this;
    }

    private HolidayManager getHolidayManager() {
        if (holidayManager == null) {
            holidayManager = HolidayManager.getInstance(ManagerParameters.create(holidayCalendar, null));
        }

        return holidayManager;
    }

    private Set<DayOfWeek> getStandardNonBusinessDays() {
        Set<DayOfWeek> standard = new HashSet<>();
        standard.add(DayOfWeek.SATURDAY);
        standard.add(DayOfWeek.SUNDAY);
        return standard;
    }

    /**
     * Get the possible holidays from previous, current &amp; next years
     *
     * @param date - Date to base the holidays on
     * @return Set of holidays for the previous, current &amp; next years
     */
    private Set<Holiday> getPossibleHolidays(Date date) {
        int currentYear = toLocalDate(date, zone).getYear();
        int previousYear = currentYear - 1;
        int nextYear = currentYear + 1;

        HolidayManager hm = getHolidayManager();
        Set<Holiday> holidays = hm.getHolidays(currentYear, regions);
        holidays.addAll(hm.getHolidays(previousYear, regions));
        holidays.addAll(hm.getHolidays(nextYear, regions));

        return holidays;
    }

    private boolean isNonBusinessDay(LocalDate date) {
        return nonBusinessDays.contains(date.getDayOfWeek());
    }

    private boolean isHoliday(Set<Holiday> holidays, LocalDate date) {
        return new CalendarUtil().contains(holidays, date, holidayType);
    }

    /**
     * Calculate the business day that is the specified days from specified date
     *
     * @param date     - Date
     * @param daysFrom - Days from (positive in future, negative in the past relative to the specified date)
     * @param next     - true to move to next business day, false to move to previous business day
     * @return the business day that is the specified number of days from the specified date.  If the resulting date
     * falls on a holiday or non-business day, then it is shifted to the next/previous business day as necessary.
     */
    private Date businessDay(Date date, int daysFrom, boolean next) {
        LocalDate result = toLocalDate(date, zone).plusDays(daysFrom);

        int count = 0;
        Set<Holiday> holidays = getPossibleHolidays(date);
        int moveDays = (next) ? 1 : -1;
        while (isNonBusinessDay(result) || isHoliday(holidays, result)) {
            result = result.plusDays(moveDays);
            count++;
            assertThat("Could not calculate business day tried all days in year", count, lessThan(370));
        }

        return toDate(result, zone);
    }

    public Date nextBusinessDay(Date date, int daysFrom) {
        return businessDay(date, daysFrom, true);
    }

    public Date previousBusinessDay(Date date, int daysFrom) {
        return businessDay(date, daysFrom, false);
    }

    /**
     * Parses a string representing a date by trying a variety of different parsers.  The parse will try each parse
     * pattern in turn. A parse is only deemed successful if it parses the whole of the input string.
     * The parser parses strictly - it does not allow for dates such as "February 942, 1996"
     *
     * @param value         - The date to parse
     * @param parsePatterns - The date format patterns to use, see SimpleDateFormat
     * @return null if cannot parse else Date
     */
    public static Date parseDateStrictly(String value, String... parsePatterns) {
        try {
            return DateUtils.parseDateStrictly(value, parsePatterns);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Convert Date to LocalDate using the system default zone
     *
     * @param dateToConvert - Date to convert
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date dateToConvert) {
        return toLocalDate(dateToConvert, ZoneId.systemDefault());
    }

    /**
     * Convert Date to LocalDate using specified zone<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>Use ZoneId.getAvailableZoneIds() to all the valid zones strings</LI>
     * <LI>Use ZoneId.of with a valid zone string</LI>
     * </OL>
     *
     * @param dateToConvert - Date to convert
     * @param zone          - Zone to use
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date dateToConvert, ZoneId zone) {
        return dateToConvert.toInstant()
                .atZone(zone)
                .toLocalDate();
    }

    /**
     * Convert LocalDate to Date using specified zone
     *
     * @param dateToConvert - Date to convert
     * @return Date
     */
    public static Date toDate(LocalDate dateToConvert) {
        return toDate(dateToConvert, ZoneId.systemDefault());
    }

    /**
     * Convert LocalDate to Date using specified zone<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>Use ZoneId.getAvailableZoneIds() to all the valid zones strings</LI>
     * <LI>Use ZoneId.of with a valid zone string</LI>
     * </OL>
     *
     * @param dateToConvert - Date to convert
     * @param zone          - Zone to use
     * @return Date
     */
    public static Date toDate(LocalDate dateToConvert, ZoneId zone) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(zone)
                .toInstant());
    }

    /**
     * @param date    - The time value to be formatted into a time string
     * @param pattern - The pattern describing the date and time format.  (See SimpleDateFormat JavaDocs for more details)
     * @return the date formatted as a string in the specified pattern
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

}
