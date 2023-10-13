package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class DomainEntity implements ValueObject {

    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Version
    @Column(name = "VERSION", nullable = false)
    public Integer getVersion() {
        return this.version;
    }

    /**
     * DO NOT CALL this method unless, maybe, for tests. Hibernate manages
     * setting this value.
     *
     * @param version
     */
    @Deprecated
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object that) {
        if (that == this) return true;
        if (that == null) return false;

        if (that instanceof ValueObject) {
            Integer thatId = ((ValueObject) that).getId();
            if (getId() != null && getId().equals(thatId)) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (getId() == null) return 0;
        return getId().hashCode();
    }

    @Transient
    public boolean isNew() {
        return getId() == null;
    }
}
