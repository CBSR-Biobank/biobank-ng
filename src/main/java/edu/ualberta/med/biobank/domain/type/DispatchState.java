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
public enum DispatchState {
    CREATION(0, "Creation"),
    IN_TRANSIT(1, "In Transit"),
    RECEIVED(2, "Received"),
    CLOSED(3, "Closed"),
    LOST(4, "Lost");

    private static final List<DispatchState> VALUES_LIST = Collections
        .unmodifiableList(Arrays.asList(values()));

    public static List<DispatchState> valuesList() {
        return VALUES_LIST;
    }

    public static DispatchState fromId(Integer id) {
        if (id == null) return CREATION;
        for (DispatchState item : values()) {
            if (item.id.equals(id)) return item;
        }
        return null;
    }

    private Integer id;
    private String label;

    private DispatchState(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public boolean isEquals(Integer state) {
        return id.equals(state);
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
