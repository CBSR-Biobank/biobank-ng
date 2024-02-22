import { z } from 'zod';

export const commentSchema = z.object({
  id: z.number(),
  message: z.string(),
  user: z.string().nullable(),
  createdAt: z.string().pipe(z.coerce.date()),
});

export type Comment = z.infer<typeof commentSchema>;

export type CommentAdd = Pick<Comment, 'message'>;
