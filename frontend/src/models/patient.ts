import { z } from 'zod';
import { collectionEventSchema } from './collection-event';
import { domainEntitySchema } from './domain-entity';

export const patientSchema = domainEntitySchema.extend({
  id: z.number(),
  createdAt: z.coerce.date(),
  pnumber: z.string(),
  specimenCount: z.number(),
  aliquotCount: z.number(),
  studyId: z.number(),
  studyNameShort: z.string(),
  collectionEvents: z.array(collectionEventSchema)
});
