package edu.ualberta.med.biobank.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The id of these enumerations are saved in the database. Therefore, DO NOT CHANGE THESE ENUM IDS
 * (unless you are prepared to write an upgrade script). However, order and enum name can be
 * modified freely.
 * <p>
 * Also, these enums should probably never be deleted, unless they are not used in <em>any</em>
 * database. Instead, they should be deprecated and probably always return false when checking
 * allow-ability.
 *
 * @author Jonathan Ferland
 *
 */
public enum PermissionEnum {
    UNUSED0(0, "Unused-0"),
    UNUSED1(1, "Unused-1"),

    SPECIMEN_CREATE(2, "Specimen Create"),
    SPECIMEN_READ(3, "Specimen Read"),
    SPECIMEN_UPDATE(4, "Specimen Update"),
    SPECIMEN_DELETE(5, "Specimen Delete"),
    SPECIMEN_LINK(6, "Specimen Link"),
    SPECIMEN_ASSIGN(7, "Specimen Assign"),

    SITE_CREATE(8, "Site Create", Require.ALL_CENTERS),
    SITE_READ(9, "Site Read"),
    SITE_UPDATE(10, "Site Update"),
    SITE_DELETE(11, "Site Delete"),

    PATIENT_CREATE(12, "Patient Create"),
    PATIENT_READ(13, "Patient Read"),
    PATIENT_UPDATE(14, "Patient Update"),
    PATIENT_DELETE(15, "Patient Delete"),
    PATIENT_MERGE(16, "Patient Merge"),

    COLLECTION_EVENT_CREATE(17, "Collection Event Create"),
    COLLECTION_EVENT_READ(18, "Collection Event Read"),
    COLLECTION_EVENT_UPDATE(19, "Collection Event Update"),
    COLLECTION_EVENT_DELETE(20, "Collection Event Delete"),

    PROCESSING_EVENT_CREATE(21, "Processing Event Create"),
    PROCESSING_EVENT_READ(22, "Processing Event Read"),
    PROCESSING_EVENT_UPDATE(23, "Processing Event Update"),
    PROCESSING_EVENT_DELETE(24, "Processing Event Delete"),

    ORIGIN_INFO_CREATE(25, "Origin Information Create"),
    ORIGIN_INFO_READ(26, "Origin Information Read"),
    ORIGIN_INFO_UPDATE(27, "Origin Information Update"),
    ORIGIN_INFO_DELETE(28, "Origin Information Delete"),

    DISPATCH_CREATE(29, "Dispatch Create"),
    DISPATCH_READ(30, "Dispatch Read"),
    DISPATCH_CHANGE_STATE(31, "Dispatch Change State"),
    DISPATCH_UPDATE(32, "Dispatch Update"),
    DISPATCH_DELETE(33, "Dispatch Delete"),

    RESEARCH_GROUP_CREATE(34, "Research Group Create", Require.ALL_CENTERS),
    RESEARCH_GROUP_READ(35, "Research Group Read"),
    RESEARCH_GROUP_UPDATE(36, "Research Group Update"),
    RESEARCH_GROUP_DELETE(37, "Research Group Delete"),

    STUDY_CREATE(38, "Study Create", Require.ALL_STUDIES),
    STUDY_READ(39, "Study Read"),
    STUDY_UPDATE(40, "Study Update"),
    STUDY_DELETE(41, "Study Delete"),

    REQUEST_CREATE(42, "Request Create"),
    REQUEST_READ(43, "Request Read"),
    REQUEST_UPDATE(44, "Request Update"),
    REQUEST_DELETE(45, "Request Delete"),
    REQUEST_PROCESS(46, "Request Process"),

    CLINIC_CREATE(47, "Clinic Create", Require.ALL_CENTERS),
    CLINIC_READ(48, "Clinic Read"),
    CLINIC_UPDATE(49, "Clinic Update"),
    CLINIC_DELETE(50, "Clinic Delete"),

    CONTAINER_TYPE_CREATE(52, "Container Type Create"),
    CONTAINER_TYPE_READ(53, "Container Type Read"),
    CONTAINER_TYPE_UPDATE(54, "Container Type Update"),
    CONTAINER_TYPE_DELETE(55, "Container Type Delete"),

    CONTAINER_CREATE(56, "Container Create"),
    CONTAINER_READ(57, "Container Read"),
    CONTAINER_UPDATE(58, "Container Update"),
    CONTAINER_DELETE(59, "Container Delete"),

    SPECIMEN_TYPE_CREATE(60, "Specimen Type Create", Require.ALL_CENTERS, Require.ALL_STUDIES),
    SPECIMEN_TYPE_READ(61, "Specimen Type Read"),
    SPECIMEN_TYPE_UPDATE(62, "Specimen Type Update", Require.ALL_CENTERS, Require.ALL_STUDIES),
    SPECIMEN_TYPE_DELETE(63, "Specimen Type Delete", Require.ALL_CENTERS, Require.ALL_STUDIES),

    LOGGING(64, "Logging"),
    REPORTS(65, "Reports"),

    SPECIMEN_LIST(66, "Specimen List"),
    LABEL_PRINTING(67, "Label Printing"),
    BATCH_OPERATIONS(68, "Batch operations"),

    SPECIMEN_LINK_AND_ASSIGN(69, "Specimen Link and Assign");

    private static final List<PermissionEnum> VALUES_LIST = Collections
        .unmodifiableList(Arrays.asList(values()));
    private static final Map<Integer, PermissionEnum> VALUES_MAP;

    static {
        Map<Integer, PermissionEnum> map =
            new HashMap<Integer, PermissionEnum>();

        for (PermissionEnum permissionEnum : values()) {
            PermissionEnum check = map.get(permissionEnum.getId());
            if (check != null) {
                throw new IllegalStateException("permission enum value "
                    + permissionEnum.getId() + " used multiple times");
            }

            map.put(permissionEnum.getId(), permissionEnum);
        }

        VALUES_MAP = Collections.unmodifiableMap(map);
    }

    private final Integer id;
    private final String name;
    private final EnumSet<Require> requires;

    private PermissionEnum(Integer id, String name, Require... requires) {
        this.id = id;
        this.name = name;
        this.requires = EnumSet.of(Require.DEFAULT, requires);
    }

    public static List<PermissionEnum> valuesList() {
        return VALUES_LIST;
    }

    public static Map<Integer, PermissionEnum> valuesMap() {
        return VALUES_MAP;
    }

    public Integer getId() {
        return id;
    }

    public EnumSet<Require> getRequires() {
        return EnumSet.copyOf(requires);
    }

    public String getName() {
        return name;
    }

    public static PermissionEnum fromId(Integer id) {
        return valuesMap().get(id);
    }

    /**
     * Whether the given {@link User} has this {@link PermissionEnum} on <em>any</em> {@link Center}
     * or {@link Study}.
     *
     * @see {@link #isMembershipAllowed(Membership, Center, Study)}
     * @param user
     * @return
     */
    public boolean isAllowed(User user) {
        return isAllowed(user, null, null);
    }

    /**
     * Whether the given {@link User} has this {@link PermissionEnum} on <em>any</em> {@link Center}
     * , but a specific {@link Study}.
     *
     * @see {@link #isAllowed(User)}
     * @param user
     * @return
     */
    public boolean isAllowed(User user, Study study) {
        return isAllowed(user, null, study);
    }

    /**
     * Whether the given {@link User} has this {@link PermissionEnum} on <em>any</em> {@link Study},
     * but a specific {@link Center}.
     *
     * @see {@link #isAllowed(User)}
     * @param user
     * @return
     */
    public boolean isAllowed(User user, Center center) {
        return isAllowed(user, center, null);
    }

    /**
     *
     * @param user
     * @param center if null, {@link Center} does not matter.
     * @param study if null, {@link Study} does not matter.
     * @return
     */
    public boolean isAllowed(User user, Center center, Study study) {
        for (Membership m : user.getAllMemberships()) {
            if (isMembershipAllowed(m, center, study)) return true;
        }
        return false;
    }

    /**
     * Return true if special requirements (i.e. {@link Require}-s) are met on the given
     * {@link Membership}, otherwise false.
     *
     * @param m
     * @return
     */
    public boolean isRequirementsMet(Membership m) {
        boolean reqsMet = true;
        Domain d = m.getDomain();
        reqsMet &= !requires.contains(Require.ALL_CENTERS) || d.isAllCenters();
        reqsMet &= !requires.contains(Require.ALL_STUDIES) || d.isAllStudies();
        return reqsMet;
    }

    /**
     * This is a confusing check. If {@link Center} is null, it means we do not care about its
     * value, otherwise, {@link Domain#contains(Center)} must be true. The same applies to
     * {@link Study}.
     *
     * @param m
     * @param c
     * @param s
     * @return
     */
    private boolean isMembershipAllowed(Membership m, Center c, Study s) {
        boolean requiresMet = isRequirementsMet(m);
        boolean hasCenter = c == null || m.getDomain().contains(c);
        boolean hasStudy = s == null || m.getDomain().contains(s);
        boolean hasPermission = m.getAllPermissions().contains(this);

        boolean allowed = requiresMet && hasCenter && hasStudy && hasPermission;
        return allowed;
    }

    /**
     * Defines special requirements for a {@link PermissionEnum}.
     *
     * @author jferland
     *
     */
    public enum Require {
        /**
         * Does nothing but make creating {@link EnumSet}-s easier.
         */
        DEFAULT,

        /**
         * If present, the {@link PermissionEnum} must exist in a {@link Membership} for which its
         * {@link Domain#isAllCenters()} returns true.
         */
        ALL_CENTERS,

        /**
         * If present, the {@link PermissionEnum} must exist in a {@link Membership} for which its
         * {@link Domain#isAllStudies()} returns true.
         */
        ALL_STUDIES;
    }
}
