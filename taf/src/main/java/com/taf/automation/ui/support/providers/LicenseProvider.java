package com.taf.automation.ui.support.providers;

import com.taf.automation.ui.support.Rand;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@XStreamAlias("license-provider")
public class LicenseProvider {
    private static final String SIX_NUMBERS = "^\\d{6}$";
    private static final String SEVEN_NUMBERS = "^\\d{7}$";
    private static final String EIGHT_NUMBERS = "^\\d{8}$";
    private static final String NINE_NUMBERS = "^\\d{9}$";
    private static final String ANY_FOUR_NUMBERS = "\\d{4}";

    private static final String AB_2A_4N = "^[a-zA-Z]{2}\\d{4}$";
    private static final String AB_1A_5N = "^[a-zA-Z]\\d{5}$";
    private static final String AB_5N_DASH_3N = "^\\d{5}-\\d{3}$";
    private static final String MB_7A_3N_2B = "^[a-zA-Z]{7}\\d{3}[a-zA-Z0-9]{2}$";
    private static final String NL_2A_8N = "^[a-zA-Z]{2}\\d{8}$";

    private static final String AL_1TO8N = "^\\d{1,8}$";
    private static final String AK_DE_1TO7N = "^\\d{1,7}$";
    private static final String AZ_HI_KS_KY_MA_MT_1A_8N = "^[a-zA-Z]\\d{8}$";
    private static final String AR_4TO9N = "^\\d{4,9}$";
    private static final String CA_NE_NY_1A_7N = "^[a-zA-Z]\\d{7}$";
    private static final String CO_1A_3TO6N = "^[a-zA-Z]\\d{3,6}$";
    private static final String CO_2A_2TO5N = "^[a-zA-Z]{2}\\d{2,5}$";
    private static final String FL_1A_12N = "^[a-zA-Z]\\d{12}$";
    private static final String ID_2A_6N_1A = "^[a-zA-Z]{2}\\d{6}[a-zA-Z]$";
    private static final String IL_1A_11TO12N = "^[a-zA-Z]\\d{11,12}$";
    private static final String IN_OK_KY_1A_9N = "^[a-zA-Z]\\d{9}$";
    private static final String IN_NV_10N = "^\\d{10}$";
    private static final String IA_3N_2A_4N = "^\\d{3}[a-zA-Z]{2}\\d{4}$";
    private static final String KS_1A_1N_1A_1N_1A = "^[a-zA-Z]\\d[a-zA-Z]\\d[a-zA-Z]$";
    private static final String LA_1TO9N = "^\\d{1,9}$";
    private static final String ME_7N_1A = "^\\d{7}[a-zA-Z]$";
    private static final String MD_MI_MN_1A_12N = "^[a-zA-Z]\\d{12}$";
    private static final String MI_1A_10N = "^[a-zA-Z]\\d{10}$";
    private static final String MO_1A_5TO9N = "^[a-zA-Z]\\d{5,9}$";
    private static final String MO_1A_6N_R = "^[a-zA-Z]\\d{6}R$";
    private static final String MO_8N_2A = "^\\d{8}[a-zA-Z]{2}$";
    private static final String MO_9N_1A = "^\\d{9}[a-zA-Z]$";
    private static final String MT_13TO14N = "^\\d{13,14}$";
    private static final String NE_1A_6TO8N = "^[a-zA-Z]\\d{6,8}$";
    private static final String NV_SD_12N = "^\\d{12}$";
    private static final String NV_X_8N = "^X\\d{8}$";
    private static final String NH_2N_3A_5N = "^\\d{2}[a-zA-Z]{3}\\d{5}$";
    private static final String NJ_1A_14N = "^[a-zA-Z]\\d{14}$";
    private static final String NY_1A_18N = "^[a-zA-Z]\\d{18}$";
    private static final String NY_16N = "^\\d{16}$";
    private static final String NY_8A = "^[a-zA-Z]{8}$";
    private static final String NC_1TO12N = "^\\d{1,12}$";
    private static final String ND_3A_6N = "^[a-zA-Z]{3}\\d{6}$";
    private static final String OH_1A_4TO8N = "^[a-zA-Z]\\d{4,8}$";
    private static final String OH_2A_3TO7N = "^[a-zA-Z]{2}\\d{3,7}$";
    private static final String OR_1TO9N = "^\\d{1,9}$";
    private static final String RI_1A_6N = "^[a-zA-Z]\\d{6}$";
    private static final String SC_5TO11N = "^\\d{5,11}$";
    private static final String SD_6TO10N = "^\\d{6,10}$";
    private static final String UT_4TO10N = "^\\d{4,10}$";
    private static final String VT_7N_A = "^\\d{7}A$";
    private static final String VA_1A_8TO11N = "^[a-zA-Z]\\d{8,11}$";
    private static final String WV_1TO2A_5TO6N = "^[a-zA-Z]{1,2}\\d{5,6}$";
    private static final String WI_1A_13N = "^[a-zA-Z]\\d{13}$";
    private static final String WY_9TO10N = "^\\d{9,10}$";

    private LicenseProvider() {
        //
    }

    private static class LazyHolder {
        private static final LicenseProvider INSTANCE = new LicenseProvider();
    }

    public static LicenseProvider getInstance() {
        return LicenseProvider.LazyHolder.INSTANCE;
    }

    /**
     * Basic validation<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>This method should not be used definitively say the license is valid</LI>
     * <LI>This method does some very basic validation of the license based on the state</LI>
     * </OL>
     *
     * @param license        - License to do basic validation for
     * @param licenseState   - License State
     * @param licenseContact - License Contact information that may be used in validation
     * @return true means the license is not completely invalid, false means the license if definitely invalid
     */
    public boolean isValid(String license, String licenseState, LicenseContact licenseContact) {
        if (StringUtils.isBlank(license)) {
            return false;
        }

        if (isWashington(licenseState)) {
            return isValidateWashingtonLicense(license, licenseContact);
        }

        State state = (State) State.ALABAMA.toEnum(licenseState);
        if (state == null) {
            return false;
        }

        if (shouldStartWithLastInitial(state)) {
            return isValidateStartWithLastInitial(state, license, licenseContact);
        }

        if (isCanadian(state)) {
            return isValidateCanadianLicense(state, license, licenseContact);
        }

        if (isOtherUnitedStates(state)) {
            return isValidateOtherUnitedStatesLicense(state, license, licenseContact);
        }

        // No (useful) rules to apply to the license
        return true;
    }

    /**
     * Get a generated license
     *
     * @param state - State to generate license for
     * @return valid license for the state
     */
    public String get(String state) {
        return get(state, new LicenseContact());
    }

    /**
     * Get a generated license that should be considered valid
     *
     * @param state   - State to generate license for
     * @param contact - Contact Information to be used in generation if necessary
     * @return license should be valid for the state
     */
    public String get(String state, LicenseContact contact) {
        State theState = (State) State.ALABAMA.toEnum(state);
        return get(getBase(theState, contact), state, contact);
    }

    /**
     * Return a validate license which is either the specified license or a slightly modified license
     *
     * @param license - License for validation
     * @param state   - State for validation
     * @return specified license or slightly modified to attempt to make valid
     */
    public String get(String license, String state) {
        return get(license, state, new LicenseContact());
    }

    /**
     * Return a validate license which is either the specified license or a slightly modified license
     *
     * @param license - License for validation
     * @param state   - State for validation
     * @param contact - Contact Information to be used in validation/generation if necessary
     * @return specified license or slightly modified to attempt to make valid
     */
    public String get(String license, String state, LicenseContact contact) {
        if (isWashington(state)) {
            return applyWashingtonValidations(license, contact);
        }

        State theState = (State) State.ALABAMA.toEnum(state);
        if (shouldStartWithLastInitial(theState)) {
            return applyStartsWithLastInitialValidations(theState, license, contact);
        }

        if (isCanadian(theState)) {
            return applyCanadianValidations(theState, license, contact);
        }

        if (isOtherUnitedStates(theState)) {
            return applyOtherUnitedStatesValidations(theState, license, contact);
        }

        // No (useful) rules to apply to the license
        return license;
    }

    private boolean shouldStartWithLastInitial(State state) {
        return state == State.FLORIDA
                || state == State.ILLINOIS
                || state == State.KENTUCKY
                || state == State.MARYLAND
                || state == State.MICHIGAN
                || state == State.MINNESOTA
                || state == State.WISCONSIN
                ;
    }

    /**
     * Is it a Washington license
     *
     * @param state - State
     * @return true if state is Washington else false
     */
    private boolean isWashington(String state) {
        return StringUtils.equalsIgnoreCase(State.WASHINGTON.getColumnName(), state);
    }

    /**
     * Is it a Canadian License
     *
     * @param state - State
     * @return true if province/territory of Canada else false
     */
    private boolean isCanadian(State state) {
        return state == State.ALBERTA
                || state == State.BRITISH_COLUMBIA
                || state == State.MANITOBA
                || state == State.NEW_BRUNSWICK
                || state == State.NEWFOUNDLAND_AND_LABRADOR
                || state == State.NOVA_SCOTIA
                || state == State.ONTARIO
                || state == State.PRINCE_EDWARD_ISLAND
                || state == State.QUEBEC
                || state == State.SASKATCHEWAN
                || state == State.NORTHWEST_TERRITORIES
                || state == State.NUNAVUT
                || state == State.YUKON
                ;
    }

    /**
     * Determine if a validate Washington license
     *
     * @param license - License to validate
     * @param contact - Contact Information for the validation
     * @return true if basic validation is successful else false
     */
    private boolean isValidateWashingtonLicense(String license, LicenseContact contact) {
        if (StringUtils.isBlank(contact.getFirst()) || StringUtils.isBlank(contact.getLast())) {
            return false;
        }

        return StringUtils.startsWith(license, generateStartOfWashingtonLicense(contact));
    }

    /**
     * Generate a validate start of a Washington License
     *
     * @param contact - Contact Information used to generate the start of the validate license
     * @return Validate start of a Washington License (assuming the contact information was valid)
     */
    private String generateStartOfWashingtonLicense(LicenseContact contact) {
        // License must start with the first 5 char of the last name
        String workingLast = StringUtils.substring(StringUtils.defaultString(contact.getLast()), 0, 5);

        // If last name is less than 5 char, then it is right padded with star
        String last = StringUtils.rightPad(workingLast, 5, "*").toUpperCase();

        // Next piece of license is the first initial of the first name
        String first = StringUtils.substring(StringUtils.defaultString(contact.getFirst()), 0, 1).toUpperCase();

        // Next piece of license is the first initial of the middle name if exists
        String middle = "";
        if (StringUtils.isNotBlank(contact.getMiddle())) {
            middle = StringUtils.substring(StringUtils.defaultString(contact.getMiddle()), 0, 1).toUpperCase();
        }

        return last + first + middle;
    }

    /**
     * Determines if the license is valid in which case the specified license is returned,
     * else attempt to make the license valid
     *
     * @param license - License to validate
     * @param contact - Contact Information for validation
     * @return specified license or a modified license in an attempt to make valid
     */
    private String applyWashingtonValidations(String license, LicenseContact contact) {
        String startPrefix = generateStartOfWashingtonLicense(contact);
        if (StringUtils.startsWith(license, startPrefix)) {
            return license;
        } else {
            // Attempt to make license valid
            return startPrefix + StringUtils.substring(license, startPrefix.length());
        }
    }

    private boolean isStartsWithLastInitial(String license, LicenseContact contact) {
        if (StringUtils.isBlank(contact.getLast())) {
            return false;
        }

        return StringUtils.startsWith(license, generateStartsWithLastInitialLicense(contact));
    }

    private String generateStartsWithLastInitialLicense(LicenseContact contact) {
        return StringUtils.substring(StringUtils.defaultString(contact.getLast()), 0, 1).toUpperCase();
    }

    private String applyStartsWithLastInitialValidations(State state, String license, LicenseContact contact) {
        if (isValidateStartWithLastInitial(state, license, contact)) {
            return license;
        }

        String startPrefix = generateStartsWithLastInitialLicense(contact);
        String randomLicense = getBase(state, contact);
        return startPrefix + StringUtils.substring(randomLicense, startPrefix.length());
    }

    private boolean isValidateCanadianLicense(State state, String license, LicenseContact contact) {
        if (StringUtils.isBlank(license)) {
            return false;
        } else if (state == State.ALBERTA) {
            return isValidateAlbertaLicense(license, contact);
        } else if (state == State.BRITISH_COLUMBIA) {
            return isValidateBritishColumbiaLicense(license, contact);
        } else if (state == State.MANITOBA) {
            return isValidateManitobaLicense(license, contact);
        } else if (state == State.NEW_BRUNSWICK) {
            return isValidateNewBrunswickLicense(license, contact);
        } else if (state == State.NEWFOUNDLAND_AND_LABRADOR) {
            return isValidateNewfoundlandAndLabradorLicense(license, contact);
        } else if (state == State.NOVA_SCOTIA) {
            return isValidateNovaScotiaLicense(license, contact);
        } else if (state == State.ONTARIO) {
            return isValidateOntarioLicense(license, contact);
        } else if (state == State.PRINCE_EDWARD_ISLAND) {
            return isValidatePrinceEdwardIslandLicense(license, contact);
        } else if (state == State.QUEBEC) {
            return isValidateQuebecLicense(license, contact);
        } else if (state == State.SASKATCHEWAN) {
            return isValidateSaskatchewanLicense(license, contact);
        } else if (state == State.NORTHWEST_TERRITORIES) {
            return isValidateNorthwestTerritoriesLicense(license, contact);
        } else if (state == State.NUNAVUT) {
            return isValidateNunavutLicense(license, contact);
        } else if (state == State.YUKON) {
            return isValidateYukonLicense(license, contact);
        } else {
            return false;
        }
    }

    /**
     * Determine if Validate Alberta License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * AA#### 2 letters followed by 4 numbers
     * </LI>
     * <LI>
     * A##### 1 letter followed by 5 numbers
     * </LI>
     * <LI>
     * ###### 6 numbers
     * </LI>
     * <LI>
     * #####-### 5 numbers, dash 3 numbers
     * </LI>
     * <DIV>
     * #= any numbers
     * </DIV>
     * <DIV>
     * A= any letters
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for Alberta else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateAlbertaLicense(String license, LicenseContact contact) {
        return license.matches(AB_2A_4N)
                || license.matches(AB_1A_5N)
                || license.matches(SIX_NUMBERS)
                || license.matches(AB_5N_DASH_3N)
                ;
    }

    /**
     * Determine if Validate British Columbia License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * ####### 7 numbers
     * </LI>
     * <DIV>
     * #= any numbers
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for British Columbia else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateBritishColumbiaLicense(String license, LicenseContact contact) {
        return license.matches(SEVEN_NUMBERS);
    }

    /**
     * Determine if Validate Manitoba License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * AAAAAAA###BB 7 letters followed by 3 numbers, followed by 2 numbers or letters
     * </LI>
     * <DIV>
     * A= any letters
     * </DIV>
     * <DIV>
     * #= any numbers
     * </DIV>
     * <DIV>
     * B= numbers or letters
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for Manitoba else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateManitobaLicense(String license, LicenseContact contact) {
        return license.matches(MB_7A_3N_2B);
    }

    /**
     * Determine if Validate Newfoundland And Labrador License<BR>
     * <B>Formats:</B>
     * <UL>
     * <LI>
     * AYYMMDD### First letter of your last name, the last 2 numbers of your birth year,
     * 2 digits of your birth month, 2 digits of your birth day followed by 3 numbers.
     * <DIV></DIV>
     * <DIV>
     * A= first letter of last name
     * </DIV>
     * <DIV>
     * Y= the last two numbers of birth year
     * </DIV>
     * <DIV>M= month of birth year</DIV>
     * <DIV>D= day of birth year</DIV>
     * <DIV>#= any numbers</DIV>
     * </LI>
     * <LI>
     * AA######## 2 letters followed by 8 numbers.
     * <DIV></DIV>
     * <DIV>
     * A= any letters
     * </DIV>
     * <DIV>
     * #= any numbers
     * </DIV>
     * </LI>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Contact information to validate license if necessary
     * @return true if license is valid for Newfoundland And Labrador else false
     */
    private boolean isValidateNewfoundlandAndLabradorLicense(String license, LicenseContact contact) {
        // AA######## 2 letters followed by 8 numbers
        //
        // A= any letters
        // #= any numbers
        if (license.matches(NL_2A_8N)) {
            return true;
        }

        // AYYMMDD### First letter of your last name, the last 2 numbers of your birth year,
        // 2 digits of your birth month, 2 digits of your birth day followed by 3 numbers
        //
        // A= first letter of last name
        // Y= the last two numbers of birth year
        // M= month of birth year
        // D= day of birth year
        // #= any numbers
        //

        String firstLetterOfLastName = StringUtils.substring(contact.getLast(), 0, 1);
        if (StringUtils.isBlank(firstLetterOfLastName)) {
            return false;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return false;
        }

        //
        // Construct the regular expression
        //
        String lower = firstLetterOfLastName.toLowerCase();
        String upper = firstLetterOfLastName.toUpperCase();
        firstLetterOfLastName = "[" + lower + upper + "]";

        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String theDOB = last2digitsOfYear + last2digitsOfMonth + last2digitsOfDay;

        String endsWithNumbers = "\\d{3}";

        return license.matches("^" + firstLetterOfLastName + theDOB + endsWithNumbers + "$");
    }

    /**
     * Determine if Validate New Brunswick License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * ####### 7 numbers
     * </LI>
     * <DIV>
     * #= any numbers
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for New Brunswick else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateNewBrunswickLicense(String license, LicenseContact contact) {
        return license.matches(SEVEN_NUMBERS);
    }

    /**
     * Determine if Validate Northwest Territories License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * ###### 6 numbers
     * </LI>
     * <DIV>
     * #= any numbers
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for Northwest Territories else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateNorthwestTerritoriesLicense(String license, LicenseContact contact) {
        return license.matches(SIX_NUMBERS);
    }

    /**
     * Determine if Validate Nova Scotia License<BR>
     * <B>Formats:</B>
     * <UL>
     * <LI>
     * AAAAADDMMYY### first 5 letters of your last name, 2 digits of your birth day,
     * 2 digits of your birth month, the last 2 digits of your birth year, followed by 3 numbers
     * <DIV></DIV>
     * <DIV>
     * A= first 5 letters of last name
     * </DIV>
     * <DIV>
     * D= day of birth year
     * </DIV>
     * <DIV>
     * M= month of birth year
     * </DIV>
     * <DIV>
     * Y= the last two numbers of birth year
     * </DIV>
     * <DIV>
     * #= any numbers
     * </DIV>
     * <DIV>
     * <B>Note:</B> If your last name is less than 5 letters, then these characters need to be entered as spaces
     * </DIV>
     * </LI>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Contact information to validate license
     * @return true if license is valid for Nova Scotia else false
     */
    private boolean isValidateNovaScotiaLicense(String license, LicenseContact contact) {
        String first5LettersOfLastName = StringUtils.substring(contact.getLast(), 0, 5);
        if (StringUtils.isBlank(first5LettersOfLastName)) {
            return false;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return false;
        }

        //
        // Construct the regular expression
        //

        // The statement about spaces is ambiguous.  So, I am going to allow leading or trailing spaces up to 5
        // based on the length of the last name.  However, I am not allowing spaces in the middle
        int maxLeadTrailSpaces = 5 - first5LettersOfLastName.length();
        String allowedSpaces = "[ ]{0," + maxLeadTrailSpaces + "}";
        first5LettersOfLastName = "((?i)" + allowedSpaces + first5LettersOfLastName + allowedSpaces + ")";

        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String theDOB = last2digitsOfDay + last2digitsOfMonth + last2digitsOfYear;

        String endsWithNumbers = "\\d{3}";

        return license.matches("^" + first5LettersOfLastName + theDOB + endsWithNumbers + "$");
    }

    /**
     * Determine if Validate Nunavut License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * ###### 6 numbers
     * </LI>
     * <DIV>
     * #= any numbers
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for Nunavut else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateNunavutLicense(String license, LicenseContact contact) {
        return license.matches(SIX_NUMBERS);
    }

    /**
     * Determine if Validate Ontario License<BR>
     * <B>Formats:</B>
     * <UL>
     * <LI>
     * A####-####Y-YMMDD the first letter of your last name, any 4 numbers, dash, any 4 numbers,
     * the last 2 digits of your birth year separated by a dash, 2 digits of your birth month,
     * 2 digits of your birth day
     * <DIV></DIV>
     * <DIV>
     * A= the first letter of your last name
     * </DIV>
     * <DIV>
     * #= any numbers
     * </DIV>
     * <DIV>
     * Y= the last two numbers of birth year
     * </DIV>
     * <DIV>
     * M= month of birth year
     * </DIV>
     * <DIV>
     * D= day of birth year
     * </DIV>
     * </LI>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Contact information to validate license
     * @return true if license is valid for Ontario else false
     */
    private boolean isValidateOntarioLicense(String license, LicenseContact contact) {
        String firstLetterOfLastName = StringUtils.substring(contact.getLast(), 0, 1);
        if (StringUtils.isBlank(firstLetterOfLastName)) {
            return false;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return false;
        }

        //
        // Construct the regular expression
        //
        String lower = firstLetterOfLastName.toLowerCase();
        String upper = firstLetterOfLastName.toUpperCase();
        firstLetterOfLastName = "[" + lower + upper + "]";

        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String firstYearDigit = StringUtils.left(last2digitsOfYear, 1);
        String secondYearDigit = StringUtils.right(last2digitsOfYear, 1);
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String theDOB = firstYearDigit + "-" + secondYearDigit + last2digitsOfMonth + last2digitsOfDay;

        return license.matches("^" + firstLetterOfLastName + ANY_FOUR_NUMBERS + "-" + ANY_FOUR_NUMBERS + theDOB + "$");
    }

    /**
     * Determine if Validate Prince Edward Island License<BR>
     * <B>Formats:</B>
     * <UL>
     * <LI>
     * ####DDMMYY## 4 numbers, 2 digit day of birth, 2 digit month of birth, last 2 digits of birth year,
     * followed by 2 numbers
     * <DIV></DIV>
     * <DIV>
     * #= any numbers
     * </DIV>
     * <DIV>
     * D= day of birth year
     * </DIV>
     * <DIV>
     * M= month of birth year
     * </DIV>
     * <DIV>
     * Y= the last two numbers of birth year
     * </DIV>
     * </LI>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Contact information to validate license
     * @return true if license is valid for Prince Edward Island else false
     */
    private boolean isValidatePrinceEdwardIslandLicense(String license, LicenseContact contact) {
        // ####DDMMYY## 4 numbers, 2 digit day of birth, 2 digit month of birth, last 2 digits of birth year,
        // followed by 2 numbers
        //
        // #= any numbers
        // D= day of birth year
        // M= month of birth year
        // Y= the last two numbers of birth year

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return false;
        }

        //
        // Construct the regular expression
        //
        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String theDOB = last2digitsOfDay + last2digitsOfMonth + last2digitsOfYear;

        String endsWithNumbers = "\\d{2}";

        return license.matches("^" + ANY_FOUR_NUMBERS + theDOB + endsWithNumbers + "$");
    }

    /**
     * Determine if Validate Quebec License<BR>
     * <B>Formats:</B>
     * <UL>
     * <LI>
     * A####DDMMYY##  first letter of your last name, 4 numbers, 2 digits of your birth day,
     * 2 digits of your birth month, last 2 digits of your birth year, followed by 2 numbers
     * <DIV></DIV>
     * <DIV>
     * A= the first letter of your last name
     * </DIV>
     * <DIV>
     * #= any numbers
     * </DIV>
     * <DIV>
     * D= day of birth year
     * </DIV>
     * <DIV>
     * M= month of birth year
     * </DIV>
     * <DIV>
     * Y= the last two numbers of birth year
     * </DIV>
     * </LI>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Contact information to validate license
     * @return true if license is valid for Quebec else false
     */
    private boolean isValidateQuebecLicense(String license, LicenseContact contact) {
        String firstLetterOfLastName = StringUtils.substring(contact.getLast(), 0, 1);
        if (StringUtils.isBlank(firstLetterOfLastName)) {
            return false;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return false;
        }

        //
        // Construct the regular expression
        //
        String lower = firstLetterOfLastName.toLowerCase();
        String upper = firstLetterOfLastName.toUpperCase();
        firstLetterOfLastName = "[" + lower + upper + "]";

        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String theDOB = last2digitsOfDay + last2digitsOfMonth + last2digitsOfYear;

        String endsWithNumbers = "\\d{2}";

        return license.matches("^" + firstLetterOfLastName + ANY_FOUR_NUMBERS + theDOB + endsWithNumbers + "$");
    }

    /**
     * Determine if Validate Saskatchewan License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * ########  8 numbers
     * </LI>
     * <DIV>
     * #= any numbers
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for Saskatchewan else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateSaskatchewanLicense(String license, LicenseContact contact) {
        return license.matches(EIGHT_NUMBERS);
    }

    /**
     * Determine if Validate Yukon License<BR>
     * <B>Format Rules:</B>
     * <UL>
     * <LI>
     * ###### 6 numbers
     * </LI>
     * <DIV>
     * #= any numbers
     * </DIV>
     * </UL>
     *
     * @param license - License to validate
     * @param contact - Not Used reserved for future use
     * @return true if license is valid for Yukon else false
     */
    @SuppressWarnings("java:S1172")
    private boolean isValidateYukonLicense(String license, LicenseContact contact) {
        return license.matches(SIX_NUMBERS);
    }

    /**
     * Generate Canadian License
     *
     * @param state   - Province/Territory to generate license
     * @param contact - Contact information to generate license
     * @return null if cannot generate else license
     */
    @SuppressWarnings("java:S112")
    private String generateCanadianLicense(State state, LicenseContact contact) {
        if (state == State.ALBERTA) {
            return generateAlbertaLicense(contact);
        } else if (state == State.BRITISH_COLUMBIA) {
            return generateBritishColumbiaLicense(contact);
        } else if (state == State.MANITOBA) {
            return generateManitobaLicense(contact);
        } else if (state == State.NEW_BRUNSWICK) {
            return generateNewBrunswickLicense(contact);
        } else if (state == State.NEWFOUNDLAND_AND_LABRADOR) {
            return generateNewfoundlandAndLabradorLicense(contact);
        } else if (state == State.NOVA_SCOTIA) {
            return generateNovaScotiaLicense(contact);
        } else if (state == State.ONTARIO) {
            return generateOntarioLicense(contact);
        } else if (state == State.PRINCE_EDWARD_ISLAND) {
            return generatePrinceEdwardIslandLicense(contact);
        } else if (state == State.QUEBEC) {
            return generateQuebecLicense(contact);
        } else if (state == State.SASKATCHEWAN) {
            return generateSaskatchewanLicense(contact);
        } else if (state == State.NORTHWEST_TERRITORIES) {
            return generateNorthwestTerritoriesLicense(contact);
        } else if (state == State.NUNAVUT) {
            return generateNunavutLicense(contact);
        } else if (state == State.YUKON) {
            return generateYukonLicense(contact);
        } else {
            throw new RuntimeException("Base License Generation is not supported for province:  " + state);
        }
    }

    /**
     * Generate Alberta License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateAlbertaLicense(LicenseContact contact) {
        // AA#### 2 letters followed by 4 numbers OR
        // A##### 1 letter followed by 5 numbers OR
        // ###### 6 numbers OR
        // #####-### 5 numbers, dash 3 numbers
        //
        // #= any numbers
        // A= any letters
        String format2A4N = Rand.letters(2).toUpperCase() + Rand.numbers(4);
        String format1A5N = Rand.letters(1).toUpperCase() + Rand.numbers(5);
        String format6N = Rand.numbers(6);
        String format5Ndash3N = Rand.numbers(5) + "-" + Rand.numbers(3);

        List<String> formats = Arrays.asList(format2A4N, format1A5N, format6N, format5Ndash3N);
        Collections.shuffle(formats);
        return formats.get(0);
    }

    /**
     * Generate British Columbia License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateBritishColumbiaLicense(LicenseContact contact) {
        // ####### 7 numbers
        //
        // #= any numbers
        return Rand.numbers(7);
    }

    /**
     * Generate Manitoba License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateManitobaLicense(LicenseContact contact) {
        // AAAAAAA###BB 7 letters followed by 3 numbers, followed by 2 numbers or letters
        //
        // A= any letters
        // #= any numbers
        // B= numbers or letters
        return Rand.letters(7).toUpperCase() + Rand.numbers(3) + Rand.alphanumeric(2).toUpperCase();
    }

    /**
     * Generate Newfoundland And Labrador License
     *
     * @param contact - Contact information to generate license
     * @return null if cannot generate else license
     */
    private String generateNewfoundlandAndLabradorLicense(LicenseContact contact) {
        String firstLetterOfLastName = StringUtils.substring(contact.getLast(), 0, 1);
        if (StringUtils.isBlank(firstLetterOfLastName)) {
            return null;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return null;
        }

        // AYYMMDD### First letter of your last name, the last 2 numbers of your birth year,
        // 2 digits of your birth month, 2 digits of your birth day followed by 3 numbers
        //
        // A= first letter of last name
        // Y= the last two numbers of birth year
        // M= month of birth year
        // D= day of birth year
        // #= any numbers
        //
        // OR
        // AA######## 2 letters followed by 8 numbers
        //
        // A= any letters
        // #= any numbers

        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String theDOB = last2digitsOfYear + last2digitsOfMonth + last2digitsOfDay;

        String formatAYYMMDD3N = firstLetterOfLastName.toUpperCase() + theDOB + Rand.numbers(3);
        String format2A8N = Rand.letters(2).toUpperCase() + Rand.numbers(8);

        List<String> formats = Arrays.asList(formatAYYMMDD3N, format2A8N);
        Collections.shuffle(formats);
        return formats.get(0);
    }

    /**
     * Generate New Brunswick License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateNewBrunswickLicense(LicenseContact contact) {
        // ####### 7 numbers
        //
        // #= any numbers
        return Rand.numbers(7);
    }

    /**
     * Generate Northwest Territories License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateNorthwestTerritoriesLicense(LicenseContact contact) {
        // ###### 6 numbers
        //
        // #= any numbers
        return Rand.numbers(6);
    }

    /**
     * Generate Nova Scotia License
     *
     * @param contact - Contact information to generate license
     * @return null if cannot generate else license
     */
    private String generateNovaScotiaLicense(LicenseContact contact) {
        String first5LettersOfLastName = StringUtils.substring(contact.getLast(), 0, 5);
        if (StringUtils.isBlank(first5LettersOfLastName)) {
            return null;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return null;
        }

        // AAAAADDMMYY### first 5 letters of your last name, 2 digits of your birth day,
        // 2 digits of your birth month, the last 2 digits of your birth year, followed by 3 numbers
        //
        // A= first 5 letters of last name
        // D= day of birth year
        // M= month of birth year
        // Y= the last two numbers of birth year
        // #= any numbers
        // Please note: If your last name is less than 5 letters,
        // then these characters need to be entered as spaces on sonnet.ca

        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String theDOB = last2digitsOfDay + last2digitsOfMonth + last2digitsOfYear;

        return first5LettersOfLastName.toUpperCase() + theDOB + Rand.numbers(3);
    }

    /**
     * Generate Nunavut License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateNunavutLicense(LicenseContact contact) {
        // ###### 6 numbers
        //
        // #= any numbers
        return Rand.numbers(6);
    }

    /**
     * Generate Ontario License
     *
     * @param contact - Contact information to generate license
     * @return null if cannot generate else license
     */
    private String generateOntarioLicense(LicenseContact contact) {
        String firstLetterOfLastName = StringUtils.substring(contact.getLast(), 0, 1);
        if (StringUtils.isBlank(firstLetterOfLastName)) {
            return null;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return null;
        }

        // A####-####Y-YMMDD the first letter of your last name, any 4 numbers, dash, any 4 numbers,
        // the last 2 digits of your birth year separated by a dash, 2 digits of your birth month,
        // 2 digits of your birth day
        //
        // A= the first letter of your last name
        // #= any numbers
        // Y= the last two numbers of birth year
        // M= month of birth year
        // D= day of birth year
        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String firstYearDigit = StringUtils.left(last2digitsOfYear, 1);
        String secondYearDigit = StringUtils.right(last2digitsOfYear, 1);
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String theDOB = firstYearDigit + "-" + secondYearDigit + last2digitsOfMonth + last2digitsOfDay;

        return firstLetterOfLastName.toUpperCase() + Rand.numbers(4) + "-" + Rand.numbers(4) + theDOB;
    }

    /**
     * Generate Prince Edward Island License
     *
     * @param contact - Contact information to generate license
     * @return null if cannot generate else license
     */
    private String generatePrinceEdwardIslandLicense(LicenseContact contact) {
        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return null;
        }

        // ####DDMMYY## 4 numbers, 2 digit day of birth, 2 digit month of birth, last 2 digits of birth year,
        // followed by 2 numbers
        //
        // #= any numbers
        // D= day of birth year
        // M= month of birth year
        // Y= the last two numbers of birth year
        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String theDOB = last2digitsOfDay + last2digitsOfMonth + last2digitsOfYear;

        return Rand.numbers(4) + theDOB + Rand.numbers(2);
    }

    /**
     * Generate Quebec License
     *
     * @param contact - Contact information to generate license
     * @return null if cannot generate else license
     */
    private String generateQuebecLicense(LicenseContact contact) {
        String firstLetterOfLastName = StringUtils.substring(contact.getLast(), 0, 1);
        if (StringUtils.isBlank(firstLetterOfLastName)) {
            return null;
        }

        LocalDate dob;
        try {
            dob = contact.getDob();
        } catch (Exception ex) {
            dob = null;
        }

        if (dob == null) {
            return null;
        }

        // A####DDMMYY##  first letter of your last name, 4 numbers, 2 digits of your birth day,
        // 2 digits of your birth month, last 2 digits of your birth year, followed by 2 numbers
        //
        // A= the first letter of your last name
        // #= any numbers
        // D= day of birth year
        // M= month of birth year
        // Y= the last two numbers of birth year
        String last2digitsOfDay = StringUtils.leftPad(String.valueOf(dob.getDayOfMonth()), 2, "0");
        String last2digitsOfMonth = StringUtils.leftPad(String.valueOf(dob.getMonthValue()), 2, "0");
        String last2digitsOfYear = StringUtils.right(String.valueOf(dob.getYear()), 2);
        String theDOB = last2digitsOfDay + last2digitsOfMonth + last2digitsOfYear;

        return firstLetterOfLastName.toUpperCase() + Rand.numbers(4) + theDOB + Rand.numbers(2);
    }

    /**
     * Generate Saskatchewan License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateSaskatchewanLicense(LicenseContact contact) {
        // ########  8 numbers
        //
        // #= any numbers
        return Rand.numbers(8);
    }

    /**
     * Generate Yukon License
     *
     * @param contact - Not Used reserved for future use
     * @return String
     */
    @SuppressWarnings("java:S1172")
    private String generateYukonLicense(LicenseContact contact) {
        // ###### 6 numbers
        //
        // #= any numbers
        return Rand.numbers(6);
    }

    /**
     * Determines if the license is valid in which case the specified license is returned,
     * else attempt to make the license valid
     *
     * @param state   - Province/Territory for validation rules
     * @param license - License to validate
     * @param contact - Contact Information for validation
     * @return specified license or a random license for the province/territory
     */
    private String applyCanadianValidations(State state, String license, LicenseContact contact) {
        if (isValidateCanadianLicense(state, license, contact)) {
            return license;
        }

        String randomLicense = generateCanadianLicense(state, contact);
        return (randomLicense != null) ? randomLicense : license;
    }

    /**
     * Generate a base license for the state
     *
     * @param state - State to generate license
     * @return license with correct size and format based on information from
     * <a href="https://ntsi.com/drivers-license-format/">here</a> for a US state <BR> or from
     * <a href="https://www.sonnet.ca/FAQ/Quoting/What-is-the-correct-way-to-enter-my-drivers-licence-information">here</a>
     * for a Canadian Province/Territory
     */
    public String getBase(State state) {
        return getBase(state, new LicenseContact());
    }

    /**
     * Generate a base license for the state
     *
     * @param state   - State to generate license
     * @param contact - Contact Information to use if necessary to generate the license
     * @return license with correct size and format based on information from
     * <a href="https://ntsi.com/drivers-license-format/">here</a> for a US state <BR> or from
     * <a href="https://www.sonnet.ca/FAQ/Quoting/What-is-the-correct-way-to-enter-my-drivers-licence-information">here</a>
     * for a Canadian Province/Territory
     */
    @SuppressWarnings("java:S112")
    public String getBase(State state, LicenseContact contact) {
        if (isState7Numbers(state)) {
            return Rand.numbers(7);
        } else if (isState8Numbers(state)) {
            return Rand.numbers(8);
        } else if (isState9Numbers(state)) {
            return Rand.numbers(9);
        } else if (state == State.CALIFORNIA
                || state == State.NEBRASKA
        ) {
            return Rand.letters(1).toUpperCase() + Rand.numbers(7);
        } else if (state == State.FLORIDA
                || state == State.ILLINOIS
                || state == State.MARYLAND
                || state == State.MICHIGAN
                || state == State.MINNESOTA
        ) {
            return Rand.letters(1).toUpperCase() + Rand.numbers(12);
        } else if (state == State.NEW_JERSEY) {
            return Rand.letters(1).toUpperCase() + Rand.numbers(14);
        } else if (state == State.WISCONSIN) {
            return Rand.letters(1).toUpperCase() + Rand.numbers(13);
        } else if (state == State.WASHINGTON) {
            return Rand.letters(1).toUpperCase() + Rand.numbers(11);
        } else if (state == State.NEW_HAMPSHIRE) {
            return Rand.numbers(2) + Rand.letters(3).toUpperCase() + Rand.numbers(5);
        } else if (isCanadian(state)) {
            return generateCanadianLicense(state, contact);
        } else {
            throw new RuntimeException("Base License Generation is not supported for state:  " + state);
        }
    }

    private boolean isState7Numbers(State state) {
        return state == State.ALABAMA // has other formats
                || state == State.ALASKA // has other formats
                || state == State.DELAWARE // has other formats
                || state == State.DISTRICT_OF_COLUMBIA // has other formats
                || state == State.GEORGIA // has other formats
                || state == State.MAINE // has other formats
                || state == State.NORTH_CAROLINA // has other formats
                || state == State.RHODE_ISLAND // has other formats
                || state == State.SOUTH_CAROLINA // has other formats
                || state == State.SOUTH_DAKOTA // has other formats
                || state == State.TENNESSEE // has other formats
                || state == State.TEXAS // has other formats
                || state == State.UTAH // has other formats
                || state == State.WEST_VIRGINIA // has other formats
                ;
    }

    private boolean isState8Numbers(State state) {
        return state == State.ALABAMA // has other formats
                || state == State.GEORGIA // has other formats
                || state == State.MAINE // has other formats
                || state == State.NEW_MEXICO // has other formats
                || state == State.NEW_YORK // has other formats
                || state == State.NORTH_CAROLINA // has other formats
                || state == State.OHIO // has other formats
                || state == State.PENNSYLVANIA
                || state == State.SOUTH_CAROLINA // has other formats
                || state == State.SOUTH_DAKOTA // has other formats
                || state == State.TENNESSEE // has other formats
                || state == State.TEXAS // has other formats
                || state == State.UTAH // has other formats
                || state == State.VERMONT // has other formats
                ;
    }

    private boolean isState9Numbers(State state) {
        return state == State.ARIZONA // has other formats
                || state == State.ARKANSAS
                || state == State.COLORADO // has other formats
                || state == State.CONNECTICUT
                || state == State.DISTRICT_OF_COLUMBIA // has other formats
                || state == State.GEORGIA // has other formats
                || state == State.HAWAII // has other formats
                || state == State.IDAHO // has other formats
                || state == State.INDIANA // has other formats
                || state == State.IOWA // has other formats
                || state == State.KANSAS // has other formats
                || state == State.KENTUCKY // has other formats
                || state == State.LOUISIANA // has other formats
                || state == State.MASSACHUSETTS // has other formats
                || state == State.MISSISSIPPI
                || state == State.MISSOURI // has other formats
                || state == State.MONTANA // has other formats
                || state == State.NEVADA // has other formats
                || state == State.NEW_MEXICO // has other formats
                || state == State.NEW_YORK // has other formats
                || state == State.NORTH_CAROLINA // has other formats
                || state == State.NORTH_DAKOTA // has other formats
                || state == State.OKLAHOMA // has other formats
                || state == State.OREGON // has other formats
                || state == State.SOUTH_CAROLINA // has other formats
                || state == State.SOUTH_DAKOTA // has other formats
                || state == State.TENNESSEE // has other formats
                || state == State.UTAH // has other formats
                || state == State.VIRGINIA // has other formats
                || state == State.WYOMING // has other formats
                ;
    }

    private boolean isValidateStartWithLastInitial(State state, String license, LicenseContact contact) {
        if (!isStartsWithLastInitial(license, contact)) {
            return false;
        }

        if ((state == State.MARYLAND || state == State.MICHIGAN || state == State.MINNESOTA)
                && license.matches(MD_MI_MN_1A_12N)) {
            return true;
        }

        return state == State.FLORIDA && license.matches(FL_1A_12N)
                || state == State.ILLINOIS && license.matches(IL_1A_11TO12N)
                || state == State.KENTUCKY && license.matches(AZ_HI_KS_KY_MA_MT_1A_8N)
                || state == State.KENTUCKY && license.matches(IN_OK_KY_1A_9N)
                || state == State.MICHIGAN && license.matches(MI_1A_10N)
                || state == State.WISCONSIN && license.matches(WI_1A_13N)
                ;
    }

    private boolean isOtherUnitedStates(State state) {
        return state != null
                && state != State.WASHINGTON
                && !shouldStartWithLastInitial(state)
                && !isCanadian(state);
    }

    private boolean isValidateOtherUnitedStatesLicense(State state, String license, LicenseContact contact) {
        if (StringUtils.isBlank(license)) {
            return false;
        }

        return isValidate7NumbersLicense(state, license, contact)
                || isValidate8NumbersLicense(state, license, contact)
                || isValidate9NumbersLicense(state, license, contact)
                || isValidateGroup1(state, license, contact)
                || isValidateGroup2(state, license, contact)
                || isValidateGroup3(state, license, contact)
                || isValidateGroup4(state, license, contact)
                || isValidateGroup5(state, license, contact)
                || isValidateGroup6(state, license, contact)
                || isValidateGroup7(state, license, contact)
                ;
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidate7NumbersLicense(State state, String license, LicenseContact contact) {
        return isState7Numbers(state) && license.matches(SEVEN_NUMBERS);
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidate8NumbersLicense(State state, String license, LicenseContact contact) {
        return isState8Numbers(state) && license.matches(EIGHT_NUMBERS);
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidate9NumbersLicense(State state, String license, LicenseContact contact) {
        return isState9Numbers(state) && license.matches(NINE_NUMBERS);
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidateGroup1(State state, String license, LicenseContact contact) {
        if (!isGroup1(state)) {
            return false;
        }

        return state == State.ALABAMA && license.matches(AL_1TO8N)
                || state == State.ALASKA && license.matches(AK_DE_1TO7N)
                || state == State.ARIZONA && license.matches(AZ_HI_KS_KY_MA_MT_1A_8N)
                || state == State.ARKANSAS && license.matches(AR_4TO9N)
                ;
    }

    private boolean isGroup1(State state) {
        return state == State.ALABAMA
                || state == State.ALASKA
                || state == State.ARIZONA
                || state == State.ARKANSAS
                ;
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidateGroup2(State state, String license, LicenseContact contact) {
        if (!isGroup2(state)) {
            return false;
        }

        return state == State.CALIFORNIA && license.matches(CA_NE_NY_1A_7N)
                || state == State.COLORADO && license.matches(CO_1A_3TO6N)
                || state == State.COLORADO && license.matches(CO_2A_2TO5N)
                || state == State.DELAWARE && license.matches(AK_DE_1TO7N)
                || state == State.HAWAII && license.matches(AZ_HI_KS_KY_MA_MT_1A_8N)
                ;
    }

    private boolean isGroup2(State state) {
        return state == State.CALIFORNIA
                || state == State.COLORADO
                || state == State.DELAWARE
                || state == State.HAWAII
                ;
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidateGroup3(State state, String license, LicenseContact contact) {
        if (!isGroup3(state)) {
            return false;
        }

        return state == State.IDAHO && license.matches(ID_2A_6N_1A)
                || state == State.INDIANA && license.matches(IN_OK_KY_1A_9N)
                || state == State.INDIANA && license.matches(IN_NV_10N)
                || state == State.IOWA && license.matches(IA_3N_2A_4N)
                || state == State.KANSAS && license.matches(KS_1A_1N_1A_1N_1A)
                || state == State.KANSAS && license.matches(AZ_HI_KS_KY_MA_MT_1A_8N)
                || state == State.LOUISIANA && license.matches(LA_1TO9N)
                ;
    }

    private boolean isGroup3(State state) {
        return state == State.IDAHO
                || state == State.INDIANA
                || state == State.IOWA
                || state == State.KANSAS
                || state == State.LOUISIANA
                ;
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidateGroup4(State state, String license, LicenseContact contact) {
        if (!isGroup4(state)) {
            return false;
        }

        if (state == State.MISSOURI) {
            return license.matches(MO_1A_5TO9N)
                    || license.matches(MO_1A_6N_R)
                    || license.matches(MO_8N_2A)
                    || license.matches(MO_9N_1A)
                    ;
        }

        if (state == State.MONTANA) {
            return license.matches(AZ_HI_KS_KY_MA_MT_1A_8N)
                    || license.matches(MT_13TO14N)
                    ;
        }

        return state == State.MAINE && license.matches(ME_7N_1A)
                || state == State.MASSACHUSETTS && license.matches(AZ_HI_KS_KY_MA_MT_1A_8N)
                ;
    }

    private boolean isGroup4(State state) {
        return state == State.MAINE
                || state == State.MASSACHUSETTS
                || state == State.MISSOURI
                || state == State.MONTANA
                ;
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidateGroup5(State state, String license, LicenseContact contact) {
        if (!isGroup5(state)) {
            return false;
        }

        if (state == State.NEVADA) {
            return license.matches(IN_NV_10N)
                    || license.matches(NV_SD_12N)
                    || license.matches(NV_X_8N)
                    ;
        }

        if (state == State.NEW_YORK) {
            return license.matches(CA_NE_NY_1A_7N)
                    || license.matches(NY_1A_18N)
                    || license.matches(NY_16N)
                    || license.matches(NY_8A)
                    ;
        }

        return state == State.NEBRASKA && license.matches(NE_1A_6TO8N)
                || state == State.NEW_HAMPSHIRE && license.matches(NH_2N_3A_5N)
                || state == State.NEW_JERSEY && license.matches(NJ_1A_14N)
                || state == State.NORTH_CAROLINA && license.matches(NC_1TO12N)
                || state == State.NORTH_DAKOTA && license.matches(ND_3A_6N)
                ;
    }

    private boolean isGroup5(State state) {
        return state == State.NEBRASKA
                || state == State.NEVADA
                || state == State.NEW_HAMPSHIRE
                || state == State.NEW_JERSEY
                || state == State.NEW_YORK
                || state == State.NORTH_CAROLINA
                || state == State.NORTH_DAKOTA
                ;
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidateGroup6(State state, String license, LicenseContact contact) {
        if (!isGroup6(state)) {
            return false;
        }

        if (state == State.OHIO) {
            return license.matches(OH_1A_4TO8N) || license.matches(OH_2A_3TO7N);
        }

        return state == State.OKLAHOMA && license.matches(IN_OK_KY_1A_9N)
                || state == State.OREGON && license.matches(OR_1TO9N)
                || state == State.RHODE_ISLAND && license.matches(RI_1A_6N)
                || state == State.SOUTH_CAROLINA && license.matches(SC_5TO11N)
                || state == State.SOUTH_DAKOTA && license.matches(SD_6TO10N)
                || state == State.SOUTH_DAKOTA && license.matches(NV_SD_12N)
                ;
    }

    private boolean isGroup6(State state) {
        return state == State.OHIO
                || state == State.OKLAHOMA
                || state == State.OREGON
                || state == State.RHODE_ISLAND
                || state == State.SOUTH_CAROLINA
                || state == State.SOUTH_DAKOTA
                ;
    }

    @SuppressWarnings("java:S1172")
    private boolean isValidateGroup7(State state, String license, LicenseContact contact) {
        if (!isGroup7(state)) {
            return false;
        }

        return state == State.UTAH && license.matches(UT_4TO10N)
                || state == State.VERMONT && license.matches(VT_7N_A)
                || state == State.VIRGINIA && license.matches(VA_1A_8TO11N)
                || state == State.WEST_VIRGINIA && license.matches(WV_1TO2A_5TO6N)
                || state == State.WYOMING && license.matches(WY_9TO10N)
                ;
    }

    private boolean isGroup7(State state) {
        return state == State.UTAH
                || state == State.VERMONT
                || state == State.VIRGINIA
                || state == State.WEST_VIRGINIA
                || state == State.WYOMING
                ;
    }

    private String applyOtherUnitedStatesValidations(State state, String license, LicenseContact contact) {
        if (isValidateOtherUnitedStatesLicense(state, license, contact)) {
            return license;
        }

        return getBase(state, contact);
    }

}
