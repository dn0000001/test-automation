package com.taf.automation.ui.support;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.HolidayType;
import de.jollyday.ManagerParameters;
import de.jollyday.util.CalendarUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

/**
 * This class provides some actions on dates that may be useful
 */
public class DateActions {
    private static final String NEW_YEARS_DAY = "NEW_YEAR";
    private static final String BOXING_DAY = "BOXING_DAY";

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

    public DateActions copy() {
        DateActions cp = new DateActions();

        cp.holidayCalendar = holidayCalendar;
        cp.holidayManager = null; // Will be initialized properly on first use
        cp.holidayType = holidayType;
        cp.regions = ArrayUtils.clone(regions);
        cp.zone = ObjectUtils.cloneIfPossible(zone);
        cp.nonBusinessDays = ObjectUtils.cloneIfPossible(nonBusinessDays);

        return cp;
    }

    public DateActions withHolidayCalendar(String value) {
        try {
            HolidayCalendar calendar = HolidayCalendar.valueOf(value.toUpperCase());
            return withHolidayCalendar(calendar);
        } catch (Exception ex) {
            return this;
        }
    }

    public DateActions withHolidayCalendar(HolidayCalendar holidayCalendar) {
        this.holidayCalendar = holidayCalendar;

        // If the holiday calendar is changed, then holiday manager needs to be re-initialized
        holidayManager = null;

        return this;
    }

    public DateActions withZone(String value) {
        try {
            ZoneId zoneId = ZoneId.of(value);
            return withZone(zoneId);
        } catch (Exception ex) {
            return this;
        }
    }

    public DateActions withZone(ZoneId zone) {
        this.zone = zone;
        return this;
    }

    public DateActions withNonBusinessDays(Set<DayOfWeek> nonBusinessDays) {
        this.nonBusinessDays = nonBusinessDays;
        return this;
    }

    public DateActions withNonBusinessDay(String value) {
        try {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(value.toUpperCase());
            return withNonBusinessDay(dayOfWeek);
        } catch (Exception ex) {
            return this;
        }
    }

    public DateActions withNonBusinessDay(DayOfWeek dayOfWeek) {
        nonBusinessDays.add(dayOfWeek);
        return this;
    }

    public DateActions withHolidayType(String value) {
        try {
            HolidayType type = HolidayType.valueOf(value.toUpperCase());
            return withHolidayType(type);
        } catch (Exception ex) {
            return this;
        }
    }

    public DateActions withHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
        return this;
    }

    public DateActions withRegions(String[] regions) {
        this.regions = regions;
        return this;
    }

    public DateActions withRegion(String region) {
        if (region != null) {
            regions = ArrayUtils.add(regions, region);
        }

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

        // Certain holidays that land on the weekend should be shifted to when they are observed
        holidays.addAll(getHolidaysThatLandOnWeekendWhichAreShifted(holidays));

        return holidays;
    }

    private Set<Holiday> getHolidaysThatLandOnWeekendWhichAreShifted(Set<Holiday> holidays) {
        Set<Holiday> shiftedHolidays = new HashSet<>();

        if (holidayCalendar == HolidayCalendar.UNITED_STATES) {
            shiftedHolidays.addAll(getUnitedStatesHolidaysThatLandOnWeekendWhichAreShifted(holidays));
        } else if (holidayCalendar == HolidayCalendar.CANADA) {
            shiftedHolidays.addAll(getCanadianHolidaysThatLandOnWeekendWhichAreShifted(holidays));
        }

        return shiftedHolidays;
    }

    private Set<Holiday> getUnitedStatesHolidaysThatLandOnWeekendWhichAreShifted(Set<Holiday> holidays) {
        Set<Holiday> shiftedHolidays = new HashSet<>();

        // Certain holidays that land on the weekend should be shifted to either Friday or Monday when they are observed
        List<Holiday> problemHolidays = holidays.stream().filter(this::isWeekEndHolidayToShift).collect(Collectors.toList());
        for (Holiday holiday : problemHolidays) {
            Date date = toDate(holiday.getDate());
            if (holiday.getDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                LocalDate shiftedDate = toLocalDate(DateUtils.addDays(date, -1));
                Holiday shiftedHoliday = new Holiday(shiftedDate, holiday.getPropertiesKey(), holiday.getType());
                shiftedHolidays.add(shiftedHoliday);
            } else if (holiday.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                LocalDate shiftedDate = toLocalDate(DateUtils.addDays(date, 1));
                Holiday shiftedHoliday = new Holiday(shiftedDate, holiday.getPropertiesKey(), holiday.getType());
                shiftedHolidays.add(shiftedHoliday);
            }
        }

        return shiftedHolidays;
    }

    /**
     * Get Canadian Holidays That Land On Weekend Which Are Shifted
     * <B>Notes:</B>
     * <OL>
     * <LI>
     * This is based on information from
     * <A HREF="https://www.timeanddate.com/holidays/canada">https://www.timeanddate.com/holidays/canada</A>
     * </LI>
     * </OL>
     *
     * @param holidays - Holidays to check if they need to be shifted
     * @return Holidays that were shifted
     */
    private Set<Holiday> getCanadianHolidaysThatLandOnWeekendWhichAreShifted(Set<Holiday> holidays) {
        Set<Holiday> shiftedHolidays = new HashSet<>();

        List<Holiday> problemHolidays = holidays.stream().filter(this::isWeekEndHolidayToShift).collect(Collectors.toList());
        for (Holiday holiday : problemHolidays) {
            Date date = toDate(holiday.getDate());
            if (StringUtils.equals(holiday.getPropertiesKey(), BOXING_DAY)) {
                // Boxing day does not seem to move in general
                // However, in 2016 it landed on a Monday and shifted to Tuesday by Christmas.
                // So, at this point I will do nothing
            } else if (holiday.getDate().getDayOfWeek() == DayOfWeek.SATURDAY
                    && StringUtils.equals(holiday.getPropertiesKey(), NEW_YEARS_DAY)
            ) {
                // Only New Years Day shifts if it lands on a Saturday where as other Holidays are not shifted
                // In 2000, New Years Day landed on Saturday and shifted to Monday.
                // However, all other holidays in 2000 that landed on Saturday were not shifted
                LocalDate shiftedDate = toLocalDate(DateUtils.addDays(date, 2));
                Holiday shiftedHoliday = new Holiday(shiftedDate, holiday.getPropertiesKey(), holiday.getType());
                shiftedHolidays.add(shiftedHoliday);
            } else if (holiday.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                // Most holidays that land on Sunday are shifted to Monday
                // In 2017, New Years Day landed on Sunday and shifted to Monday
                // In 2001, Saint Jean Baptiste Day landed on Sunday and shifted to Monday
                // In 2001, Canada Day landed on Sunday and shifted to Monday
                // In 2001, Remembrance Day landed on Sunday and shifted to Monday
                // In 2005, Christmas Day landed on Sunday and shifted to Monday
                LocalDate shiftedDate = toLocalDate(DateUtils.addDays(date, 1));
                Holiday shiftedHoliday = new Holiday(shiftedDate, holiday.getPropertiesKey(), holiday.getType());
                shiftedHolidays.add(shiftedHoliday);
            }
        }

        return shiftedHolidays;
    }

    private boolean isWeekEndHolidayToShift(Holiday holiday) {
        return getStandardNonBusinessDays().contains(holiday.getDate().getDayOfWeek()) && isProblemHoliday(holiday);
    }

    private boolean isProblemHoliday(Holiday holiday) {
        if (holidayCalendar == HolidayCalendar.UNITED_STATES) {
            // Note:  I have only put federal holidays
            return StringUtils.equals(holiday.getPropertiesKey(), NEW_YEARS_DAY)
                    || StringUtils.equals(holiday.getPropertiesKey(), "INDEPENDENCE_DAY")
                    || StringUtils.equals(holiday.getPropertiesKey(), "CHRISTMAS")
                    ;
        }

        if (holidayCalendar == HolidayCalendar.CANADA) {
            return StringUtils.equals(holiday.getPropertiesKey(), NEW_YEARS_DAY)
                    || StringUtils.equals(holiday.getPropertiesKey(), "NATIONAL_DAY")
                    || StringUtils.equals(holiday.getPropertiesKey(), "CHRISTMAS")
                    || StringUtils.equals(holiday.getPropertiesKey(), BOXING_DAY)
                    || StringUtils.equals(holiday.getPropertiesKey(), "REMEMBRANCE")
                    || StringUtils.equals(holiday.getPropertiesKey(), "SAINT_JEAN_BAPTISTE_DAY")
                    ;
        }

        return false;
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
     * Calculate the business day that is the specified number of business days from the specified date.<BR>
     * <B>Note: </B> When you buy stuff online it will say delivery in (something like) 2-4 business days.  This means
     * that the shop only works on a business day (Monday to Friday excluding holidays) to process your order.  This
     * method calculates that date.
     *
     * @param date             - Date
     * @param businessDaysFrom - Days from (positive in future, negative in the past relative to the specified date)
     * @param next             - true to move to next business day, false to move to previous business day
     * @return the business day that is the specified number of business days from the specified date
     */
    private Date onlyBusinessDays(Date date, int businessDaysFrom, boolean next) {
        int count = 0;
        int businessDays = 0;
        int specifiedBusinessDaysFrom = Math.abs(businessDaysFrom);
        LocalDate result = toLocalDate(date, zone);
        Set<Holiday> holidays = getPossibleHolidays(date);

        // Handle special case if business days from is 0 and start date is non-business day or holiday
        if (specifiedBusinessDaysFrom == 0 && (isNonBusinessDay(result) || isHoliday(holidays, result))) {
            businessDays--;
        }

        int moveDays = (next) ? 1 : -1;
        while (businessDays < specifiedBusinessDaysFrom) {
            result = result.plusDays(moveDays);
            if (!isNonBusinessDay(result) && !isHoliday(holidays, result)) {
                businessDays++;
            }

            count++;
            assertThat("Could not calculate business day tried all days in year", count, lessThan(370));
        }

        return toDate(result, zone);
    }

    public Date onlyBusinessDaysAfter(Date date, int daysFrom) {
        return onlyBusinessDays(date, daysFrom, true);
    }

    public Date onlyBusinessDaysBefore(Date date, int daysFrom) {
        return onlyBusinessDays(date, daysFrom, false);
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

    /**
     * The number of days between the start date and end date
     *
     * @param startDate - Start Date
     * @param endDate   - End Date
     * @return the amount of time between startDate and endDate in terms of days;
     * positive if endDate is later than startDate, negative if earlier
     */
    public static Long daysBetween(Date startDate, Date endDate) {
        return between(ChronoUnit.DAYS, startDate, endDate);
    }

    /**
     * The number of ChronoUnit between the start date and end date
     *
     * @param chronoUnit - ChronoUnit
     * @param startDate  - Start Date
     * @param endDate    - End Date
     * @return the amount of time between startDate and endDate in terms of this unit;
     * positive if endDate is later than startDate, negative if earlier
     */
    public static Long between(ChronoUnit chronoUnit, Date startDate, Date endDate) {
        LocalDate start = DateActions.toLocalDate(startDate);
        LocalDate end = DateActions.toLocalDate(endDate);
        return chronoUnit.between(start, end);
    }

    /**
     * Get SimpleDateFormat with the pattern applied<BR>
     * <B>Note: </B> Main use should be for JEXL expressions
     *
     * @param pattern - The pattern describing the date and time format
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

}
