import { z } from 'zod';
import { Status } from './status';

export const specimenPullSchema = z.object({
  pnumber: z.string(),
  inventoryId: z.string(),
  dateDrawn: z.string().pipe(z.coerce.date()),
  specimenType: z.string(),
  location: z.string(),
  activityStatus: z.nativeEnum(Status),
});

export type SpecimenPull = z.infer<typeof specimenPullSchema>;
