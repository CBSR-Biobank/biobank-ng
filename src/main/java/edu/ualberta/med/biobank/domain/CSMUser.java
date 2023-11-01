package edu.ualberta.med.biobank.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "CSM_USER")
public class CSMUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.CSMUser.loginName.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.CSMUser.loginName.NotBlank}")
    @Column(name = "LOGIN_NAME", unique = true)
    private String loginName;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.CSMUser.migratedFlag.NotEmpty}")
    @Column(name = "MIGRATED_FLAG", columnDefinition = "TINYINT", length = 1)
    private boolean migratedFlag;

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.CSMUser.firstName.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.CSMUser.firstName.NotBlank}")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.CSMUser.lastName.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.CSMUser.lastName.NotBlank}")
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.CSMUser.updateDate.NotEmpty}")
    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "PREMGRT_LOGIN_NAME")
    private String preMgrtLoginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isMigratedFlag() {
        return migratedFlag;
    }

    public void setMigratedFlag(boolean migratedFlag) {
        this.migratedFlag = migratedFlag;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getPreMgrtLoginName() {
        return preMgrtLoginName;
    }

    public void setPreMgrtLoginName(String preMgrtLoginName) {
        this.preMgrtLoginName = preMgrtLoginName;
    }
}
