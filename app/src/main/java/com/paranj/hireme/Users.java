package com.paranj.hireme;

/**
 * Created by Paranj on 11/21/17.
 */

public class Users {

    private String uId, first, last, phoneNumber, email, countryCode;

    public Users(String uId, String first, String last, String phoneNumber, String email, String countryCode) {
        this.uId = uId;
        this.first = first;
        this.last = last;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.countryCode = countryCode;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
