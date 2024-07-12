import { z } from 'zod';
import { CatalogueState } from './catalogue-state';

export const catalougeStatusSchema = z.object({
  id: z.ostring(),
  progress: z.number(),
  state: z.nativeEnum(CatalogueState),
  nameShort: z.string(),
  fileUrl: z.string().nullable(),
});

export type CatalogueStatus = z.infer<typeof catalougeStatusSchema>;
