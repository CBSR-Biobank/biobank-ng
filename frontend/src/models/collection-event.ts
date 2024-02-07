import { z } from 'zod';
import { annotationSchema } from './annotation';
import { domainEntitySchema } from './domain-entity';
import { sourceSpecimenSchema } from './specimen';
import { Status } from './status';

export const collectionEventBriefSchema = domainEntitySchema.extend({
  id: z.number(),
  createdAt: z.union([z.null(), z.string().pipe(z.coerce.date())]).optional(),
  visitNumber: z.number(),
  specimenCount: z.number(),
  aliquotCount: z.number(),
  status: z.nativeEnum(Status)
});

export const collectionEventSchema = domainEntitySchema.extend({
  id: z.number(),
  vnumber: z.number(),
  patientId: z.number(),
  pnumber: z.string(),
  studyId: z.number(),
  studyNameShort: z.string(),
  status: z.nativeEnum(Status),
  commentCount: z.number(),
  annotations: z.array(annotationSchema),
  sourceSpecimens: z.array(sourceSpecimenSchema)
});

export type CollectionEventBrief = z.infer<typeof collectionEventBriefSchema>;
export type CollectionEvent = z.infer<typeof collectionEventSchema>;

export type CollectionEventAdd = Pick<CollectionEvent, 'vnumber'>;

export type CollectionEventUpdate = Pick<CollectionEvent, 'vnumber' | 'status' | 'annotations' | 'sourceSpecimens'>;
