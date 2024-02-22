import { z } from 'zod';

import { annotationTypeNameSchema } from './annotation-type';

export const annotationSchema = z.object({
  type: annotationTypeNameSchema,
  name: z.string(),
  value: z.string().nullable(),
});

export type Annotation = z.infer<typeof annotationSchema>;

export function annotationValueAsDate(annotation: Annotation) {
  if (!annotation.value || annotation.value === '') {
    return undefined;
  }
  const schema = z.union([z.undefined(), z.string().pipe(z.coerce.date())]).optional();
  return schema.parse(annotation.value);
}
