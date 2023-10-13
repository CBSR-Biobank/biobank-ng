package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


/**
 * A contact person associated with a collecting location. This person should be
 * the point of contact for any inquiries.
 */
@Entity
@Table(name = "CONTACT")
public class Contact extends DomainEntity implements HasName {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Contact.name.NotNull}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.Contact.name.NotBlank}")
    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "MOBILE_NUMBER", length = 50)
    private String mobileNumber;

    @Column(name = "FAX_NUMBER", length = 50)
    private String faxNumber;

    @Column(name = "EMAIL_ADDRESS", length = 50)
    private String emailAddress;

    @Column(name = "PAGER_NUMBER", length = 50)
    private String pagerNumber;

    @Column(name = "OFFICE_NUMBER", length = 50)
    private String officeNumber;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "contacts")
    private Set<Study> studies = new HashSet<Study>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLINIC_ID", nullable = false)
    private Clinic clinic;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    // TODO: write an email check that allows null @Email
    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPagerNumber() {
        return this.pagerNumber;
    }

    public void setPagerNumber(String pagerNumber) {
        this.pagerNumber = pagerNumber;
    }

    public String getOfficeNumber() {
        return this.officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public Set<Study> getStudies() {
        return this.studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public Clinic getClinic() {
        return this.clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }
}
