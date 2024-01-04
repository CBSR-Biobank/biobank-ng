package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionEventRepository
    extends JpaRepository<CollectionEvent, Integer>, JpaSpecificationExecutor<CollectionEvent> {
    @Query(
        value = """
        select
            ce.id,
            ce.visit_number as visitNumber,
            ce.ACTIVITY_STATUS_ID,
            COUNT(distinct ospc.id) as specimenCount,
            COUNT(distinct aspc.id) - COUNT(distinct ospc.id) as aliquotCount,
            MIN(ospc.created_at) as createdAt
        from collection_event ce
        left join specimen ospc on ospc.ORIGINAL_COLLECTION_EVENT_ID = ce.ID
        left join specimen aspc on aspc.COLLECTION_EVENT_ID = ce.ID
        where ce.PATIENT_ID in (select id from patient p where p.pnumber = :pnumber)
        group by ce.id
        order by ce.visit_number
        """,
        nativeQuery = true
    )
    public <T> Collection<T> findSummariesByPatient(String pnumber, Class<T> type);

    @Query(
        value = """
        select
            ce.id,
            ce.visit_number as visitNumber,
            ce.ACTIVITY_STATUS_ID,
            ce.patient_id as patientId,
            p.pnumber as patientNumber,
            study.id as studyId,
            study.name_short as studyNameShort,
            ea.id as attributeId,
            ea.VALUE attributeValue,
            gea.LABEL attributeLabel,
            if(spc.ORIGINAL_COLLECTION_EVENT_ID = ce.id, 1, 0) as isSourceSpecimen,
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
            if(spccmt.SPECIMEN_ID != null, 1, 0) hasSpecimenComments,
            cmt.id as commentId,
            cmt.MESSAGE as commentMessage,
            cmt.CREATED_AT as commentCreatedAt,
            usr.login as commentUser
        from collection_event ce
        left join specimen spc on spc.COLLECTION_EVENT_ID = ce.ID
        left join patient p on p.id = ce.patient_id
        left join study on study.id = p.study_id
        left join event_attr ea on ea.COLLECTION_EVENT_ID = ce.id
        left join study_event_attr sea on sea.ID = ea.STUDY_EVENT_ATTR_ID
        left join global_event_attr gea on gea.id = sea.GLOBAL_EVENT_ATTR_ID
        left join specimen_type st on st.ID = spc.SPECIMEN_TYPE_ID
        left join origin_info oi on oi.ID = spc.ORIGIN_INFO_ID
        left join center c on c.id=oi.CENTER_ID
        left join center cc on cc.id=spc.CURRENT_CENTER_ID
        left join collection_event_comment cec on cec.COLLECTION_EVENT_ID = ce.id
        left join comment cmt on cmt.id = cec.COMMENT_ID
        left join specimen_comment spccmt on spccmt.SPECIMEN_ID = spc.ID
        left join principal usr on usr.id=cmt.USER_ID
        left join specimen_position spos on spos.SPECIMEN_ID=spc.id
        left join container on container.id=spos.CONTAINER_ID
        left join container top_container on top_container.id=container.TOP_CONTAINER_ID
        left join container_type top_container_type on top_container_type.id=top_container.CONTAINER_TYPE_ID
        left join processing_event pe on pe.id=spc.PROCESSING_EVENT_ID
        where
            p.pnumber = :pnumber
            and ce.visit_number = :vnumber
        order by spc.CREATED_AT
        """,
        nativeQuery = true
    )
    public <T> Collection<T> findByPatientAndVnumber(String pnumber, Integer vnumber, Class<T> type);
}
