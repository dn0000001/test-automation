package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.csv.CSVFormat;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * Example test that writes a csv file which contains a list
 */
public class CsvWriteContainsListTest extends TestNGBase {
    private static final String[] HEADER_ROW = {"ROW", "First", "Last"};
    private static final List<String> GENERIC_CONTACT_DETAILS_HEADER = Arrays.asList("Phone", "Email");
    private static final List<String> GENERIC_ADDRESSES_HEADER = Arrays.asList("Type", "Address");
    private List<CsvOutputRecord> outputRecords;
    private int maxContactDetailsSize = 1;
    private int maxAddressesSize = 1;

    private enum AddressType {
        HOME,
        WORK,
        COTTAGE
    }

    private class Address {
        AddressType type;
        String address;

        public AddressType getType() {
            return type;
        }

        public void setType(AddressType type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }

    private class ContactDetails {
        private String phone;
        private String email;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    private class OutputInfo implements CsvOutputRecord {
        private String row;
        private String first;
        private String last;
        private List<ContactDetails> contactDetails;
        private List<Address> addresses;

        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public List<ContactDetails> getContactDetails() {
            if (contactDetails == null) {
                contactDetails = new ArrayList<>();
            }

            return contactDetails;
        }

        public List<Address> getAddresses() {
            if (addresses == null) {
                addresses = new ArrayList<>();
            }

            return addresses;
        }

        @Override
        public List<String> asList() {
            List<String> columns = new ArrayList<>();
            columns.add(getRow());
            columns.add(getFirst());
            columns.add(getLast());
            for (ContactDetails item : getContactDetails()) {
                columns.add(item.getPhone());
                columns.add(item.getEmail());
            }

            for (Address item : getAddresses()) {
                if (item.getType() == null) {
                    columns.add("");
                } else {
                    columns.add(item.getType().toString());
                }

                columns.add(item.getAddress());
            }

            return columns;
        }

        @Override
        public void padListsWithEmptyItems(int... totals) {
            assertThat("Totals for this class", totals.length, equalTo(2));
            padContactDetailsWithEmptyItems(totals[0]);
            padAddressesWithEmptyItems(totals[1]);
        }

        private void padContactDetailsWithEmptyItems(int totalContactDetails) {
            // In this example test, I always want at least 1 contact details
            assertThat("Total Contact Details", totalContactDetails, greaterThanOrEqualTo(1));

            // If the number of contract details is greater than the total, then it must be invalid
            int padItems = totalContactDetails - getContactDetails().size();
            assertThat("Total Contact Details Invalid", padItems, greaterThanOrEqualTo(0));

            for (int i = 0; i < padItems; i++) {
                ContactDetails empty = new ContactDetails();
                empty.setPhone("");
                empty.setEmail("");
                getContactDetails().add(empty);
            }
        }

        private void padAddressesWithEmptyItems(int totalAddresses) {
            // In this example test, I always want at least 1 address
            assertThat("Total Addresses", totalAddresses, greaterThanOrEqualTo(1));

            // If the number of addresses is greater than the total, then it must be invalid
            int padItems = totalAddresses - getAddresses().size();
            assertThat("Total Addresses Invalid", padItems, greaterThanOrEqualTo(0));

            for (int i = 0; i < padItems; i++) {
                Address empty = new Address();
                empty.setAddress("");
                getAddresses().add(empty);
            }
        }

    }

    private void createTestData() {
        outputRecords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OutputInfo item = new OutputInfo();
            item.setRow("" + (i + 1));
            item.setFirst(Rand.letters(10));
            item.setLast(Rand.letters(20));
            item.getContactDetails().addAll(createContactDetailsTestData(Rand.randomRange(1, 5)));
            item.getAddresses().addAll(createAddressesTestData(Rand.randomRange(1, 2)));
            outputRecords.add(item);

            // Keep track of the max size of contact details
            // Note:  Normally, this would be done at the point where the test data is being stored
            // such that it can be output to a CSV file later
            maxContactDetailsSize = Math.max(maxContactDetailsSize, item.getContactDetails().size());
            maxAddressesSize = Math.max(maxAddressesSize, item.getAddresses().size());
        }
    }

    private List<ContactDetails> createContactDetailsTestData(int size) {
        List<ContactDetails> all = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ContactDetails item = new ContactDetails();
            item.setEmail(Rand.alphanumeric(10) + "@gmail.com");
            item.setPhone(Rand.numbers(3) + "-" + Rand.numbers(3) + "-" + Rand.numbers(4));
            all.add(item);
        }

        return all;
    }

    private List<Address> createAddressesTestData(int size) {
        List<Address> addresses = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Address item = new Address();
            item.setType((AddressType) Rand.randomEnum(AddressType.HOME));
            item.setAddress(Rand.letters(20));
            addresses.add(item);
        }

        return addresses;
    }

    private String[] getHeaderRow() {
        List<String> headers = new ArrayList<>(Arrays.asList(HEADER_ROW));
        headers.addAll(CsvUtils.generateCsvHeadersForList(GENERIC_CONTACT_DETAILS_HEADER, maxContactDetailsSize, "contactDetails"));
        headers.addAll(CsvUtils.generateCsvHeadersForList(GENERIC_ADDRESSES_HEADER, maxAddressesSize, "addresses"));
        return headers.toArray(new String[0]);
    }

    private void writeToCSV() {
        CSVFormat format = CSVFormat.EXCEL.withHeader(getHeaderRow());
        SimpleDateFormat formatter = new SimpleDateFormat("hh_mm_ss");
        String strDateStamp = formatter.format(new Date());
        String filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "test-csv-file-" + strDateStamp + ".csv";
        CsvUtils.writeToCSV(filename, format, outputRecords, maxContactDetailsSize, maxAddressesSize);
    }

    @Test
    public void performCsvWriteTest() {
        createTestData();
        writeToCSV();
    }

}
