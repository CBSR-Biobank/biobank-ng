import { ApiError } from '@app/api/api';
import { LoggingApi } from '@app/api/logging-api';
import { useQuery } from '@tanstack/react-query';
import { SortingState } from '@tanstack/react-table';

export function useLogging(args: { page?: number; size?: number; sorting?: SortingState; searchTerm?: string }) {
  return useQuery({
    queryKey: ['logging', args],
    queryFn: async () => LoggingApi.paginate(args),
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400,
  });
}
