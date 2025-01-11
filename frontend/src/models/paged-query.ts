import { SortingState } from '@tanstack/react-table';
import { z } from 'zod';

export const pagedQuerySchema = z.object({
  page: z.number().min(0).optional(),
  size: z.number().min(-1).optional(),
  search: z.optional(z.string()),
  sort: z.array(z.string()).optional(),
});

export type PagedQuery = z.infer<typeof pagedQuerySchema>;

export function pagedQueryBuild(props: { page?: number; size?: number; search?: string; sorting?: SortingState }) {
  let query: any = { size: 0 };
  Object.entries(props).forEach(([key, value]) => {
    if (value !== undefined) {
      if (key === 'search') {
        const str = value as string;
        if (str.trim() !== '') {
          query[key] = value;
        }
      } else if (key === 'sorting') {
        query['sort'] = (props.sorting ?? []).map((s) => `${s.desc ? '-' : ''}${s.id}`);
      } else {
        query[key] = value;
      }
    }
  });
  return pagedQuerySchema.parse(query);
}
