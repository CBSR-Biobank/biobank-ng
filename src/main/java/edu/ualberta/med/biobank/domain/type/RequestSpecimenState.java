package edu.ualberta.med.biobank.domain.type;

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
public enum RequestSpecimenState implements ItemState {
    AVAILABLE_STATE(0, "Available"),
    PULLED_STATE(1, "Pulled"),
    UNAVAILABLE_STATE(2, "Unavailable"),
    DISPATCHED_STATE(3, "Dispatched");

    private static final List<RequestSpecimenState> VALUES_LIST = Collections
        .unmodifiableList(Arrays.asList(values()));

    public static List<RequestSpecimenState> valuesList() {
        return VALUES_LIST;
    }

    public static RequestSpecimenState fromId(Integer id) {
        for (RequestSpecimenState item : values()) {
            if (item.id.equals(id)) return item;

        }
        return null;
    }

    private Integer id;
    private String label;

    private RequestSpecimenState(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
