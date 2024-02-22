import { z } from 'zod';

import { domainEntitySchema } from './domain-entity';

export const studyNameSchema = domainEntitySchema.extend({
  id: z.number(),
  name: z.string(),
  nameShort: z.string(),
});

export type StudyName = z.infer<typeof studyNameSchema>;
