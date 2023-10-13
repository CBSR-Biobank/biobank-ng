package edu.ualberta.med.biobank.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "COMMENT")
public class Comment extends DomainEntity
    implements HasCreatedAt {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Comment.message.NotNull}")
    @Column(name = "MESSAGE", columnDefinition = "TEXT")
    private String message;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Comment.createdAt.NotNull}")
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Comment.user.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // TODO: stop this property from being updated, and test that!
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
