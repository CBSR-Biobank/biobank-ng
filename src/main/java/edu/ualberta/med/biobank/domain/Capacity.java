package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Capacity {

    @Min(value = 0, message = "{edu.ualberta.med.biobank.domain.Capacity.rowCapacity.Min}")
    @NotNull(message = "{edu.ualberta.med.biobank.domain.Capacity.rowCapacity.NotNull}")
    @Column(name = "ROW_CAPACITY", nullable = false)
    private Integer rowCapacity;

    @Min(value = 0, message = "{edu.ualberta.med.biobank.domain.Capacity.rowCapacity.Min}")
    @NotNull(message = "{edu.ualberta.med.biobank.domain.Capacity.colCapacity.NotNull}")
    @Column(name = "COL_CAPACITY", nullable = false)
    private Integer colCapacity;

    public Capacity() {}

    public Capacity(Integer maxRows, Integer maxCols) {
        this.rowCapacity = maxRows;
        this.colCapacity = maxCols;
    }

    public Capacity(Capacity other) {
        this.rowCapacity = other.getRowCapacity();
        this.colCapacity = other.getColCapacity();
    }

    public Integer getRowCapacity() {
        return this.rowCapacity;
    }

    public void setRowCapacity(Integer rowCapacity) {
        this.rowCapacity = rowCapacity;
    }

    public Integer getColCapacity() {
        return this.colCapacity;
    }

    public void setColCapacity(Integer colCapacity) {
        this.colCapacity = colCapacity;
    }

    @Override
    public String toString() {
        return "Capacity [rowCapacity=" + rowCapacity + ", colCapacity=" + colCapacity + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getColCapacity() == null) ? 0 : getColCapacity().hashCode());
        result = prime * result + ((getRowCapacity() == null) ? 0 : getRowCapacity().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Capacity that = (Capacity) obj;
        if (getColCapacity() == null) {
            if (that.getColCapacity() != null) {
                return false;
            }
        } else if (!getColCapacity().equals(that.getColCapacity())) {
            return false;
        }
        if (getRowCapacity() == null) {
            if (that.getRowCapacity() != null) {
                return false;
            }
        } else if (!getRowCapacity().equals(that.getRowCapacity())) {
            return false;
        }
        return true;
    }
}
