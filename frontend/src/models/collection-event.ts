import { z } from 'zod';
import { commentSchema } from './comment';
import { domainEntitySchema } from './domain-entity';
import { sourceSpecimenSchema } from './specimen';
import { Status } from './status';

/**
 *
 */
export const eventAttributeSchema = z.object({
  name: z.string(),
  value: z.string()
});

export const collectionEventBriefSchema = domainEntitySchema.extend({
  id: z.number(),
  createdAt: z.union([z.null(), z.string().pipe(z.coerce.date())]),
  visitNumber: z.number(),
  specimenCount: z.number(),
  aliquotCount: z.number(),
  status: z.nativeEnum(Status)
});

export const collectionEventSchema = domainEntitySchema.extend({
  id: z.number(),
  visitNumber: z.number(),
  patientId: z.number(),
  patientNumber: z.string(),
  studyId: z.number(),
  studyNameShort: z.string(),
  status: z.nativeEnum(Status),
  attributes: z.array(eventAttributeSchema),
  comments: z.array(commentSchema),
  sourceSpecimens: z.array(sourceSpecimenSchema)
});

export type CollectionEventBrief = z.infer<typeof collectionEventBriefSchema>;
export type CollectionEvent = z.infer<typeof collectionEventSchema>;
