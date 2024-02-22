import { z } from 'zod';

export const loggingSchema = z.object({
  id: z.number(),
  action: z.string(),
  center: z.string(),
  createdAt: z.string().pipe(z.coerce.date()),
  details: z.string(),
  inventoryId: z.string(),
  locationLabel: z.string(),
  patientNumber: z.string(),
  type: z.string(),
  username: z.string(),
});

export type Logging = z.infer<typeof loggingSchema>;
