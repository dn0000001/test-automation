package com.taf.automation.ui.support.generators;

import com.taf.automation.expressions.BasicParser;
import com.taf.automation.expressions.ExpressionParser;
import com.taf.automation.expressions.StateEquals;
import com.taf.automation.expressions.StateEqualsFromList;
import com.taf.automation.expressions.StateNotEquals;
import com.taf.automation.expressions.USAddress;
import com.taf.automation.expressions.ZipCodeEquals;
import com.taf.automation.expressions.ZipCodeEqualsFromList;
import com.taf.automation.expressions.ZipCodeNotEquals;
import com.taf.automation.expressions.ZipCodeSizeEquals5;
import com.taf.automation.expressions.ZipCodeSizeEquals9;
import com.taf.automation.ui.support.util.Utils;
import datainstiller.generators.File2ListReader;
import datainstiller.generators.GeneratorInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Generator to get a random real US address (McDonald locations)
 */
public class RandomRealUSAddressGenerator extends File2ListReader implements GeneratorInterface {
    private static final String NO_SHUFFLE = "NO_SHUFFLE;";
    private List<String> addresses = null;

    /**
     * Initialize the addresses<BR>
     * <B>Note:</B> The addresses are from real McDonald locations in the US<BR>
     */
    private void initAddresses() {
        if (addresses == null) {
            addresses = populate("/generators/real-us-addresses.csv");
        }
    }

    /**
     * Parse the raw address into an object
     *
     * @param rawAddress - Raw Address
     * @return USAddress
     */
    private USAddress parseAddress(String rawAddress) {
        String[] pieces = Utils.splitData(rawAddress, "\t", 6);
        USAddress address = new USAddress();
        address.setStreet(pieces[0]);
        address.setCity(pieces[1]);
        address.setState(pieces[2]);
        address.setZipCode(pieces[3]);
        address.setCountry(pieces[4]);
        address.setPhoneNumber(pieces[5]);
        return address;
    }

    /**
     * Format the address
     *
     * @param address - Address to be formatted
     * @param pattern - The pattern to be used in formatting
     * @return String
     */
    private String formatAddress(USAddress address, String pattern) {
        if (address == null) {
            return "";
        }

        if (StringUtils.defaultString(pattern).equals("")) {
            return address.getStreet() + ", " +
                    address.getCity() + ", " +
                    address.getState() + " " +
                    address.getZipCode() + " " +
                    address.getCountry();
        }

        String output = pattern;
        output = output.replace("{#}", address.getStreet());
        output = output.replace("{C}", address.getCity());
        output = output.replace("{S}", address.getState());
        output = output.replace("{Z}", address.getZipCode());
        output = output.replace("{U}", address.getCountry());
        output = output.replace("{P}", address.getPhoneNumber());
        return output;
    }

    /**
     * Generate a valid real US address<BR><BR>
     * <B>Pattern:</B><BR>
     * {#} - Street<BR>
     * {C} - City<BR>
     * {S} - State<BR>
     * {Z} - ZipCode<BR>
     * {U} - Country<BR>
     * {P} - Phone Number<BR><BR>
     * <B>Conditions:</B><BR>
     * <TABLE border="1">
     * <TR>
     * <TH>Condition</TH>
     * <TH>Value</TH>
     * </TR>
     * <TR>
     * <TD>Multiple And Conditions use separator</TD>
     * <TD>&amp;&amp;</TD>
     * </TR>
     * <TR>
     * <TD>State Code Equals</TD>
     * <TD>STATE==</TD>
     * </TR>
     * <TR>
     * <TD>State Code Not Equal To</TD>
     * <TD>STATE!=</TD>
     * </TR>
     * <TR>
     * <TD>State Code from List</TD>
     * <TD>STATE*=</TD>
     * </TR>
     * <TR>
     * <TD>ZipCode Equals</TD>
     * <TD>ZIP==</TD>
     * </TR>
     * <TR>
     * <TD>ZipCode Not Equal To</TD>
     * <TD>ZIP!=</TD>
     * </TR>
     * <TR>
     * <TD>ZipCode from List</TD>
     * <TD>ZIP*=</TD>
     * </TR>
     * <TR>
     * <TD>ZipCode Only 5 digits</TD>
     * <TD>ZIP5</TD>
     * </TR>
     * <TR>
     * <TD>ZipCode Only 9 digits</TD>
     * <TD>ZIP9</TD>
     * </TR>
     * </TABLE>Expression
     *
     * @param pattern    - Pattern for generated address
     * @param conditions - Conditions on the State & ZipCode fields
     * @return Empty string if could not find address matching conditions else address in format
     */
    @Override
    public String generate(String pattern, String conditions) {
        initAddresses();

        boolean shuffle = true;
        String theConditions = conditions;
        if (StringUtils.startsWithIgnoreCase(theConditions, NO_SHUFFLE)) {
            theConditions = StringUtils.removeStartIgnoreCase(conditions, NO_SHUFFLE);
            shuffle = false;
        }

        if (shuffle) {
            Collections.shuffle(addresses);
        }

        if (!StringUtils.defaultString(theConditions).equals("")) {
            ExpressionParser parser = new BasicParser()
                    .withExpression(new StateEquals())
                    .withExpression(new StateNotEquals())
                    .withExpression(new StateEqualsFromList())
                    .withExpression(new ZipCodeEquals())
                    .withExpression(new ZipCodeNotEquals())
                    .withExpression(new ZipCodeEqualsFromList())
                    .withExpression(new ZipCodeSizeEquals5())
                    .withExpression(new ZipCodeSizeEquals9())
                    .withConditions(theConditions);
            for (String rawAddress : addresses) {
                USAddress checkAddress = parseAddress(rawAddress);
                if (parser.eval(checkAddress)) {
                    return formatAddress(checkAddress, pattern);
                }
            }

            return "";
        }

        USAddress address = parseAddress(addresses.get(0));
        return formatAddress(address, pattern);
    }

}
