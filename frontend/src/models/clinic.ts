import { z } from 'zod';

import { domainEntitySchema } from './domain-entity';

export const clinicNameSchema = domainEntitySchema.extend({
  id: z.number(),
  name: z.string(),
  nameShort: z.string(),
});

export type ClinicName = z.infer<typeof clinicNameSchema>;
