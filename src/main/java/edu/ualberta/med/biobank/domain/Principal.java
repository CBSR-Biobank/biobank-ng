package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "PRINCIPAL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
public class Principal extends DomainEntity implements HasStatus {

    // @NotEmpty(groups = PreInsert.class, message = "{edu.ualberta.med.biobank.domain.Principal.memberships.NotEmpty}")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "principal")
    private Set<Membership> memberships = new HashSet<Membership>(0);

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Principal.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status = Status.ACTIVE;

    // Require at least one membership on creation so there is some loose
    // association between the creator and the created user.
    // FIXME: move this to group and require at least one group or membership
    // for a user.
    public Set<Membership> getMemberships() {
        return this.memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    @Override
    public Status getActivityStatus() {
        return this.status;
    }

    @Override
    public void setActivityStatus(Status status) {
        this.status = status;
    }

    /**
     * Return true if this {@link Principal} can be removed by the given
     * {@link User}, i.e. if the given {@link User} is of <em>equal</em> or
     * greater power.
     *
     * @param user
     * @return true if this {@link Principal} is subordinate to the given
     *         {@link User}.
     */
    @Transient
    public boolean isFullyManageable(User user) {
        for (Membership membership : getMemberships()) {
            if (!membership.isFullyManageable(user))
                return false;
        }
        return true;
    }
}
