package edu.ualberta.med.biobank.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.ualberta.med.biobank.util.NullUtil;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@DiscriminatorValue("BbGroup")
public class Group extends Principal
        implements HasName {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.BbGroup.name.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.BbGroup.name.NotBlank}")
    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "GROUP_USER", joinColumns = @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false), inverseJoinColumns = @JoinColumn(name = "USER_ID", nullable = false, updatable = false))
    private Set<User> users = new HashSet<User>(0);

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    // TODO: enforce this again, someday
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    private static class NameComparator
            implements Comparator<Group>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(Group a, Group b) {
            if (a == null && b == null)
                return 0;
            if (a == null ^ b == null)
                return (a == null) ? -1 : 1;
            return NullUtil.cmp(a.getName(), b.getName());
        }
    }
}
