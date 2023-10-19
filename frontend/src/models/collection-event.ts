import { z } from 'zod';
import { domainEntitySchema } from './domain-entity';

export const collectionEventSchema = domainEntitySchema.extend({
  id: z.number(),
  createdAt: z.coerce.date(),
  visitNumber: z.number(),
  specimenCount: z.number(),
  aliquotCount: z.number(),
  status: z.string()
});
