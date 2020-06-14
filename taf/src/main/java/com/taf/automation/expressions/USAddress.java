package com.taf.automation.expressions;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This class holds the US address information used in RandomRealUSAddressGenerator
 */
public class USAddress {
    @XStreamAsAttribute
    private String street;

    @XStreamAsAttribute
    private String city;

    @XStreamAsAttribute
    private String state;

    @XStreamAsAttribute
    private String zipCode;

    @XStreamAsAttribute
    private String country;

    @XStreamAsAttribute
    private String phoneNumber;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
