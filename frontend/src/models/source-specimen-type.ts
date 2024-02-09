import { z } from 'zod';
import { domainEntitySchema } from './domain-entity';

export const sourceSpecimenTypeSchema = domainEntitySchema.extend({
  id: z.number().optional(),
  name: z.string(),
  nameShort: z.string(),
  needOriginalVolume: z.boolean()
});

export type SourceSpecimenType = z.infer<typeof sourceSpecimenTypeSchema>;
