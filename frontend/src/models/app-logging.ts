import { z } from 'zod';

export const appLoggingSchema = z.object({
  id: z.number(),
  action: z.string(),
  center: z.string().nullable(),
  createdAt: z.string().datetime({ offset: true }),
  details: z.string(),
  inventoryId: z.string().nullable(),
  locationLabel: z.string().nullable(),
  patientNumber: z.string(),
  type: z.string(),
  username: z.string().nullable(),
});

export type AppLogging = z.infer<typeof appLoggingSchema>;
