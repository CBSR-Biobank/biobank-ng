package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs(
    {
        @NamedEntityGraph(
            name = "user-with-groups",
            attributeNodes = { @NamedAttributeNode("groups"), @NamedAttributeNode("csmUser") }
        ),
        @NamedEntityGraph(
            name = "user-with-groups-and-memberships",
            attributeNodes = {
                @NamedAttributeNode(value = "groups", subgraph = "groups-memberships-subgraph"),
                @NamedAttributeNode(value = "memberships", subgraph = "memberships-subgraph")
            },
            subgraphs = {
                @NamedSubgraph(
                    name = "groups-memberships-subgraph",
                    attributeNodes = { @NamedAttributeNode(value = "memberships", subgraph = "memberships-subgraph") }
                ),
                @NamedSubgraph(
                    name = "memberships-subgraph",
                    attributeNodes = {
                        @NamedAttributeNode("domain"), @NamedAttributeNode("permissions"), @NamedAttributeNode("roles")
                    }
                )
            }
        )
    }
)
@Entity
@DiscriminatorValue("User")
public class User extends Principal implements HasComments {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.User.login.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.User.login.NotBlank}")
    @Column(name = "LOGIN", unique = true)
    private String login;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.User.csmUser.NotNull}")
    @OneToOne
    @JoinColumn(name = "CSM_USER_ID")
    private CSMUser csmUser;

    @Column(name = "RECV_BULK_EMAILS")
    private boolean recvBulkEmails = true;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "NEED_PWD_CHANGE")
    private boolean needPwdChange = true;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", updatable = false)
    private Set<Comment> comments = new HashSet<Comment>(0);

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Group> groups = new HashSet<Group>(0);

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // TODO: rename to isRecvBulkEmails
    public boolean getRecvBulkEmails() {
        return this.recvBulkEmails;
    }

    public void setRecvBulkEmails(boolean recvBulkEmails) {
        this.recvBulkEmails = recvBulkEmails;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // TODO: write an email check that allows null @Email
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "NEED_PWD_CHANGE")
    // TODO: rename to isRecvBulkEmails
    public boolean getNeedPwdChange() {
        return this.needPwdChange;
    }

    public void setNeedPwdChange(boolean needPwdChange) {
        this.needPwdChange = needPwdChange;
    }

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public CSMUser getCsmUser() {
        return csmUser;
    }

    public void setCsmUser(CSMUser csmUser) {
        this.csmUser = csmUser;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean isFullyManageable(User user) {
        if (!super.isFullyManageable(user)) return false;
        for (Group group : getGroups()) {
            if (!group.isFullyManageable(user)) return false;
        }
        return true;
    }

    /**
     * Returns all of this {@link User}'s {@link Memberships}, i.e. from both
     * the {@link User} directly and from the {@link Group}-s.
     *
     * @return
     */
    @Transient
    public Set<Membership> getAllMemberships() {
        Set<Membership> memberships = new HashSet<Membership>();
        memberships.addAll(getMemberships());

        for (Group group : getGroups()) {
            memberships.addAll(group.getMemberships());
        }

        return memberships;
    }

    @Transient
    public Set<Domain> getManageableDomains() {
        Set<Domain> domains = new HashSet<Domain>();
        for (Membership membership : getAllMemberships()) {
            if (membership.isUserManager()) {
                domains.add(membership.getDomain());
            }
        }
        return domains;
    }

    /**
     * Get a {@link Set} of all the {@link Membership}-s the given {@link User}
     * is able to partially manage (i.e. for which
     * {@link Membership#isPartiallyManageable(User)} returns true).
     *
     * @param u
     * @return
     */
    @Transient
    public Set<Membership> getManageableMemberships(User u) {
        Set<Membership> manageable = new HashSet<Membership>();
        for (Membership membership : getMemberships()) {
            if (membership.isPartiallyManageable(u)) {
                manageable.add(membership);
            }
        }
        return manageable;
    }
}
