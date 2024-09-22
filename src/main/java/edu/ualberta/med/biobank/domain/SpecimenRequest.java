package edu.ualberta.med.biobank.domain;

import java.util.Date;

public record SpecimenRequest(String pnumber,  Date dateDrawn, String specimenType, Integer count) {}
