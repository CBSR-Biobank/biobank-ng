package edu.ualberta.med.biobank.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "SHIPMENT_INFO")
public class ShipmentInfo extends DomainEntity {

    @Column(name = "RECEIVED_AT")
    private Date receivedAt;

    @Column(name = "PACKED_AT")
    private Date packedAt;

    @Column(name = "WAYBILL")
    private String waybill;

    @Column(name = "BOX_NUMBER")
    private String boxNumber;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ShipmentInfo.shippingMethod.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHIPPING_METHOD_ID", nullable = false)
    private ShippingMethod shippingMethod;

    public Date getReceivedAt() {
        return this.receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Date getPackedAt() {
        return this.packedAt;
    }

    public void setPackedAt(Date packedAt) {
        this.packedAt = packedAt;
    }

    public String getWaybill() {
        return this.waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getBoxNumber() {
        return this.boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public ShippingMethod getShippingMethod() {
        return this.shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
}
