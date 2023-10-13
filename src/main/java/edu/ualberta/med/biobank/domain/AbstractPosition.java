package edu.ualberta.med.biobank.domain;

import edu.ualberta.med.biobank.domain.util.RowColPos;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AbstractPosition extends DomainEntity {

    @Min(value = 0, message = "{edu.ualberta.med.biobank.domain.AbstractPosition.row.Min}")
    @NotNull(message = "{edu.ualberta.med.biobank.domain.AbstractPosition.row.NotNull}")
    @Column(name = "ROW", nullable = false)
    private Integer row;

    @Min(value = 0, message = "{edu.ualberta.med.biobank.domain.AbstractPosition.col.Min}")
    @NotNull(message = "col cannot be null")
    @Column(name = "COL", nullable = false)
    private Integer col;

    public Integer getRow() {
        return this.row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return this.col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    @Transient
    public RowColPos getPosition() {
        return new RowColPos(getRow(), getCol());
    }

    // AbstractPosition _SHOULD_ have a parentContainer property so it has more
    // meaning, but this method is a band-aid until ParentContainer is pulled
    // into this class.
    @Transient
    public abstract Container getHoldingContainer();
}
