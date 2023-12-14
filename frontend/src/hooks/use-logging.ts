import { ApiError } from '@app/api/api';
import { LoggingApi } from '@app/api/logging-api';
import { useQuery } from '@tanstack/react-query';

export function useLoggingLatest() {
  return useQuery(['logging', 'latest'], async () => LoggingApi.getLatest(), {
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400
  });
}
