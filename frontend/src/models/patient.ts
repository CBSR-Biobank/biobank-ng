import { z } from 'zod';
import { collectionEventBriefSchema } from './collection-event';
import { domainEntitySchema } from './domain-entity';

export const patientSchema = domainEntitySchema.extend({
  id: z.number(),
  createdAt: z.string().pipe(z.coerce.date()),
  pnumber: z.string(),
  specimenCount: z.number(),
  aliquotCount: z.number(),
  commentCount: z.number(),
  studyId: z.number(),
  studyNameShort: z.string(),
  collectionEvents: z.array(collectionEventBriefSchema)
});

export type Patient = z.infer<typeof patientSchema>;

export type PatientAdd = Pick<Patient, 'pnumber' | 'createdAt' | 'studyNameShort'>;

export type PatientUpdate = PatientAdd;
