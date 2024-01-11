package edu.ualberta.med.biobank.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.ualberta.med.biobank.domain.Address;
import edu.ualberta.med.biobank.domain.AliquotedSpecimen;
import edu.ualberta.med.biobank.domain.CSMUser;
import edu.ualberta.med.biobank.domain.Capacity;
import edu.ualberta.med.biobank.domain.Center;
import edu.ualberta.med.biobank.domain.Clinic;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.domain.Contact;
import edu.ualberta.med.biobank.domain.Container;
import edu.ualberta.med.biobank.domain.ContainerLabelingScheme;
import edu.ualberta.med.biobank.domain.ContainerPosition;
import edu.ualberta.med.biobank.domain.ContainerType;
import edu.ualberta.med.biobank.domain.Dispatch;
import edu.ualberta.med.biobank.domain.DispatchSpecimen;
import edu.ualberta.med.biobank.domain.EventAttr;
import edu.ualberta.med.biobank.domain.GlobalEventAttr;
import edu.ualberta.med.biobank.domain.Group;
import edu.ualberta.med.biobank.domain.Membership;
import edu.ualberta.med.biobank.domain.OriginInfo;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.domain.PermissionEnum;
import edu.ualberta.med.biobank.domain.Principal;
import edu.ualberta.med.biobank.domain.ProcessingEvent;
import edu.ualberta.med.biobank.domain.Rank;
import edu.ualberta.med.biobank.domain.Request;
import edu.ualberta.med.biobank.domain.RequestSpecimen;
import edu.ualberta.med.biobank.domain.ResearchGroup;
import edu.ualberta.med.biobank.domain.Role;
import edu.ualberta.med.biobank.domain.ShipmentInfo;
import edu.ualberta.med.biobank.domain.ShippingMethod;
import edu.ualberta.med.biobank.domain.Site;
import edu.ualberta.med.biobank.domain.SourceSpecimen;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.domain.SpecimenPosition;
import edu.ualberta.med.biobank.domain.SpecimenType;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.domain.StudyEventAttr;
import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.domain.type.LabelingLayout;
import edu.ualberta.med.biobank.domain.util.EventAttrTypeEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import net.datafaker.Faker;

/**
 * Tries to make setting up test data easier by requiring the absolute minimum amount of data and
 * remembering the last created object and using that as a default for other objects.
 *
 * @author Jonathan Ferland
 *
 */
public class Factory {

    private EntityManager em;

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(Factory.class);

    private static final Random R = new Random();

    private final NameGenerator nameGenerator;

    public static final String STUDY_EVENT_ATTR_SELECT_PERMISSIBLE = "Option1;Option2;Option3";

    private final ContainerLabelingSchemeGetter schemeGetter;

    private Site defaultSite;
    private Center defaultCenter;
    private Clinic defaultClinic;
    private ContainerType defaultTopContainerType;
    private ContainerType defaultContainerType;
    private SpecimenType defaultSourceSpecimenType;
    private SpecimenType defaultAliquotedSpecimenType;
    private Container defaultTopContainer;
    private Container defaultParentContainer;
    private Container defaultContainer;
    private Specimen defaultParentSpecimen;
    private Specimen defaultChildSpecimen;
    private ContainerLabelingScheme defaultContainerLabelingScheme;
    private Capacity defaultCapacity = new Capacity(5, 5);
    private Study defaultStudy;
    private Patient defaultPatient;
    private CollectionEvent defaultCollectionEvent;
    private OriginInfo defaultOriginInfo;
    private User defaultUser;
    private Group defaultGroup;
    private Principal defaultPrincipal;
    private Membership defaultMembership;
    private Role defaultRole;
    private Dispatch defaultDispatch;
    private DispatchSpecimen defaultDispatchSpecimen;
    private Request defaultRequest;
    private RequestSpecimen defaultRequestSpecimen;
    private ResearchGroup defaultResearchGroup;
    private ProcessingEvent defaultProcessingEvent;
    private SourceSpecimen defaultSourceSpecimen;
    private AliquotedSpecimen defaultAliquotedSpecimen;
    private Contact defaultContact;
    private Comment defaultComment;
    private ShipmentInfo defaultShipmentInfo;
    private ShippingMethod defaultShippingMethod;

    private EventAttrTypeEnum defaultEventAttrTypeEnum;
    private GlobalEventAttr defaultGlobalEventAttr;
    private StudyEventAttr defaultStudyEventAttr;
    private EventAttr defaultCeventEventAttr;

    // https://www.datafaker.net/documentation/getting-started/
    private final Faker faker;

    public Factory(EntityManager em) {
        this.em = em;
        this.faker = new Faker();
        this.nameGenerator = new NameGenerator(new BigInteger(130, R).toString(32));
        this.schemeGetter = new ContainerLabelingSchemeGetter();
    }

    /**
     * Made this public so that it can be used by other helpers. For example
     * {@link SpecimenBatchOpPojoHelper} uses this to generate strings used as values for
     * attributes.
     */
    public NameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public Faker getFaker() {
        return faker;
    }

    public Comment getDefaultComment() {
        if (defaultComment == null) {
            defaultComment = createComment();
        }
        return defaultComment;
    }

    public void setDefaultComment(Comment defaultComment) {
        this.defaultComment = defaultComment;
    }

    public Contact getDefaultContact() {
        if (defaultContact == null) {
            defaultContact = createContact();
        }
        return defaultContact;
    }

    public void setDefaultContact(Contact defaultContact) {
        this.defaultContact = defaultContact;
    }

    public Clinic getDefaultClinic() {
        if (defaultClinic == null) {
            defaultClinic = createClinic();
        }
        return defaultClinic;
    }

    public void setDefaultClinic(Clinic defaultClinic) {
        this.defaultClinic = defaultClinic;
    }

    public SourceSpecimen getDefaultSourceSpecimen() {
        if (defaultSourceSpecimen == null) {
            defaultSourceSpecimen = createSourceSpecimen();
        }
        return defaultSourceSpecimen;
    }

    public void setDefaultSourceSpecimen(SourceSpecimen defaultSourceSpecimen) {
        this.defaultSourceSpecimen = defaultSourceSpecimen;
        this.defaultSourceSpecimenType = defaultSourceSpecimen.getSpecimenType();
    }

    public AliquotedSpecimen getDefaultAliquotedSpecimen() {
        if (defaultAliquotedSpecimen == null) {
            defaultAliquotedSpecimen = createAliquotedSpecimen();
        }
        this.defaultAliquotedSpecimenType = defaultAliquotedSpecimen.getSpecimenType();
        return defaultAliquotedSpecimen;
    }

    public void setDefaultAliquotedSpecimen(AliquotedSpecimen defaultAliquotedSpecimen) {
        this.defaultAliquotedSpecimen = defaultAliquotedSpecimen;
    }

    public ProcessingEvent getDefaultProcessingEvent() {
        if (defaultProcessingEvent == null) {
            defaultProcessingEvent = createProcessingEvent();
        }
        return defaultProcessingEvent;
    }

    public void setDefaultProcessingEvent(ProcessingEvent defaultProcessingEvent) {
        this.defaultProcessingEvent = defaultProcessingEvent;
    }

    public ResearchGroup getDefaultResearchGroup() {
        if (defaultResearchGroup == null) {
            defaultResearchGroup = createResearchGroup();
        }
        return defaultResearchGroup;
    }

    public void setDefaultResearchGroup(ResearchGroup researchGroup) {
        this.defaultResearchGroup = researchGroup;
    }

    public Request getDefaultRequest() {
        if (defaultRequest == null) {
            defaultRequest = createRequest();
        }
        return defaultRequest;
    }

    public void setDefaultRequest(Request request) {
        this.defaultRequest = request;
    }

    public RequestSpecimen getDefaultRequestSpecimen() {
        if (defaultRequestSpecimen == null) {
            defaultRequestSpecimen = createRequestSpecimen();
        }
        return defaultRequestSpecimen;
    }

    public void setDefaultRequestSpecimen(RequestSpecimen requiestSpecimen) {
        this.defaultRequestSpecimen = requiestSpecimen;
    }

    public Dispatch getDefaultDispatch() {
        if (defaultDispatch == null) {
            defaultDispatch = createDispatch(getDefaultCenter(), createSite());
        }
        return defaultDispatch;
    }

    public void setDefaultDispatch(Dispatch defaultDispatch) {
        this.defaultDispatch = defaultDispatch;
    }

    public DispatchSpecimen getDefaultDispatchSpecimen() {
        if (defaultDispatchSpecimen == null) {
            defaultDispatchSpecimen = createDispatchSpecimen();
        }
        return defaultDispatchSpecimen;
    }

    public void setDefaultDispatchSpecimen(DispatchSpecimen defaultDispatchSpecimen) {
        this.defaultDispatchSpecimen = defaultDispatchSpecimen;
    }

    public Role getDefaultRole() {
        if (defaultRole == null) {
            defaultRole = createRole();
        }
        return defaultRole;
    }

    public void setDefaultRole(Role defaultRole) {
        this.defaultRole = defaultRole;
    }

    public Center getDefaultCenter() {
        if (defaultCenter == null) {
            defaultCenter = createSite();
        }
        return defaultCenter;
    }

    public void setDefaultCenter(Center defaultCenter) {
        this.defaultCenter = defaultCenter;
    }

    public Group getDefaultGroup() {
        if (defaultGroup == null) {
            defaultGroup = createGroup();
        }
        return defaultGroup;
    }

    public void setDefaultGroup(Group defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public Principal getDefaultPrincipal() {
        if (defaultPrincipal == null) {
            defaultPrincipal = createUser();
        }
        return defaultPrincipal;
    }

    public void setDefaultPrincipal(Principal defaultPrincipal) {
        this.defaultPrincipal = defaultPrincipal;
    }

    public User getDefaultUser() {
        if (defaultUser == null) {
            defaultUser = createUser();
        }
        return defaultUser;
    }

    public void setDefaultUser(User defaultUser) {
        this.defaultUser = defaultUser;
    }

    public Membership getDefaultMembership() {
        if (defaultMembership == null) {
            defaultMembership = createMembership();
        }
        return defaultMembership;
    }

    public void setDefaultMembership(Membership defaultMembership) {
        this.defaultMembership = defaultMembership;
    }

    public Container getDefaultParentContainer() {
        return defaultParentContainer;
    }

    public void setDefaultParentContainer(Container defaultParentContainer) {
        this.defaultParentContainer = defaultParentContainer;
    }

    public Container getDefaultTopContainer() {
        if (defaultTopContainer == null) {
            defaultTopContainer = createTopContainer();
        }
        return defaultTopContainer;
    }

    public void setDefaultTopContainer(Container defaultTopContainer) {
        this.defaultTopContainer = defaultTopContainer;
    }

    public ContainerType getDefaultTopContainerType() {
        if (defaultTopContainerType == null) {
            defaultTopContainerType = createTopContainerType();
        }
        return defaultTopContainerType;
    }

    public void setDefaultTopContainerType(ContainerType defaultTopContainerType) {
        this.defaultTopContainerType = defaultTopContainerType;
    }

    public OriginInfo getDefaultOriginInfo() {
        if (defaultOriginInfo == null) {
            defaultOriginInfo = createOriginInfo();
        }
        return defaultOriginInfo;
    }

    public void setDefaultOriginInfo(OriginInfo defaultOriginInfo) {
        this.defaultOriginInfo = defaultOriginInfo;
    }

    public Study getDefaultStudy() {
        if (defaultStudy == null) {
            defaultStudy = createStudy();
        }
        return defaultStudy;
    }

    public void setDefaultStudy(Study defaultStudy) {
        this.defaultStudy = defaultStudy;
    }

    public Patient getDefaultPatient() {
        if (defaultPatient == null) {
            defaultPatient = createPatient();
        }
        return defaultPatient;
    }

    public void setDefaultPatient(Patient defaultPatient) {
        this.defaultPatient = defaultPatient;
    }

    public CollectionEvent getDefaultCollectionEvent() {
        if (defaultCollectionEvent == null) {
            defaultCollectionEvent = createCollectionEvent();
        }
        return defaultCollectionEvent;
    }

    public void setDefaultCollectionEvent(CollectionEvent defaultCollectionEvent) {
        this.defaultCollectionEvent = defaultCollectionEvent;
    }

    public Site getDefaultSite() {
        if (defaultSite == null) {
            defaultSite = createSite();
        }
        return defaultSite;
    }

    public void setDefaultSite(Site defaultSite) {
        this.defaultSite = defaultSite;
    }

    public ContainerType getDefaultContainerType() {
        if (defaultContainerType == null) {
            defaultContainerType = createContainerType();
        }
        return defaultContainerType;
    }

    public void setDefaultContainerType(ContainerType defaultContainerType) {
        this.defaultContainerType = defaultContainerType;
    }

    public Container getDefaultContainer() {
        if (defaultContainer == null) {
            defaultContainer = createContainer();
        }
        return defaultContainer;
    }

    public void setDefaultContainer(Container defaultContainer) {
        this.defaultContainer = defaultContainer;
    }

    public ContainerLabelingScheme getDefaultContainerLabelingScheme() {
        if (defaultContainerLabelingScheme == null) {
            defaultContainerLabelingScheme = getScheme().getSbs();
        }
        return defaultContainerLabelingScheme;
    }

    public void setDefaultContainerLabelingScheme(ContainerLabelingScheme defaultContainerLabelingScheme) {
        this.defaultContainerLabelingScheme = defaultContainerLabelingScheme;
    }

    public Capacity getDefaultCapacity() {
        if (defaultCapacity == null) {
            defaultCapacity = createCapacity();
        }
        return defaultCapacity;
    }

    public void setDefaultCapacity(Capacity defaultCapacity) {
        this.defaultCapacity = defaultCapacity;
    }

    public SpecimenType getDefaultSourceSpecimenType() {
        if (defaultSourceSpecimenType == null) {
            defaultSourceSpecimenType = createSpecimenType();
        }
        return defaultSourceSpecimenType;
    }

    public void setDefaultSourceSpecimenType(SpecimenType defaultSpecimenType) {
        this.defaultSourceSpecimenType = defaultSpecimenType;
    }

    public SpecimenType getDefaultAliquotedSpecimenType() {
        if (defaultAliquotedSpecimenType == null) {
            defaultAliquotedSpecimenType = createSpecimenType();
        }
        return defaultAliquotedSpecimenType;
    }

    public void setDefaultAliquotedSpecimenType(SpecimenType defaultSpecimenType) {
        this.defaultAliquotedSpecimenType = defaultSpecimenType;
    }

    public Specimen getDefaultParentSpecimen() {
        if (defaultParentSpecimen == null) {
            defaultParentSpecimen = createParentSpecimen();
        }
        return defaultParentSpecimen;
    }

    public void setDefaultParentSpecimen(Specimen defaultSpecimen) {
        this.defaultParentSpecimen = defaultSpecimen;
    }

    public Specimen getDefaultChildSpecimen() {
        if (defaultChildSpecimen == null) {
            defaultChildSpecimen = createChildSpecimen();
        }
        return defaultChildSpecimen;
    }

    public void setDefaultChildSpecimen(Specimen defaultSpecimen) {
        this.defaultChildSpecimen = defaultSpecimen;
    }

    public ShipmentInfo getDefaultShipmentInfo() {
        return defaultShipmentInfo;
    }

    public void setDefaultShipmentInfo(ShipmentInfo defaultShipmentInfo) {
        this.defaultShipmentInfo = defaultShipmentInfo;
    }

    public ShippingMethod getDefaultShippingMethod() {
        return defaultShippingMethod;
    }

    public void setDefaultShippingMethod(ShippingMethod shippingMethod) {
        this.defaultShippingMethod = shippingMethod;
    }

    public Capacity createCapacity() {
        Capacity capacity = new Capacity();
        capacity.setRowCapacity(8);
        capacity.setRowCapacity(12);
        return capacity;
    }

    public Comment createComment() {
        Comment comment = new Comment();
        comment.setUser(getDefaultUser());
        comment.setCreatedAt(new Date());
        comment.setMessage(faker.lorem().paragraph());

        setDefaultComment(comment);
        em.persist(comment);
        em.flush();
        return comment;
    }

    public Contact createContact() {
        String name = faker.name().fullName();
        Contact contact = new Contact();

        Clinic clinic = getDefaultClinic();
        contact.setClinic(clinic);
        contact.setName(name);

        setDefaultContact(contact);

        clinic.getContacts().add(contact);
        em.merge(clinic);
        em.flush();
        return contact;
    }

    public Clinic createClinic() {
        // Use Center.class because the name must be unique on Center
        String name = nameGenerator.next(Center.class);

        Clinic clinic = new Clinic();
        clinic.setName(name);
        clinic.setNameShort(name);

        Address address = clinic.getAddress();
        address.setCity(faker.address().city());
        em.persist(address);

        setDefaultCenter(clinic);
        setDefaultClinic(clinic);
        em.persist(clinic);
        em.flush();
        return clinic;
    }

    public SourceSpecimen createSourceSpecimen() {
        SourceSpecimen sourceSpecimen = new SourceSpecimen();
        sourceSpecimen.setStudy(getDefaultStudy());
        sourceSpecimen.setSpecimenType(getDefaultSourceSpecimenType());

        getDefaultStudy().getSourceSpecimens().add(sourceSpecimen);
        setDefaultSourceSpecimen(sourceSpecimen);
        em.persist(sourceSpecimen);
        em.flush();
        return sourceSpecimen;
    }

    public AliquotedSpecimen createAliquotedSpecimen() {
        AliquotedSpecimen aliquotedSpecimen = new AliquotedSpecimen();
        aliquotedSpecimen.setStudy(getDefaultStudy());
        aliquotedSpecimen.setVolume(new BigDecimal("1.00"));
        aliquotedSpecimen.setQuantity(1);
        aliquotedSpecimen.setSpecimenType(getDefaultSourceSpecimenType());

        getDefaultStudy().getAliquotedSpecimens().add(aliquotedSpecimen);
        setDefaultAliquotedSpecimen(aliquotedSpecimen);
        em.persist(aliquotedSpecimen);
        em.flush();
        return aliquotedSpecimen;
    }

    public ProcessingEvent createProcessingEvent() {
        String worksheet = nameGenerator.next(ProcessingEvent.class);

        ProcessingEvent processingEvent = new ProcessingEvent();
        processingEvent.setWorksheet(worksheet);
        processingEvent.setCenter(getDefaultCenter());
        processingEvent.setCreatedAt(new Date());

        setDefaultProcessingEvent(processingEvent);
        em.persist(processingEvent);
        em.flush();
        return processingEvent;
    }

    public ResearchGroup createResearchGroup() {
        // Use Center.class because the name must be unique on Center
        String name = nameGenerator.next(Center.class);

        ResearchGroup researchGroup = new ResearchGroup();
        Address address = researchGroup.getAddress();
        address.setCity(faker.address().city());
        em.persist(address);

        researchGroup.setName(name);
        researchGroup.setNameShort(name);
        researchGroup.setStudy(getDefaultStudy());

        setDefaultCenter(researchGroup);
        setDefaultResearchGroup(researchGroup);
        em.persist(researchGroup);
        em.flush();
        return researchGroup;
    }

    public Request createRequest() {
        Request request = new Request();

        Address address = request.getAddress();
        address.setCity(faker.address().city());
        em.persist(address);

        request.setCreatedAt(new Date());
        request.setResearchGroup(getDefaultResearchGroup());

        setDefaultRequest(request);
        em.persist(request);
        em.flush();
        return request;
    }

    public RequestSpecimen createRequestSpecimen() {
        RequestSpecimen requestSpecimen = new RequestSpecimen();
        requestSpecimen.setRequest(getDefaultRequest());

        Specimen specimen = createChildSpecimen();
        requestSpecimen.setSpecimen(specimen);

        setDefaultRequestSpecimen(requestSpecimen);
        em.persist(requestSpecimen);
        em.flush();
        return requestSpecimen;
    }

    public Dispatch createDispatch(Center sender, Center receiver) {
        Dispatch dispatch = new Dispatch();

        dispatch.setSenderCenter(sender);
        dispatch.setReceiverCenter(receiver);

        setDefaultDispatch(dispatch);
        em.persist(dispatch);
        em.flush();
        return dispatch;
    }

    public DispatchSpecimen createDispatchSpecimen() {
        DispatchSpecimen dispatchSpecimen = new DispatchSpecimen();
        dispatchSpecimen.setDispatch(getDefaultDispatch());

        Specimen specimen = createParentSpecimen();
        dispatchSpecimen.setSpecimen(specimen);

        em.persist(dispatchSpecimen);
        em.flush();
        return dispatchSpecimen;
    }

    public Site createSite() {
        // Use Center.class because the name must be unique on Center
        String name = nameGenerator.next(Center.class);
        Site site = new Site();
        site.setName(name);
        site.setNameShort(name);

        Address address = site.getAddress();
        address.setCity(faker.address().city());
        em.persist(address);

        setDefaultSite(site);
        setDefaultCenter(site);
        em.persist(site);
        em.flush();
        return site;
    }

    public ContainerLabelingScheme createContainerLabelingScheme() {
        String name = nameGenerator.next(ContainerLabelingScheme.class);

        ContainerLabelingScheme scheme = new ContainerLabelingScheme();
        scheme.setName(name);
        scheme.setMinChars(1);
        scheme.setMaxChars(1);
        scheme.setMaxCapacity(1);
        setDefaultContainerLabelingScheme(scheme);
        em.persist(scheme);
        em.flush();
        return scheme;
    }

    public ContainerType createContainerType() {
        String name = nameGenerator.next(ContainerType.class);

        ContainerType containerType = new ContainerType();
        containerType.setName(name);
        containerType.setNameShort(name);
        containerType.setSite(getDefaultSite());
        containerType.setCapacity(new Capacity(getDefaultCapacity()));
        containerType.setChildLabelingScheme(getDefaultContainerLabelingScheme());
        containerType.setLabelingLayout(LabelingLayout.VERTICAL);

        setDefaultContainerType(containerType);
        em.persist(containerType);
        em.flush();
        return containerType;
    }

    public ContainerType createTopContainerType() {
        ContainerType oldDefaultContainerType = getDefaultContainerType();
        ContainerType topContainerType = createContainerType();
        topContainerType.setTopLevel(true);

        // restore the old, non-topLevel ContainerType
        setDefaultContainerType(oldDefaultContainerType);
        setDefaultTopContainerType(topContainerType);
        em.merge(topContainerType);
        em.flush();
        return topContainerType;
    }

    public Container createContainer() {
        String label = nameGenerator.next(Container.class);

        Container container = new Container();
        container.setSite(getDefaultSite());
        if (!getDefaultTopContainerType().getSite().equals(container.getSite())) {
            // make sure sites match
            createTopContainerType();
        }

        // initialise container type to something for now, it is assigned the correct
        // value below
        container.setContainerType(getDefaultTopContainerType());
        container.setLabel(label);
        container.setTopContainer(container);

        Container parentContainer = getDefaultParentContainer();
        if (parentContainer != null) {
            ContainerType containerType = getDefaultContainerType();
            if (!containerType.getSite().equals(container.getSite())) {
                // make sure sites match
                containerType = createContainerType();
            }
            container.setContainerType(containerType);

            ContainerType parentCt = parentContainer.getContainerType();
            parentCt.getChildContainerTypes().add(containerType);
            containerType.getParentContainerTypes().add(parentCt);

            em.merge(parentCt);
            em.flush();

            Integer numChildren = parentContainer.getChildPositions().size();
            Integer row = numChildren / parentCt.getRowCapacity();
            Integer col = numChildren % parentCt.getColCapacity();

            ContainerPosition cp = new ContainerPosition();
            cp.setRow(row);
            cp.setCol(col);

            cp.setContainer(container);
            container.setPosition(cp);

            cp.setParentContainer(parentContainer);
            parentContainer.getChildPositions().add(cp);
            container.setTopContainer(parentContainer.getTopContainer());
        }

        setDefaultContainer(container);
        em.persist(container);
        em.flush();
        return container;
    }

    public Container createTopContainer() {
        setDefaultParentContainer(null);
        Container topContainer = createContainer();

        setDefaultTopContainer(topContainer);
        setDefaultParentContainer(topContainer);
        em.merge(topContainer);
        em.flush();
        return topContainer;
    }

    public Container createParentContainer() {
        Container parentContainer = createContainer();
        setDefaultParentContainer(parentContainer);
        em.merge(parentContainer);
        em.flush();
        return parentContainer;
    }

    public SpecimenType createSpecimenType() {
        String name = nameGenerator.next(SpecimenType.class);

        SpecimenType specimenType = new SpecimenType();
        specimenType.setName(name);
        specimenType.setNameShort(name);

        setDefaultSourceSpecimenType(specimenType);
        em.persist(specimenType);
        em.flush();
        return specimenType;
    }

    public Specimen createSpecimen() {
        String name = nameGenerator.next(Specimen.class);

        Specimen specimen = new Specimen();
        specimen.setInventoryId(name);
        specimen.setCurrentCenter(getDefaultCenter());
        specimen.setCollectionEvent(getDefaultCollectionEvent());
        specimen.setOriginInfo(getDefaultOriginInfo());
        specimen.setCreatedAt(new Date());

        return specimen;
    }

    public Specimen createMicroplateSpecimen(String position) {
        Specimen parentSpecimen = createSpecimen();
        parentSpecimen.setInventoryId("##" + getDefaultContainer().getLabel() + "##" + position);
        parentSpecimen.setSpecimenType(getDefaultSourceSpecimenType());

        CollectionEvent cevent = getDefaultCollectionEvent();
        parentSpecimen.setOriginalCollectionEvent(cevent);
        cevent.getOriginalSpecimens().add(parentSpecimen);
        cevent.getAllSpecimens().add(parentSpecimen);

        em.persist(parentSpecimen);
        em.flush();

        // setDefaultParentSpecimen(parentSpecimen);
        return parentSpecimen;
    }

    public Specimen createParentSpecimen() {
        Specimen parentSpecimen = createSpecimen();
        parentSpecimen.setSpecimenType(getDefaultSourceSpecimenType());

        CollectionEvent cevent = getDefaultCollectionEvent();
        parentSpecimen.setOriginalCollectionEvent(cevent);
        cevent.getOriginalSpecimens().add(parentSpecimen);
        cevent.getAllSpecimens().add(parentSpecimen);

        em.persist(parentSpecimen);
        em.flush();

        setDefaultParentSpecimen(parentSpecimen);
        return parentSpecimen;
    }

    public Specimen createChildSpecimen() {
        Specimen childSpecimen = createSpecimen();
        childSpecimen.setSpecimenType(getDefaultAliquotedSpecimenType());

        Specimen parentSpecimen = getDefaultParentSpecimen();
        childSpecimen.setParentSpecimen(parentSpecimen);
        childSpecimen.setCollectionEvent(parentSpecimen.getCollectionEvent());

        Specimen topSpecimen = parentSpecimen.getTopSpecimen();
        if (topSpecimen != null) {
            childSpecimen.setTopSpecimen(topSpecimen);
        } else {
            childSpecimen.setTopSpecimen(parentSpecimen);
        }

        ProcessingEvent pevent = getDefaultProcessingEvent();
        parentSpecimen.setProcessingEvent(pevent);
        pevent.getSpecimens().add(childSpecimen);

        childSpecimen.setQuantity(getDefaultAliquotedSpecimen().getVolume());
        parentSpecimen.getCollectionEvent().getAllSpecimens().add(childSpecimen);

        em.persist(childSpecimen);
        em.flush();

        setDefaultChildSpecimen(childSpecimen);
        return childSpecimen;
    }

    public Specimen createPositionedSpecimen() {
        Specimen assignedSpecimen = createChildSpecimen();

        Container parentContainer = getDefaultContainer();
        ContainerType parentCt = parentContainer.getContainerType();

        parentCt.getSpecimenTypes().add(assignedSpecimen.getSpecimenType());

        em.merge(parentCt);
        em.flush();

        Integer numSpecimens = parentContainer.getSpecimenPositions().size();
        Integer row = numSpecimens / parentCt.getRowCapacity();
        Integer col = numSpecimens % parentCt.getColCapacity();

        SpecimenPosition sp = new SpecimenPosition();
        sp.setRow(row);
        sp.setCol(col);
        sp.setPositionString("asdf"); // TODO: set this right

        sp.setSpecimen(assignedSpecimen);
        assignedSpecimen.setSpecimenPosition(sp);

        sp.setContainer(parentContainer);
        parentContainer.getSpecimenPositions().add(sp);

        em.merge(assignedSpecimen);
        em.flush();
        return assignedSpecimen;
    }

    public Study createStudy() {
        String name = nameGenerator.next(Study.class);

        Study study = new Study();
        study.setName(name);
        study.setNameShort(name);

        setDefaultStudy(study);
        em.persist(study);
        em.flush();
        return study;
    }

    public CollectionEvent createCollectionEvent() {
        CollectionEvent collectionEvent = new CollectionEvent();

        // make sure the patient has this collection events so we can use the
        // set to generate a sensible default visit number.
        Patient patient = getDefaultPatient();
        collectionEvent.setPatient(patient);

        int numCEs = patient.getCollectionEvents().size();
        collectionEvent.setVisitNumber(numCEs + 1);
        patient.getCollectionEvents().add(collectionEvent);

        setDefaultCollectionEvent(collectionEvent);
        em.persist(collectionEvent);
        em.merge(patient);
        em.flush();
        return collectionEvent;
    }

    public Patient createPatient() {
        String name = nameGenerator.next(Patient.class);

        Patient patient = new Patient();
        patient.setPnumber(name);
        patient.setStudy(getDefaultStudy());
        patient.setCreatedAt(new Date());

        setDefaultPatient(patient);
        em.persist(patient);
        em.flush();
        return patient;
    }

    public OriginInfo createOriginInfo() {
        OriginInfo originInfo = new OriginInfo();
        originInfo.setCenter(getDefaultCenter());

        ShipmentInfo shipmentInfo = getDefaultShipmentInfo();
        if (shipmentInfo != null) {
            originInfo.setShipmentInfo(shipmentInfo);
            originInfo.setReceiverCenter(getDefaultSite());
        }

        setDefaultOriginInfo(originInfo);
        em.persist(originInfo);
        em.flush();
        return originInfo;
    }

    public ShipmentInfo createShipmentInfo() {
        String waybill = nameGenerator.next(ShipmentInfo.class);

        ShipmentInfo shipmentInfo = new ShipmentInfo();

        // set packed at to 2 days ago
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -2);

        shipmentInfo.setPackedAt(cal.getTime());
        shipmentInfo.setReceivedAt(new Date());
        shipmentInfo.setWaybill(waybill);
        shipmentInfo.setBoxNumber("");
        shipmentInfo.setShippingMethod(createShippingMethod());

        setDefaultShipmentInfo(shipmentInfo);
        em.persist(shipmentInfo);
        em.flush();

        return shipmentInfo;
    }

    public ShippingMethod createShippingMethod() {
        String name = nameGenerator.next(ShippingMethod.class);
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setName(name);

        setDefaultShippingMethod(shippingMethod);
        em.persist(shippingMethod);
        em.flush();
        return shippingMethod;
    }

    public EventAttrTypeEnum getDefaultEventAttrTypeEnum() {
        if (defaultEventAttrTypeEnum == null) {
            defaultEventAttrTypeEnum = EventAttrTypeEnum.TEXT;
        }
        return defaultEventAttrTypeEnum;
    }

    public void setDefaultEventAttrTypeEnum(EventAttrTypeEnum eventAttrTypeEnum) {
        defaultEventAttrTypeEnum = eventAttrTypeEnum;
    }

    public GlobalEventAttr getDefaultGlobalEventAttr() {
        getDefaultEventAttrTypeEnum();
        List<GlobalEventAttr> list = em
            .createQuery("select ge from GlobalEventAttr", GlobalEventAttr.class)
            .getResultList();

        for (GlobalEventAttr gea : list) {
            String name = gea.getEventAttrType().getName();
            if (defaultEventAttrTypeEnum.getName().equals(name)) {
                defaultGlobalEventAttr = gea;
            }
        }
        if (
            (defaultGlobalEventAttr == null) ||
            !defaultEventAttrTypeEnum.getName().equals(defaultGlobalEventAttr.getEventAttrType().getName())
        ) {
            throw new IllegalStateException("could not find global event attr type");
        }
        return defaultGlobalEventAttr;
    }

    public void setDefaultGlobalEventAttr(GlobalEventAttr globalEventAttr) {
        this.defaultGlobalEventAttr = globalEventAttr;
    }

    public StudyEventAttr getDefautlStudyEventAttr() {
        if (defaultStudyEventAttr == null) {
            defaultStudyEventAttr = createStudyEventAttr();
        }
        return defaultStudyEventAttr;
    }

    public void setDefaultStudyEventAttr(StudyEventAttr studyEventAttr) {
        this.defaultStudyEventAttr = studyEventAttr;
    }

    public StudyEventAttr createStudyEventAttr() {
        String permissible = null;
        StudyEventAttr studyEventAttr = new StudyEventAttr();
        studyEventAttr.setStudy(getDefaultStudy());
        studyEventAttr.setGlobalEventAttr(getDefaultGlobalEventAttr());

        switch (getDefaultEventAttrTypeEnum()) {
            case SELECT_SINGLE:
            case SELECT_MULTIPLE:
                permissible = STUDY_EVENT_ATTR_SELECT_PERMISSIBLE;
                break;
            case NUMBER:
            case DATE_TIME:
            case TEXT:
                // do nothing
                break;
            default:
                throw new IllegalStateException("invalid event attribute type: " + getDefaultEventAttrTypeEnum());
        }

        studyEventAttr.setPermissible(permissible);

        setDefaultStudyEventAttr(studyEventAttr);
        getDefaultStudy().getStudyEventAttrs().add(studyEventAttr);
        em.persist(studyEventAttr);
        em.flush();
        return studyEventAttr;
    }

    public EventAttr getDefautlCeventEventAttr() {
        if (defaultCeventEventAttr == null) {
            defaultCeventEventAttr = createCeventEventAttr();
        }
        return defaultCeventEventAttr;
    }

    public void setDefaultCeventEventAttr(EventAttr ceventEventAttr) {
        this.defaultCeventEventAttr = ceventEventAttr;
    }

    public EventAttr createCeventEventAttr() {
        String value = nameGenerator.next(EventAttr.class);
        EventAttr ceventEventAttr = new EventAttr();
        StudyEventAttr studyEventAttr = getDefautlStudyEventAttr();
        ceventEventAttr.setStudyEventAttr(studyEventAttr);
        ceventEventAttr.setCollectionEvent(getDefaultCollectionEvent());

        String eventAttrTypeName = studyEventAttr.getGlobalEventAttr().getEventAttrType().getName();
        switch (EventAttrTypeEnum.getEventAttrType(eventAttrTypeName)) {
            case SELECT_SINGLE:
                value = defaultStudyEventAttr.getPermissible().split(";")[0];
                break;
            case SELECT_MULTIPLE:
                value =
                    defaultStudyEventAttr.getPermissible().split(";")[0] +
                    defaultStudyEventAttr.getPermissible().split(";")[1];
                break;
            case NUMBER:
                value = "1.0";
                break;
            case DATE_TIME:
                value = faker.date().past(1000, TimeUnit.DAYS, "yyyy-MM-dd hh:mm");
                break;
            case TEXT:
                // do nothing
                break;
            default:
                throw new IllegalStateException("invalid event attribute type: " + eventAttrTypeName);
        }

        ceventEventAttr.setValue(value);

        setDefaultCeventEventAttr(ceventEventAttr);
        em.persist(ceventEventAttr);
        em.flush();
        return ceventEventAttr;
    }

    public User createUser() {
        String name = nameGenerator.next(User.class);

        CSMUser csmUser = new CSMUser();
        csmUser.setLoginName(faker.name().lastName());
        csmUser.setMigratedFlag(true);
        csmUser.setFirstName(faker.name().firstName());
        csmUser.setLastName(faker.name().lastName());
        csmUser.setUpdateDate(new Date());
        em.persist(csmUser);

        User user = new User();
        user.setLogin(name);
        user.setEmail(name);
        user.setFullName(faker.name().fullName());
        user.setCsmUser(csmUser);
        em.persist(user);
        em.flush();

        setDefaultUser(user);
        setDefaultPrincipal(user);
        em.persist(user);
        em.flush();
        return user;
    }

    public Group createGroup() {
        String name = nameGenerator.next(Group.class);

        Group group = new Group();
        group.setName(name);
        group.setDescription(name);

        // temporary membership, for creating
        Membership m = new Membership();
        m.setPrincipal(group);
        group.getMemberships().add(m);

        setDefaultGroup(group);
        setDefaultPrincipal(group);

        em.persist(group);
        em.flush();

        // remove membership
        group.getMemberships().clear();
        em.remove(m);

        em.merge(group);
        em.flush();
        return group;
    }

    public enum Domain {
        GLOBAL,
        CENTER,
        STUDY,
        CENTER_STUDY
    }

    public Membership createMembership() {
        return buildMembership().create();
    }

    public Membership createMembership(Domain domain, Rank rank) {
        Membership membership = new Membership();

        membership.getDomain().setAllCenters(true);
        membership.getDomain().setAllStudies(true);

        if (domain == Domain.CENTER || domain == Domain.CENTER_STUDY) {
            membership.getDomain().getCenters().add(getDefaultCenter());
            membership.getDomain().setAllCenters(false);
        }
        if (domain == Domain.STUDY || domain == Domain.CENTER_STUDY) {
            membership.getDomain().getStudies().add(getDefaultStudy());
            membership.getDomain().setAllStudies(false);
        }

        Principal p = getDefaultPrincipal();
        p.getMemberships().add(membership);
        membership.setPrincipal(p);

        membership.setUserManager(rank.isGe(Rank.MANAGER) ? true : false);

        if (Rank.MANAGER.equals(rank)) {
            // needs at least one permission or role to manage
            membership.getPermissions().add(PermissionEnum.CLINIC_READ);
        }

        setDefaultMembership(membership);
        em.persist(membership);
        em.flush();
        return membership;
    }

    public static class MembershipBuilder {

        private final Factory factory;
        private boolean userManager = false;
        private boolean everyPermission = false;
        private Quantity centerQuantity = Quantity.ONE;
        private Quantity studyQuantity = Quantity.ONE;

        public MembershipBuilder(Factory factory) {
            this.factory = factory;
        }

        public MembershipBuilder setCenter() {
            centerQuantity = Quantity.ONE;
            return this;
        }

        public MembershipBuilder setStudy() {
            studyQuantity = Quantity.ONE;
            return this;
        }

        public MembershipBuilder setGlobal() {
            centerQuantity = Quantity.ALL;
            studyQuantity = Quantity.ALL;
            return this;
        }

        public MembershipBuilder setAllCenters() {
            centerQuantity = Quantity.ALL;
            return this;
        }

        public MembershipBuilder setAllStudies() {
            studyQuantity = Quantity.ALL;
            return this;
        }

        public MembershipBuilder setUserManager(boolean userManager) {
            this.userManager = userManager;
            if (userManager) setEveryPermission(true);
            return this;
        }

        public MembershipBuilder setEveryPermission(boolean everyPermission) {
            this.everyPermission = everyPermission;
            if (!everyPermission) setUserManager(false);
            return this;
        }

        public Membership create() {
            return factory.createMembership(this);
        }

        enum Quantity {
            NONE,
            ONE,
            ALL
        }
    }

    public MembershipBuilder buildMembership() {
        return new MembershipBuilder(this);
    }

    public Membership createMembership(MembershipBuilder builder) {
        Membership membership = new Membership();

        switch (builder.centerQuantity) {
            case NONE:
                break;
            case ONE:
                membership.getDomain().getCenters().add(getDefaultCenter());
                break;
            case ALL:
                membership.getDomain().setAllCenters(true);
                break;
        }

        switch (builder.studyQuantity) {
            case NONE:
                break;
            case ONE:
                membership.getDomain().getStudies().add(getDefaultStudy());
                break;
            case ALL:
                membership.getDomain().setAllStudies(true);
                break;
        }

        Principal p = getDefaultPrincipal();
        p.getMemberships().add(membership);
        membership.setPrincipal(p);

        membership.setEveryPermission(builder.everyPermission);
        membership.setUserManager(builder.userManager);

        setDefaultMembership(membership);
        em.persist(membership);
        em.flush();
        return membership;
    }

    public Role createRole() {
        String name = nameGenerator.next(Role.class);

        Role role = new Role();

        role.setName(name);

        setDefaultRole(role);
        em.persist(role);
        em.flush();
        return role;
    }

    public ContainerLabelingSchemeGetter getScheme() {
        return schemeGetter;
    }

    public class ContainerLabelingSchemeGetter {

        public ContainerLabelingScheme getSbs() {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            var cq = cb.createQuery(ContainerLabelingScheme.class);
            var scheme = cq.from(ContainerLabelingScheme.class);
            var predicate = cb.equal(scheme.get("name"), "SBS Standard");
            cq.where(predicate);
            var query = em.createQuery(cq);
            return query.getSingleResult();
        }

        public ContainerLabelingScheme get2CharAlphabetic() {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            var cq = cb.createQuery(ContainerLabelingScheme.class);
            var scheme = cq.from(ContainerLabelingScheme.class);
            var predicate = cb.equal(scheme.get("name"), "CBSR 2 char alphabetic");
            cq.where(predicate);
            var query = em.createQuery(cq);
            return query.getSingleResult();
        }
    }

    public String getName(Class<?> klazz) {
        return nameGenerator.next(klazz);
    }
}
