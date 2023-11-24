package edu.ualberta.med.biobank.domain;

import org.hibernate.annotations.Parameter;

import java.util.Date;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LOG")
public class Log implements ValueObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id-generator")
    @GenericGenerator(
        name = "id-generator",
        strategy = "edu.ualberta.med.biobank.domain.util.CustomIdGenerator",
        parameters = {
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "50")
        })
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "USERNAME", length = 100)
    private String username;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "CENTER", length = 50)
    private String center;

    @Column(name = "ACTION", length = 100)
    private String action;

    @Column(name = "PATIENT_NUMBER", length = 100)
    private String patientNumber;

    @Column(name = "INVENTORY_ID", length = 100)
    private String inventoryId;

    @Column(name = "LOCATION_LABEL")
    private String locationLabel;

    @Column(name = "DETAILS", columnDefinition="TEXT")
    private String details;

    @Column(name = "TYPE", length = 100)
    private String type;

    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCenter() {
        return this.center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPatientNumber() {
        return this.patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getLocationLabel() {
        return this.locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
