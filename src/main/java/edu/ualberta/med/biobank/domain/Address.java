package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ADDRESS")
public class Address extends DomainEntity {

    @Column(name = "STREET1")
    private String street1;

    @Column(name = "STREET2")
    private String street2;

    @Column(name = "CITY", length = 50)
    private String city;

    @Column(name = "PROVINCE", length = 50)
    private String province;

    @Column(name = "POSTAL_CODE", length = 50)
    private String postalCode;

    @Column(name = "EMAIL_ADDRESS", length = 100)
    private String emailAddress;

    @Column(name = "PHONE_NUMBER", length = 50)
    private String phoneNumber;

    @Column(name = "FAX_NUMBER", length = 50)
    private String faxNumber;

    @Column(name = "COUNTRY", length = 50)
    private String country;

    public String getStreet1() {
        return this.street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return this.street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
