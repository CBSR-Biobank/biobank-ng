import { z } from 'zod';

import { domainEntitySchema } from './domain-entity';
import { Status } from './status';

export const sourceSpecimenSchema = domainEntitySchema.extend({
  inventoryId: z.string(),
  quantity: z.number().nullable(),
  timeDrawn: z.string().pipe(z.coerce.date()),
  status: z.nativeEnum(Status),
  specimenTypeId: z.number(),
  specimenTypeNameShort: z.string(),
  pnumber: z.string(),
  vnumber: z.number(),
  originCenterId: z.number(),
  originCenterNameShort: z.string(),
  currentCenterId: z.number(),
  currentCenterNameShort: z.string(),
  position: z.string().nullable(),
  worksheet: z.string().nullable(),
  hasComments: z.boolean().optional(),
});

export type SourceSpecimen = z.infer<typeof sourceSpecimenSchema>;
export type SourceSpecimenAdd = Pick<
  SourceSpecimen,
  'inventoryId' | 'timeDrawn' | 'status' | 'specimenTypeNameShort' | 'pnumber' | 'vnumber' | 'originCenterNameShort'
>;

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
