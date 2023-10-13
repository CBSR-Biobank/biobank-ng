package edu.ualberta.med.biobank.domain;

public interface HasId<T> {
    public T getId();

    public void setId(T id);
}
