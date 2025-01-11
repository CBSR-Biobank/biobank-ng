import { z } from 'zod';

export function paginatedResponseSchema<T>(valueSchema: z.ZodSchema<T>) {
  return z.object({
    content: z.array(valueSchema),
    empty: z.boolean(),
    first: z.boolean(),
    last: z.boolean(),
    number: z.number().min(0),
    numberOfElements: z.number().min(0),
    size: z.number(),
    totalElements: z.number().min(0),
    totalPages: z.number().min(0),
  });
}

export type PaginatedResponse<T> = z.infer<ReturnType<typeof paginatedResponseSchema<T>>>;
