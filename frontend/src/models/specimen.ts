import { z } from 'zod';
import { domainEntitySchema } from './domain-entity';
import { Status } from './status';

export const sourceSpecimenSchema = domainEntitySchema.extend({
  inventoryId: z.string(),
  quantity: z.number().nullable(),
  status: z.nativeEnum(Status),
  specimenTypeId: z.number(),
  specimenTypeNameShort: z.string(),
  originCenterId: z.number(),
  originCenterNameShort: z.string(),
  currentCenterId: z.number(),
  currentCenterNameShort: z.string(),
  timeDrawn: z.string().pipe(z.coerce.date())
});

export type SourceSpecimen = z.infer<typeof sourceSpecimenSchema>;

export const aliquotSchema = domainEntitySchema.extend({
  parentId: z.number().min(1),
  inventoryId: z.string(),
  createdAt: z.string().pipe(z.coerce.date()),
  quantity: z.number().nullable(),
  status: z.nativeEnum(Status),
  specimenTypeId: z.number(),
  specimenTypeNameShort: z.string(),
  originCenterId: z.number(),
  originCenterNameShort: z.string(),
  currentCenterId: z.number(),
  currentCenterNameShort: z.string(),
  hasComments: z.boolean(),
  position: z.string().nullable(),
  patientNumber: z.string(),
  studyId: z.number().min(1),
  studyNameShort: z.string(),
  processingEventId: z.number().min(1),
  worksheet: z.string(),
});

export type Aliquot = z.infer<typeof aliquotSchema>;
