import { z } from 'zod';

import { Status } from './status';

export const annotationTypeNameSchema = z.union([
  z.literal('number'),
  z.literal('text'),
  z.literal('date_time'),
  z.literal('select_single'),
  z.literal('select_multiple'),
]);

export const annotationTypeSchema = z.object({
  type: annotationTypeNameSchema,
  label: z.string(),
  status: z.nativeEnum(Status),
  required: z.boolean(),
  validValues: z.array(z.string()).nullable(),
});

export type AnnotationTypeName = z.infer<typeof annotationTypeNameSchema>;
export type AnnotationType = z.infer<typeof annotationTypeSchema>;
