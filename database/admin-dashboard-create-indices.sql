-- LOG TABLE
ALTER TABLE log DROP INDEX IF EXISTS created_at_ndx;
ALTER TABLE log DROP INDEX IF EXISTS patient_number_ndx;
ALTER TABLE log DROP INDEX IF EXISTS inventory_id_ndx;

ALTER TABLE log ADD INDEX created_at_ndx (created_at);
ALTER TABLE log ADD INDEX patient_number_ndx (patient_number);
ALTER TABLE log ADD INDEX inventory_id_ndx (inventory_id);

-- PATIENT TABLE

ALTER TABLE patient DROP INDEX IF EXISTS patient_number_ndx;
ALTER TABLE patient DROP INDEX IF EXISTS created_at_ndx;

ALTER TABLE patient DROP INDEX IF EXISTS patient_number_ndx;
ALTER TABLE patient DROP INDEX IF EXISTS created_at_ndx;

-- SPECIMEN TABLE

ALTER TABLE specimen DROP INDEX IF EXISTS created_at_ndx;
ALTER TABLE specimen ADD INDEX created_at_ndx (created_at);
