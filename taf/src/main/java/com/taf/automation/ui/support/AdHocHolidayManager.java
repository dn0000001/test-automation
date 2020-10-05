package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;
import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.EthiopianOrthodoxHoliday;
import de.jollyday.config.Fixed;
import de.jollyday.config.FixedWeekdayBetweenFixed;
import de.jollyday.config.FixedWeekdayInMonth;
import de.jollyday.config.FixedWeekdayRelativeToFixed;
import de.jollyday.config.HebrewHoliday;
import de.jollyday.config.HinduHoliday;
import de.jollyday.config.Holiday;
import de.jollyday.config.Holidays;
import de.jollyday.config.IslamicHoliday;
import de.jollyday.config.RelativeToEasterSunday;
import de.jollyday.config.RelativeToFixed;
import de.jollyday.config.RelativeToWeekdayInMonth;
import de.jollyday.datasource.ConfigurationDataSource;
import de.jollyday.impl.DefaultHolidayManager;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Holiday Manager for Ad Hoc holidays (added by user on the fly)
 */
public class AdHocHolidayManager extends DefaultHolidayManager {
    private final ConfigurationDataSource customConfigurationDataSource;
    private final ManagerParameter customManagerParameter;

    public AdHocHolidayManager() {
        // Use existing holiday list to initialize
        HolidayManager hm = HolidayManager.getInstance(ManagerParameters.create("ca", null));
        customConfigurationDataSource = Utils.deepCopy(hm.getConfigurationDataSource());
        customManagerParameter = Utils.deepCopy(hm.getManagerParameter());
        configuration = customConfigurationDataSource.getConfiguration(customManagerParameter);
        configuration.setDescription("AdHocHolidayManager");
        configuration.setHierarchy("ad-hoc");

        // We don't want any of the previous holidays as such remove them
        removeAllStoredHolidays();
        configuration.getSubConfigurations().removeIf(item -> true);
    }

    /**
     * Remove all the stored holidays from the loading of the holiday manager used for setup
     */
    private void removeAllStoredHolidays() {
        getStoredHolidays().getFixed().removeIf(item -> true);
        getStoredHolidays().getRelativeToFixed().removeIf(item -> true);
        getStoredHolidays().getRelativeToWeekdayInMonth().removeIf(item -> true);
        getStoredHolidays().getFixedWeekday().removeIf(item -> true);
        getStoredHolidays().getChristianHoliday().removeIf(item -> true);
        getStoredHolidays().getIslamicHoliday().removeIf(item -> true);
        getStoredHolidays().getFixedWeekdayBetweenFixed().removeIf(item -> true);
        getStoredHolidays().getFixedWeekdayRelativeToFixed().removeIf(item -> true);
        getStoredHolidays().getHinduHoliday().removeIf(item -> true);
        getStoredHolidays().getHebrewHoliday().removeIf(item -> true);
        getStoredHolidays().getEthiopianOrthodoxHoliday().removeIf(item -> true);
        getStoredHolidays().getRelativeToEasterSunday().removeIf(item -> true);
    }

    private Holidays getStoredHolidays() {
        return configuration.getHolidays();
    }

    @Override
    public ConfigurationDataSource getConfigurationDataSource() {
        return customConfigurationDataSource;
    }

    @Override
    public ManagerParameter getManagerParameter() {
        return customManagerParameter;
    }

    /**
     * Add an Ad Hoc Holiday<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>There are 2 different Holiday classes in this library.  This method is using de.jollyday.config.Holiday
     * &amp; not the de.jollyday.Holiday</LI>
     * <LI>For convenience the base holiday class is used for the parameter and cast to the concrete holiday by
     * the method</LI>
     * </OL>
     * <B>Supported Holiday Types/Classes:</B>
     * <OL>
     * <LI>Fixed</LI>
     * <LI>RelativeToFixed</LI>
     * <LI>RelativeToWeekdayInMonth</LI>
     * <LI>FixedWeekdayInMonth</LI>
     * <LI>ChristianHoliday</LI>
     * <LI>IslamicHoliday</LI>
     * <LI>FixedWeekdayBetweenFixed</LI>
     * <LI>FixedWeekdayRelativeToFixed</LI>
     * <LI>HinduHoliday</LI>
     * <LI>HebrewHoliday</LI>
     * <LI>EthiopianOrthodoxHoliday</LI>
     * <LI>RelativeToEasterSunday</LI>
     * </OL>
     *
     * @param holiday - Concrete Holiday to add from supported types
     * @return AdHocHolidayManager
     */
    public AdHocHolidayManager withHoliday(Holiday holiday) {
        if (Fixed.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getFixed().add((Fixed) holiday);
        } else if (RelativeToFixed.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getRelativeToFixed().add((RelativeToFixed) holiday);
        } else if (RelativeToWeekdayInMonth.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getRelativeToWeekdayInMonth().add((RelativeToWeekdayInMonth) holiday);
        } else if (FixedWeekdayInMonth.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getFixedWeekday().add((FixedWeekdayInMonth) holiday);
        } else if (ChristianHoliday.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getChristianHoliday().add((ChristianHoliday) holiday);
        } else if (IslamicHoliday.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getIslamicHoliday().add((IslamicHoliday) holiday);
        } else if (FixedWeekdayBetweenFixed.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getFixedWeekdayBetweenFixed().add((FixedWeekdayBetweenFixed) holiday);
        } else if (FixedWeekdayRelativeToFixed.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getFixedWeekdayRelativeToFixed().add((FixedWeekdayRelativeToFixed) holiday);
        } else if (HinduHoliday.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getHinduHoliday().add((HinduHoliday) holiday);
        } else if (HebrewHoliday.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getHebrewHoliday().add((HebrewHoliday) holiday);
        } else if (EthiopianOrthodoxHoliday.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getEthiopianOrthodoxHoliday().add((EthiopianOrthodoxHoliday) holiday);
        } else if (RelativeToEasterSunday.class.isAssignableFrom(holiday.getClass())) {
            getStoredHolidays().getRelativeToEasterSunday().add((RelativeToEasterSunday) holiday);
        } else {
            assertThat("Unsupported Holiday Implementation:  " + holiday.getClass(), false);
        }

        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (object.getClass() != this.getClass()) {
            return false;
        }

        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat(aggregator, getStoredHolidays(), ((AdHocHolidayManager) object).getStoredHolidays());
        return aggregator.getFailureCount() == 0;
    }

    @Override
    public int hashCode() {
        List<String> excludeFields = new ArrayList<>();
        return HashCodeBuilder.reflectionHashCode(this, excludeFields);
    }

}
