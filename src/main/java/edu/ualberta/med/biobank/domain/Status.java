package edu.ualberta.med.biobank.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The id of these enumerations are saved in the database. Therefore, DO NOT
 * CHANGE THESE ENUM IDS (unless you are prepared to write an upgrade script).
 * However, order and enum name can be modified freely.
 * <p>
 * Also, these enums should probably never be deleted, unless they are not used
 * in <em>any</em> database. Instead, they should be deprecated and probably
 * always return false when checking allow-ability.
 *
 * @author Jonathan Ferland
 */
public enum Status {
    NONE(0, "None"),
    ACTIVE(1, "Active"),
    CLOSED(2, "Closed"),
    // TODO: why can't there be a closed and flagged item or an active but
    // flagged item? Especially for users that are mean to be enabled or
    // disabled. When is ActivityStatus.FLAGGED even used in the source code?
    // Can a flagged user log in? What is the point of flagged? Shouldn't it be
    // separate?
    FLAGGED(4, "Flagged");

    private static final List<Status> VALUES_LIST = Collections
        .unmodifiableList(Arrays.asList(values()));

    public static List<Status> valuesList() {
        return VALUES_LIST;
    }

    public static Status fromId(Integer id) {
        for (Status item : values()) {
            if (item.id.equals(id)) return item;
        }
        return null;
    }

    private final Integer id;
    private final String name;

    private Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name.toString();
    }

    @Override
    public String toString() {
        return getName();
    }
}
