package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.providers.LicenseContact;
import com.taf.automation.ui.support.providers.LicenseProvider;
import com.taf.automation.ui.support.providers.State;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Listeners(AllureTestNGListener.class)
public class LicenseProviderTest {
    private static final LicenseContact contactJohnSmith = getJohnSmith();
    private static final List<LicenseInfo> knownLicenses = getKnownLicenses();
    private static final List<LicenseInfo> invalidLicenses = getInvalidLicenses();

    private static class LicenseInfo {
        private String license;
        private State state;
        private LicenseContact contact;
    }

    private static LicenseContact getJohnSmith() {
        return new LicenseContact()
                .withFirst("John")
                .withLast("Smith")
                .withMiddle("Doe")
                .withDOB("03/11/2000", "MM/dd/yyyy");
    }

    private static List<LicenseInfo> getKnownLicenses() {
        List<LicenseInfo> licenses = new ArrayList<>();

        licenses.addAll(getUnitedStatesKnownLicenses());
        licenses.addAll(getCanadianKnownLicenses());

        return licenses;
    }

    private static List<LicenseInfo> getUnitedStatesKnownLicenses() {
        LicenseContact contact = getJohnSmith();
        List<LicenseInfo> licenses = new ArrayList<>();

        licenses.add(createLicenseInfo("9400310", State.ALABAMA, contact));
        licenses.add(createLicenseInfo("3541639", State.ALASKA, contact));
        licenses.add(createLicenseInfo("A45609381", State.ARIZONA, contact));
        licenses.add(createLicenseInfo("429416166", State.ARKANSAS, contact));
        licenses.add(createLicenseInfo("Q1897178", State.CALIFORNIA, contact));
        licenses.add(createLicenseInfo("928346236", State.COLORADO, contact));
        licenses.add(createLicenseInfo("157913164", State.CONNECTICUT, contact));
        licenses.add(createLicenseInfo("5751177", State.DISTRICT_OF_COLUMBIA, contact));
        licenses.add(createLicenseInfo("6075690", State.DELAWARE, contact));
        licenses.add(createLicenseInfo("S530464000910", State.FLORIDA, contact));
        licenses.add(createLicenseInfo("257434940", State.GEORGIA, contact));
        licenses.add(createLicenseInfo("575590630", State.HAWAII, contact));
        licenses.add(createLicenseInfo("RE959496Y", State.IDAHO, contact));
        licenses.add(createLicenseInfo("S53046400073", State.ILLINOIS, contact));
        licenses.add(createLicenseInfo("6564944345", State.INDIANA, contact));
        licenses.add(createLicenseInfo("482093063", State.IOWA, contact));
        licenses.add(createLicenseInfo("509217227", State.KANSAS, contact));
        licenses.add(createLicenseInfo("S18107686", State.KENTUCKY, contact));
        licenses.add(createLicenseInfo("002217317", State.LOUISIANA, contact));
        licenses.add(createLicenseInfo("5439138", State.MAINE, contact));
        licenses.add(createLicenseInfo("S530424117892", State.MARYLAND, contact));
        licenses.add(createLicenseInfo("021422437", State.MASSACHUSETTS, contact));
        licenses.add(createLicenseInfo("S530798122418", State.MICHIGAN, contact));
        licenses.add(createLicenseInfo("S530708452847", State.MINNESOTA, contact));
        licenses.add(createLicenseInfo("588059258", State.MISSISSIPPI, contact));
        licenses.add(createLicenseInfo("492190514", State.MISSOURI, contact));
        licenses.add(createLicenseInfo("517274526", State.MONTANA, contact));
        licenses.add(createLicenseInfo("A24763102", State.NEBRASKA, contact));
        licenses.add(createLicenseInfo("530192412000", State.NEVADA, contact));
        licenses.add(createLicenseInfo("17VOF50765", State.NEW_HAMPSHIRE, contact));
        licenses.add(createLicenseInfo("K37633998890175", State.NEW_JERSEY, contact));
        licenses.add(createLicenseInfo("234908935", State.NEW_MEXICO, contact));
        licenses.add(createLicenseInfo("639678705", State.NEW_YORK, contact));
        licenses.add(createLicenseInfo("575161877844", State.NORTH_CAROLINA, contact));
        licenses.add(createLicenseInfo("501196829", State.NORTH_DAKOTA, contact));
        licenses.add(createLicenseInfo("BG792378", State.OHIO, contact));
        licenses.add(createLicenseInfo("441211137", State.OKLAHOMA, contact));
        licenses.add(createLicenseInfo("4405052", State.OREGON, contact));
        licenses.add(createLicenseInfo("60689922", State.PENNSYLVANIA, contact));
        licenses.add(createLicenseInfo("7219014", State.RHODE_ISLAND, contact));
        licenses.add(createLicenseInfo("365796714", State.SOUTH_CAROLINA, contact));
        licenses.add(createLicenseInfo("503252585", State.SOUTH_DAKOTA, contact));
        licenses.add(createLicenseInfo("458625299", State.TENNESSEE, contact));
        licenses.add(createLicenseInfo("66171803", State.TEXAS, contact));
        licenses.add(createLicenseInfo("236686787", State.UTAH, contact));

        // Based on all the websites I read, it should be 8Numeric or 7Numeric+A
        // However, the site I generated these from has 9Numeric always.  I removed a digit to make valid.
        licenses.add(createLicenseInfo("58184767", State.VERMONT, contact));

        licenses.add(createLicenseInfo("229630614", State.VIRGINIA, contact));
        licenses.add(createLicenseInfo("SMITHJD656HE", State.WASHINGTON, contact));
        licenses.add(createLicenseInfo("1545474", State.WEST_VIRGINIA, contact));
        licenses.add(createLicenseInfo("S5304640009100", State.WISCONSIN, contact));
        licenses.add(createLicenseInfo("653697193", State.WYOMING, contact));

        return licenses;
    }

    private static List<LicenseInfo> getCanadianKnownLicenses() {
        LicenseContact contact = getJohnSmith();
        List<LicenseInfo> licenses = new ArrayList<>();

        licenses.add(createLicenseInfo("AA1234", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("A12345", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("123456", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("12345-678", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("1234576", State.BRITISH_COLUMBIA, contact));
        licenses.add(createLicenseInfo("ABCDEFG123H3", State.MANITOBA, contact));
        licenses.add(createLicenseInfo("AB12345678", State.NEWFOUNDLAND_AND_LABRADOR, contact));
        licenses.add(createLicenseInfo("S000311789", State.NEWFOUNDLAND_AND_LABRADOR, contact));
        licenses.add(createLicenseInfo("1234567", State.NEW_BRUNSWICK, contact));
        licenses.add(createLicenseInfo("123456", State.NORTHWEST_TERRITORIES, contact));
        licenses.add(createLicenseInfo("SMITH110300789", State.NOVA_SCOTIA, contact));
        licenses.add(createLicenseInfo("654321", State.NUNAVUT, contact));
        licenses.add(createLicenseInfo("S1234-56780-00311", State.ONTARIO, contact));
        licenses.add(createLicenseInfo("987611030054", State.PRINCE_EDWARD_ISLAND, contact));
        licenses.add(createLicenseInfo("S987611030054", State.QUEBEC, contact));
        licenses.add(createLicenseInfo("12345678", State.SASKATCHEWAN, contact));
        licenses.add(createLicenseInfo("654321", State.YUKON, contact));

        return licenses;
    }

    private static LicenseInfo createLicenseInfo(String license, State state, LicenseContact contact) {
        LicenseInfo info = new LicenseInfo();

        info.license = license;
        info.state = state;
        info.contact = contact;

        return info;
    }

    private static List<LicenseInfo> getInvalidLicenses() {
        LicenseContact contact = getJohnSmith();
        List<LicenseInfo> licenses = new ArrayList<>();

        licenses.add(createLicenseInfo("12AABBCC", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("12345A", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("123456-AB", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("123456-78", State.ALBERTA, contact));
        licenses.add(createLicenseInfo("123J567", State.BRITISH_COLUMBIA, contact));
        licenses.add(createLicenseInfo("ABCDEF1234H3", State.MANITOBA, contact));
        licenses.add(createLicenseInfo("ABCDEFGH23H3", State.MANITOBA, contact));
        licenses.add(createLicenseInfo("1BCDEFG123H3", State.MANITOBA, contact));
        licenses.add(createLicenseInfo("ABC2345678", State.NEWFOUNDLAND_AND_LABRADOR, contact));
        licenses.add(createLicenseInfo("S110003789", State.NEWFOUNDLAND_AND_LABRADOR, contact));
        licenses.add(createLicenseInfo("S001103789", State.NEWFOUNDLAND_AND_LABRADOR, contact));
        licenses.add(createLicenseInfo("S030011789", State.NEWFOUNDLAND_AND_LABRADOR, contact));
        licenses.add(createLicenseInfo("S123456", State.NEW_BRUNSWICK, contact));
        licenses.add(createLicenseInfo("ABCDEF", State.NORTHWEST_TERRITORIES, contact));
        licenses.add(createLicenseInfo("SMITJ110300789", State.NOVA_SCOTIA, contact));
        licenses.add(createLicenseInfo("JOHN110300789", State.NOVA_SCOTIA, contact));
        licenses.add(createLicenseInfo("SMITH031100789", State.NOVA_SCOTIA, contact));
        licenses.add(createLicenseInfo("SMITH001103789", State.NOVA_SCOTIA, contact));
        licenses.add(createLicenseInfo("54321", State.NUNAVUT, contact));
        licenses.add(createLicenseInfo("65321A", State.NUNAVUT, contact));
        licenses.add(createLicenseInfo("7654321", State.NUNAVUT, contact));
        licenses.add(createLicenseInfo("S1234-56780-01103", State.ONTARIO, contact));
        licenses.add(createLicenseInfo("S1234-56781-10300", State.ONTARIO, contact));
        licenses.add(createLicenseInfo("S1234-5AB80-00311", State.ONTARIO, contact));
        licenses.add(createLicenseInfo("J1234-56780-00311", State.ONTARIO, contact));
        licenses.add(createLicenseInfo("987A11030054", State.PRINCE_EDWARD_ISLAND, contact));
        licenses.add(createLicenseInfo("987611000354", State.PRINCE_EDWARD_ISLAND, contact));
        licenses.add(createLicenseInfo("987603110054", State.PRINCE_EDWARD_ISLAND, contact));
        licenses.add(createLicenseInfo("J987611030054", State.QUEBEC, contact));
        licenses.add(createLicenseInfo("S987603110054", State.QUEBEC, contact));
        licenses.add(createLicenseInfo("S987611000354", State.QUEBEC, contact));
        licenses.add(createLicenseInfo("1234567", State.SASKATCHEWAN, contact));
        licenses.add(createLicenseInfo("1234B567", State.SASKATCHEWAN, contact));
        licenses.add(createLicenseInfo("7654321", State.YUKON, contact));
        licenses.add(createLicenseInfo("54A321", State.YUKON, contact));

        return licenses;
    }

    private boolean isValidateLicense(LicenseInfo info) {
        return LicenseProvider.getInstance().isValid(info.license, info.state.getColumnName(), info.contact);
    }

    @Features("LicenseProvider")
    @Stories("License Validation")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void validateKnownLicensesTest() {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        String reason = "State (%s) had invalid license (%s)";
        for (LicenseInfo item : knownLicenses) {
            aggregator.assertThat(String.format(reason, item.state, item.license), isValidateLicense(item));
        }

        Helper.assertThat(aggregator);
    }

    @Features("LicenseProvider")
    @Stories("License Validation")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void validateInvalidLicensesTest() {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        String reason = "State (%s) had an unexpected valid license (%s)";
        for (LicenseInfo item : invalidLicenses) {
            aggregator.assertThat(String.format(reason, item.state, item.license), !isValidateLicense(item));
        }

        Helper.assertThat(aggregator);
    }

    @Features("LicenseProvider")
    @Stories("License Generating")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void nullLicenceGeneratedBasedOnInvalidStateTest() {
        String license = LicenseProvider.getInstance().get(null, null, contactJohnSmith);
        assertThat("Null State generates null license", license, nullValue());

        license = LicenseProvider.getInstance().get(null, "", contactJohnSmith);
        assertThat("Empty State generates null license", license, nullValue());

        license = LicenseProvider.getInstance().get(null, "INVALID", contactJohnSmith);
        assertThat("Invalid State generates null license", license, nullValue());
    }

    @Features("LicenseProvider")
    @Stories("License Generating")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void licenceGeneratedWithValidStateButProvidedLicenseInvalidTest() {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        String nullReason = "Null License generated for %s with invalid provided license";
        String nullGenerated = "Null License input generated an invalid license (%s) for %s";
        String emptyReason = "Empty License generated for %s with invalid provided license";
        String emptyGenerated = "Empty License input generated an invalid license (%s) for %s";
        String invalidReason = "Invalid License generated for %s with invalid provided license";
        String invalidGenerated = "Invalid License input generated an invalid license (%s) for %s";
        String invalidLicenseForAll = "A";
        String invalidLicenseReason = "The state (%s) generated license was the invalid license for all states";

        String license;
        LicenseInfo info;

        for (State state : State.values()) {
            license = LicenseProvider.getInstance().get(null, state.getColumnName(), contactJohnSmith);
            aggregator.assertThat(String.format(nullReason, state), license, notNullValue());

            info = new LicenseInfo();
            info.license = license;
            info.state = state;
            info.contact = contactJohnSmith;
            aggregator.assertThat(String.format(nullGenerated, license, state), isValidateLicense(info));

            license = LicenseProvider.getInstance().get("", state.getColumnName(), contactJohnSmith);
            aggregator.assertThat(String.format(emptyReason, state), license, notNullValue());

            info = new LicenseInfo();
            info.license = license;
            info.state = state;
            info.contact = contactJohnSmith;
            aggregator.assertThat(String.format(emptyGenerated, license, state), isValidateLicense(info));

            license = LicenseProvider.getInstance().get(invalidLicenseForAll, state.getColumnName(), contactJohnSmith);
            aggregator.assertThat(String.format(invalidReason, state), license, notNullValue());
            aggregator.assertThat(String.format(invalidLicenseReason, state), license, not(equalTo(invalidLicenseForAll)));

            info = new LicenseInfo();
            info.license = license;
            info.state = state;
            info.contact = contactJohnSmith;
            aggregator.assertThat(String.format(invalidGenerated, license, state), isValidateLicense(info));
        }

        Helper.assertThat(aggregator);
    }

    @Features("LicenseProvider")
    @Stories("License Generating")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void validateGeneratingLicensesProvidedLicenseValidTest() {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        String license;
        String reason = "State (%s) should have returned the valid license (%s) provided";
        for (LicenseInfo item : knownLicenses) {
            license = LicenseProvider.getInstance().get(item.license, item.state.getColumnName(), contactJohnSmith);
            aggregator.assertThat(String.format(reason, item.state, item.license), license, equalTo(item.license));
        }

        Helper.assertThat(aggregator);
    }

}
