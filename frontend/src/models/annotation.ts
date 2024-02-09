import { z } from 'zod';

const annotationTypeSchema = z.union([
  z.literal('number'),
  z.literal('text'),
  z.literal('date_time'),
  z.literal('select_single'),
  z.literal('select_multiple')
]);

export const annotationSchema = z.object({
  type: annotationTypeSchema,
  name: z.string(),
  value: z.string().nullable()
});

export type AnnotationType = z.infer<typeof annotationTypeSchema>;
export type Annotation = z.infer<typeof annotationSchema>;
