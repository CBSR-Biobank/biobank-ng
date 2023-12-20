-- select *
-- from container
-- left join container_type on container_type.id=container.CONTAINER_TYPE_ID
-- where container.TOP_CONTAINER_ID=container.ID;

-- select * from processing_event where WORKSHEET = '2 10031';

-- select * from specimen where id=1398769;

select
    spc.id specimenId,
    spc.PARENT_SPECIMEN_ID as parentSpecimenId,
    spc.INVENTORY_ID as specimenInventoryId,
    spc.CREATED_AT specimenTimeDrawn,
    spc.QUANTITY as specimenQuantity,
    spc.ACTIVITY_STATUS_ID as specimenActivityStatusId,
    st.id as specimenTypeId,
    st.NAME_SHORT as specimenTypeNameShort,
    spc.PROCESSING_EVENT_ID,
    pe.WORKSHEET as worksheet,
    oi.CENTER_ID as originCenterId,
    c.NAME_SHORT as originCenterNameShort,
    cc.ID as currentCenterId,
    cc.NAME_SHORT as currentCenterNameShort,
    if(spccmt.SPECIMEN_ID != null, "Y", "N") hasSpecimenComments,
    concat(container.label, "-", spos.POSITION_STRING, " (", top_container_type.NAME_SHORT, ")") as `position`,
    p.PNUMBER as pnumber,
    study.id as studyId,
    study.NAME_SHORT as studyNameShort
from specimen spc
left join specimen parent_spc on parent_spc.id=spc.PARENT_SPECIMEN_ID
left join collection_event ce on ce.id=parent_spc.COLLECTION_EVENT_ID
left join patient p on p.id=ce.PATIENT_ID
left join study on study.id=p.STUDY_ID
left join specimen_type st on st.ID = spc.SPECIMEN_TYPE_ID
left join origin_info oi on oi.ID = spc.ORIGIN_INFO_ID
left join processing_event pe on pe.id=parent_spc.PROCESSING_EVENT_ID
left join center c on c.id = oi.CENTER_ID
left join center cc on cc.id = spc.CURRENT_CENTER_ID
left join specimen_comment spccmt on spccmt.SPECIMEN_ID = spc.ID
left join specimen_position spos on spos.SPECIMEN_ID=spc.id
left join container on container.id=spos.CONTAINER_ID
left join container top_container on top_container.id=container.TOP_CONTAINER_ID
left join container_type top_container_type on top_container_type.id=top_container.CONTAINER_TYPE_ID
where
        -- parent_spc.INVENTORY_ID = "2 10031 01U"
        parent_spc.INVENTORY_ID = "HJXD"
order by
        spc.CREATED_AT;
