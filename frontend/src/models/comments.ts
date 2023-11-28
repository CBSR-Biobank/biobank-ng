import { z } from 'zod';

export const commentSchema = z.object({
  id: z.number(),
  message: z.string(),
  user: z.string(),
  createdAt: z.string().pipe(z.coerce.date())
});

export type Comment = z.infer<typeof commentSchema>;
