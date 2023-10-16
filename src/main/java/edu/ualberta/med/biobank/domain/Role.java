package edu.ualberta.med.biobank.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.UniqueElements;

import edu.ualberta.med.biobank.util.NullUtil;

@Entity
@Table(name = "ROLE")
// TODO: check that no Membership uses this role before deleting
public class Role extends DomainEntity {
    public static final NameComparator NAME_COMPARATOR = new NameComparator();

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Role.name.NotEmpty}")
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @ElementCollection(targetClass = PermissionEnum.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ROLE_PERMISSION", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "PERMISSION_ID", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Set<PermissionEnum> permissions = new HashSet<PermissionEnum>(0);

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PermissionEnum> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    private static class NameComparator
        implements Comparator<Role>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(Role a, Role b) {
            if (a == null && b == null) return 0;
            if (a == null ^ b == null) return (a == null) ? -1 : 1;
            return NullUtil.cmp(a.getName(), b.getName());
        }
    }
}
