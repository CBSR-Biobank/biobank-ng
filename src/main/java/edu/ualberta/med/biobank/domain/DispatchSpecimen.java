package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;
import edu.ualberta.med.biobank.domain.type.DispatchSpecimenState;
import edu.ualberta.med.biobank.domain.type.DispatchSpecimenStateConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "DISPATCH_SPECIMEN",
uniqueConstraints = {
    @UniqueConstraint(columnNames = { "DISPATCH_ID", "SPECIMEN_ID" }) })
public class DispatchSpecimen extends DomainEntity implements HasComments {

    @NotNull(message = "{edu.ualberta.med.biobank.domain.DispatchSpecimen.state.NotNull}")
    @Column(name = "STATE")
    @Convert(converter = DispatchSpecimenStateConverter.class)
    private DispatchSpecimenState state = DispatchSpecimenState.NONE;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.DispatchSpecimen.dispatch.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISPATCH_ID", nullable = false)
    private Dispatch dispatch;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.DispatchSpecimen.specimen.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIMEN_ID", nullable = false)
    private Specimen specimen;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "DISPATCH_SPECIMEN_COMMENT",
    joinColumns = { @JoinColumn(name = "DISPATCH_SPECIMEN_ID", nullable = false) },
    inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    public DispatchSpecimenState getState() {
        return this.state;
    }

    public void setState(DispatchSpecimenState state) {
        this.state = state;
    }

    public Dispatch getDispatch() {
        return this.dispatch;
    }

    public void setDispatch(Dispatch dispatch) {
        this.dispatch = dispatch;
    }

    public Specimen getSpecimen() {
        return this.specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
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
