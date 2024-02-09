-- select *
-- from container
-- left join container_type on container_type.id=container.CONTAINER_TYPE_ID
-- where container.TOP_CONTAINER_ID=container.ID;

-- select * from processing_event where WORKSHEET = '2 10031';

-- select * from specimen where id=1398769;

-- select
--     spc.id specimenId,
--     spc.PARENT_SPECIMEN_ID as parentSpecimenId,
--     spc.INVENTORY_ID as specimenInventoryId,
--     spc.CREATED_AT specimenTimeDrawn,
--     spc.QUANTITY as specimenQuantity,
--     spc.ACTIVITY_STATUS_ID as specimenActivityStatusId,
--     st.id as specimenTypeId,
--     st.NAME_SHORT as specimenTypeNameShort,
--     spc.PROCESSING_EVENT_ID,
--     pe.WORKSHEET as worksheet,
--     oi.CENTER_ID as originCenterId,
--     c.NAME_SHORT as originCenterNameShort,
--     cc.ID as currentCenterId,
--     cc.NAME_SHORT as currentCenterNameShort,
--     if(spccmt.SPECIMEN_ID != null, "Y", "N") hasSpecimenComments,
--     concat(container.label, "-", spos.POSITION_STRING, " (", top_container_type.NAME_SHORT, ")") as `position`,
--     p.PNUMBER as pnumber,
--     study.id as studyId,
--     study.NAME_SHORT as studyNameShort
-- from specimen spc
-- left join specimen parent_spc on parent_spc.id=spc.PARENT_SPECIMEN_ID
-- left join collection_event ce on ce.id=parent_spc.COLLECTION_EVENT_ID
-- left join patient p on p.id=ce.PATIENT_ID
-- left join study on study.id=p.STUDY_ID
-- left join specimen_type st on st.ID = spc.SPECIMEN_TYPE_ID
-- left join origin_info oi on oi.ID = spc.ORIGIN_INFO_ID
-- left join processing_event pe on pe.id=parent_spc.PROCESSING_EVENT_ID
-- left join center c on c.id = oi.CENTER_ID
-- left join center cc on cc.id = spc.CURRENT_CENTER_ID
-- left join specimen_comment spccmt on spccmt.SPECIMEN_ID = spc.ID
-- left join specimen_position spos on spos.SPECIMEN_ID=spc.id
-- left join container on container.id=spos.CONTAINER_ID
-- left join container top_container on top_container.id=container.TOP_CONTAINER_ID
-- left join container_type top_container_type on top_container_type.id=top_container.CONTAINER_TYPE_ID
-- where
--         -- parent_spc.INVENTORY_ID = "2 10031 01U"
--         parent_spc.INVENTORY_ID = "HJXD"
-- order by
--         spc.CREATED_AT;
--
-- SET @pnumber := '2016-0120';
-- SET @vnumber := 14;

select
    ce.id,
    ce.visit_number as visitNumber,
    ce.ACTIVITY_STATUS_ID,
    ce.patient_id as patientId,
    p.pnumber as patientNumber,
    study.id as studyId,
    study.name_short as studyNameShort,
    ea.id as attributeId,
    gea.LABEL attributeLabel,
    ea.VALUE attributeValue,
    if(spc.ORIGINAL_COLLECTION_EVENT_ID = ce.id,1,0 ) as isSourceSpecimen,
    spc.id specimenId,
    spc.INVENTORY_ID as specimenInventoryId,
    spc.CREATED_AT specimenCreatedAt,
    spc.QUANTITY as specimenQuantity,
    spc.ACTIVITY_STATUS_ID as specimenActivityStatusId,
    st.id as specimenTypeId,
    st.NAME_SHORT as specimenTypeNameShort,
    oi.CENTER_ID as originCenterId,
    c.NAME_SHORT as originCenterNameShort,
    cc.ID as currentCenterId,
    cc.NAME_SHORT as currentCenterNameShort,
    concat(container.label, "-", spos.POSITION_STRING, " (", top_container_type.NAME_SHORT, ")") as `position`,
    pe.ID processingEventId,
    pe.WORKSHEET as worksheet,
    if( spccmt.SPECIMEN_ID != null,1,0 ) hasSpecimenComments,
    cmt.id as commentId,
    cmt.MESSAGE as commentMessage,
    cmt.CREATED_AT as commentCreatedAt,
    usr.login as commentUser
from collection_event ce
left join specimen spc on spc.COLLECTION_EVENT_ID = ce.ID
left join patient p on p.id = ce.patient_id
left join study on study.id = p.study_id
left join study_event_attr sea on sea.STUDY_ID= study.ID
left join global_event_attr gea on gea.id = sea.GLOBAL_EVENT_ATTR_ID
left join event_attr ea on ea.STUDY_EVENT_ATTR_ID = sea.ID and ea.COLLECTION_EVENT_ID=ce.id
left join specimen_type st on st.ID = spc.SPECIMEN_TYPE_ID
left join origin_info oi on oi.ID = spc.ORIGIN_INFO_ID
left join center c on c.id = oi.CENTER_ID
left join center cc on cc.id = spc.CURRENT_CENTER_ID
left join collection_event_comment cec on cec.COLLECTION_EVENT_ID = ce.id
left join comment cmt on cmt.id = cec.COMMENT_ID
left join specimen_comment spccmt on spccmt.SPECIMEN_ID = spc.ID
left join principal usr on usr.id = cmt.USER_ID
left join specimen_position spos on spos.SPECIMEN_ID = spc.id
left join container on container.id = spos.CONTAINER_ID
left join container top_container on top_container.id = container.TOP_CONTAINER_ID
left join container_type top_container_type on top_container_type.id = top_container.CONTAINER_TYPE_ID
left join processing_event pe on pe.id = spc.PROCESSING_EVENT_ID
where
    p.pnumber = '2016-0120'
    and ce.visit_number = 2
order by
    spc.CREATED_AT;
