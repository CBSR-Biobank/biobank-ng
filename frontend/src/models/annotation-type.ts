import { z } from 'zod';
import { Status } from './status';

export const annotationTypeSchema = z.object({
  type: z.string(),
  label: z.string(),
  status: z.nativeEnum(Status),
  required: z.boolean(),
  validValues: z.array(z.string()).nullable()
});

export type AnnotationType = z.infer<typeof annotationTypeSchema>;
