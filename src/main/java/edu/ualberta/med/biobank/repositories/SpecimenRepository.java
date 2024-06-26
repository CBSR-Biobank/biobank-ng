package edu.ualberta.med.biobank.repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import edu.ualberta.med.biobank.domain.Specimen;

@Repository
public interface SpecimenRepository extends JpaRepository<Specimen, Integer> {
    @Query(
        value = """
        select
            spc.id specimenId,
            spc.PARENT_SPECIMEN_ID as parentSpecimenId,
            spc.INVENTORY_ID as specimenInventoryId,
            spc.CREATED_AT specimenCreatedAt,
            spc.QUANTITY as specimenQuantity,
            spc.ACTIVITY_STATUS_ID as specimenActivityStatusId,
            st.id as specimenTypeId,
            st.NAME_SHORT as specimenTypeNameShort,
            p.pnumber as patientNumber,
            ce.visit_number as visitNumber,
            oi.CENTER_ID as originCenterId,
            c.NAME_SHORT as originCenterNameShort,
            cc.ID as currentCenterId,
            cc.NAME_SHORT as currentCenterNameShort,
            if(spccmt.SPECIMEN_ID != null, "Y", "N") hasSpecimenComments,
            concat(container.label, "|", spos.POSITION_STRING, " (", top_container_type.NAME_SHORT, ")") as `position`,
            p.PNUMBER as patientNumber,
            study.id as studyId,
            study.NAME_SHORT as studyNameShort,
            pe.ID processingEventId,
            pe.WORKSHEET as worksheet
        from specimen spc
        left join collection_event ce on ce.id=spc.COLLECTION_EVENT_ID
        left join patient p on p.id=ce.PATIENT_ID
        left join study on study.id=p.STUDY_ID
        left join specimen_type st on st.ID = spc.SPECIMEN_TYPE_ID
        left join origin_info oi on oi.ID = spc.ORIGIN_INFO_ID
        left join processing_event pe on pe.id=spc.PROCESSING_EVENT_ID
        left join center c on c.id = oi.CENTER_ID
        left join center cc on cc.id = spc.CURRENT_CENTER_ID
        left join specimen_comment spccmt on spccmt.SPECIMEN_ID = spc.ID
        left join specimen_position spos on spos.SPECIMEN_ID=spc.id
        left join container on container.id=spos.CONTAINER_ID
        left join container top_container on top_container.id=container.TOP_CONTAINER_ID
        left join container_type top_container_type on top_container_type.id=top_container.CONTAINER_TYPE_ID
        where
            spc.INVENTORY_ID = :inventoryId
        order by
            spc.CREATED_AT
        """,
        nativeQuery = true
    )
    public <T> Collection<T> findByInventoryId(String inventoryId, Class<T> type);

    @Query(
        value = """
        select
            spc.id specimenId,
            spc.PARENT_SPECIMEN_ID as parentSpecimenId,
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
            if(spccmt.SPECIMEN_ID != null, "Y", "N") hasSpecimenComments,
            concat(container.label, "|", spos.POSITION_STRING, " (", top_container_type.NAME_SHORT, ")") as `position`,
            p.PNUMBER as patientNumber,
            study.id as studyId,
            study.NAME_SHORT as studyNameShort,
            pe.ID processingEventId,
            pe.WORKSHEET as worksheet
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
            parent_spc.INVENTORY_ID = :inventoryId
        order by
            spc.CREATED_AT
        """,
        nativeQuery = true
    )
    public <T> Collection<T> findByParentInventoryId(String inventoryId, Class<T> type);

    @Query(
        value = """
        select
          study.name_short study,
          pt.pnumber pnumber,
          spc.inventory_id,
          spc.created_at,
          pspc.created_at time_drawn,
          spc.quantity,
          center.name_short center,
          ce.visit_number,
          pe.worksheet worksheet,
          pspc.inventory_id parent_inventory_id,
          stype.name specimen_type,
          top_cntr_type.name_short top_container,
          cntr.label,
          spos.position_string position
        from
          specimen spc
          left join specimen pspc on pspc.id = spc.parent_specimen_id
          join specimen_type stype on stype.id = spc.specimen_type_id
          join collection_event ce on ce.id = spc.collection_event_id
          left join processing_event pe on pe.id = pspc.processing_event_id
          join patient pt on pt.id = ce.patient_id
          join study on study.id = pt.study_id
          join center on center.id = spc.current_center_id
          join site_study on site_study.study_id = study.id
          join center site on site.id = site_study.site_id
          left join specimen_position spos on spos.specimen_id = spc.id
          left join container cntr on cntr.id = spos.container_id
          left join container top_cntr on top_cntr.id = cntr.top_container_id
          left join container_type top_cntr_type on top_cntr_type.id = top_cntr.container_type_id
        where
          study.name_short = :nameShort
          and top_cntr_type.name_short not like 'SS%'
          and spc.activity_status_id = 1
        order by
          pt.pnumber,
          ce.visit_number,
          spc.inventory_id,
          spc.created_at,
          pspc.inventory_id,
          stype.name
        """,
        nativeQuery = true
    )
    public <T> Collection<T> findByStudy(String nameShort, Class<T> type);
}
