package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "ORIGIN_INFO")
public class OriginInfo extends DomainEntity implements HasComments {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "originInfo")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    private Set<Specimen> specimens = new HashSet<Specimen>(0);

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "SHIPMENT_INFO_ID", unique = true)
    private ShipmentInfo shipmentInfo;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.OriginInfo.center.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CENTER_ID", nullable = false)
    private Center center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_SITE_ID")
    private Center receiverCenter;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "ORIGIN_INFO_COMMENT",
        joinColumns = { @JoinColumn(name = "ORIGIN_INFO_ID", nullable = false, updatable = false) },
        inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false, updatable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Specimen> getSpecimens() {
        return this.specimens;
    }

    public void setSpecimens(Set<Specimen> specimens) {
        this.specimens = specimens;
    }

    public ShipmentInfo getShipmentInfo() {
        return this.shipmentInfo;
    }

    public void setShipmentInfo(ShipmentInfo shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

    public Center getCenter() {
        return this.center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public Center getReceiverCenter() {
        return this.receiverCenter;
    }

    public void setReceiverCenter(Center receiverCenter) {
        this.receiverCenter = receiverCenter;
    }
}
