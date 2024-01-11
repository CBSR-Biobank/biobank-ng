package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import edu.ualberta.med.biobank.domain.type.DispatchState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * caTissue Term - Transfer Event: Event that refers to moving specimen from one
 * storage location to another storage location.
 *
 */
@Entity
@Table(name = "DISPATCH")
public class Dispatch extends DomainEntity implements HasComments {

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Dispatch.state.NotNull}")
    @Column(name = "STATE")
    @Enumerated(EnumType.ORDINAL)
    private DispatchState state = DispatchState.CREATION;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dispatch", orphanRemoval = true)
    private Set<DispatchSpecimen> dispatchSpecimens = new HashSet<DispatchSpecimen>(0);

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Dispatch.senderCenter.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_CENTER_ID", nullable = false)
    private Center senderCenter;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "SHIPMENT_INFO_ID", unique = true)
    private ShipmentInfo shipmentInfo;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Dispatch.receiverCenter.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_CENTER_ID", nullable = false)
    private Center receiverCenter;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "DISPATCH_COMMENT",
        joinColumns = { @JoinColumn(name = "DISPATCH_ID", nullable = false) },
        inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    public DispatchState getState() {
        return this.state;
    }

    public void setState(DispatchState state) {
        this.state = state;
    }

    public Set<DispatchSpecimen> getDispatchSpecimens() {
        return this.dispatchSpecimens;
    }

    public void setDispatchSpecimens(Set<DispatchSpecimen> dispatchSpecimens) {
        this.dispatchSpecimens = dispatchSpecimens;
    }

    public Center getSenderCenter() {
        return this.senderCenter;
    }

    public void setSenderCenter(Center senderCenter) {
        this.senderCenter = senderCenter;
    }

    public ShipmentInfo getShipmentInfo() {
        return this.shipmentInfo;
    }

    public void setShipmentInfo(ShipmentInfo shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

    public Center getReceiverCenter() {
        return this.receiverCenter;
    }

    public void setReceiverCenter(Center receiverCenter) {
        this.receiverCenter = receiverCenter;
    }

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

}
