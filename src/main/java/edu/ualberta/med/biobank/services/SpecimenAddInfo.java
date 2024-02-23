package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.dtos.ClinicDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.SpecimenTypeDTO;

record SpecimenAddInfo(ClinicDTO clinic, CollectionEventDTO visit, SpecimenTypeDTO specimenType) {
}
