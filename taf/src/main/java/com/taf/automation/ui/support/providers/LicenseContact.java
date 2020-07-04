package com.taf.automation.ui.support.providers;

import com.taf.automation.ui.support.DateActions;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.time.LocalDate;
import java.util.Date;

@XStreamAlias("license-contact")
public class LicenseContact {
    @XStreamAsAttribute
    private String first;

    @XStreamAsAttribute
    private String middle;

    @XStreamAsAttribute
    private String last;

    @XStreamAsAttribute
    private String dob;

    @XStreamAsAttribute
    private String dobPattern;

    /**
     * @return a new instance (for use in JEXL)
     */
    public LicenseContact newInstance() {
        return new LicenseContact();
    }

    public LicenseContact withFirst(String first) {
        this.first = first;
        return this;
    }

    public LicenseContact withMiddle(String middle) {
        this.middle = middle;
        return this;
    }

    public LicenseContact withLast(String last) {
        this.last = last;
        return this;
    }

    public LicenseContact withDOB(String dob, String pattern) {
        this.dob = dob;
        this.dobPattern = pattern;
        return this;
    }

    public String getFirst() {
        return first;
    }

    public String getMiddle() {
        return middle;
    }

    public String getLast() {
        return last;
    }

    public LocalDate getDob() {
        Date parsedDOB = DateActions.parseDateStrictly(dob, dobPattern);
        return DateActions.toLocalDate(parsedDOB);
    }

}
