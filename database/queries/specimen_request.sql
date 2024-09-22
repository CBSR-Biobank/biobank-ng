select
   p.PNUMBER,
   s.inventory_id,
   stop.CREATED_AT,
   CONCAT(ct.LABEL,
   sp.POSITION_STRING) location,
   s.activity_status_id
FROM specimen s
   inner join collection_event ce on ce.id = s.COLLECTION_EVENT_ID
   inner join patient p on p.id = ce.patient_id
   inner join specimen stop on stop.id = s.TOP_SPECIMEN_ID
   inner join specimen_type styp on styp.id = s.SPECIMEN_TYPE_ID
   inner join specimen_position sp on sp.specimen_id = s.id
   inner join container ct on ct.id = sp.container_id
WHERE ct.label not like 'SS%'
   and p.pnumber = '2086'
   and abs(datediff(stop.created_at, '2001-05-08')) <= 1
   and styp.name_short = 'Plasma'
   and s.activity_status_id != 2
 ORDER BY s.activity_status_id, RAND();

 -- select convert_tz('2001-05-09 06:00:00.000', 'UTC', 'US/Mountain');
