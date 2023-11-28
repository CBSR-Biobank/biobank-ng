import { z } from 'zod';
import { domainEntitySchema } from './domain-entity';
import { Status } from './status';

export const sourceSpecimenSchema = domainEntitySchema.extend({
  id: z.number(),
  inventoryId: z.string(),
  quantity: z.number().nullable(),
  status: z.nativeEnum(Status),
  specimenTypeId: z.number(),
  specimenTypeNameShort: z.string(),
  currentCenterId: z.number(),
  currentCenterNameShort: z.string(),
  originCenterId: z.number(),
  originCenterNameShort: z.string(),
  timeDrawn: z.string().pipe(z.coerce.date())
});

export type SourceSpecimen = z.infer<typeof sourceSpecimenSchema>;
