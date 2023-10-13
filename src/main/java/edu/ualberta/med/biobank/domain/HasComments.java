package edu.ualberta.med.biobank.domain;

import java.util.Set;

public interface HasComments {
    public Set<Comment> getComments();

    public void setComments(Set<Comment> comments);
}
