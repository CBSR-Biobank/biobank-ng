package edu.ualberta.med.biobank.domain.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The id of these enumerations are saved in the database. Therefore, DO NOT CHANGE THESE ENUM IDS
 * (unless you are prepared to write an upgrade script). However, order and enum name can be
 * modified freely.
 * <p>
 * Also, these enums should probably never be deleted, unless they are not used in <em>any</em>
 * database. Instead, they should be deprecated and probably always return false when checking
 * allow-ability.
 *
 * @author Nelson Loyola
 */
@SuppressWarnings("nls")
public enum LabelingLayout {
    VERTICAL(0, "Vertical"),
    HORIZONTAL(1, "Horizontal");

    private Integer id;
    private String label;

    private static final List<LabelingLayout> VALUES_LIST = Collections
        .unmodifiableList(Arrays.asList(values()));

    public static List<LabelingLayout> valuesList() {
        return VALUES_LIST;
    }

    public static LabelingLayout fromId(Integer id) {
        if (id == null) return VERTICAL;
        for (LabelingLayout item : values()) {
            if (item.id.equals(id)) return item;
        }
        return null;
    }

    private LabelingLayout(Integer id, String label) {
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
