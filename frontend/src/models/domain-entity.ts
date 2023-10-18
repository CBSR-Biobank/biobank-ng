import { z } from 'zod';

export const domainEntitySchema = z.object({
  id: z.number().min(1)
});

export type DomainEntity = z.infer<typeof domainEntitySchema>;
