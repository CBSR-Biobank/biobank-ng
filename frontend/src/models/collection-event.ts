import { z } from 'zod';
import { domainEntitySchema } from './domain-entity';

export const collectionEventSchema = domainEntitySchema.extend({
  id: z.number(),
  visitNumber: z.number(),
  specimenCount: z.number(),
  aliquotCount: z.number(),
  status: z.string()
});
