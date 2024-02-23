import { ClinicApi } from '@app/api/clinic-api';
import { Status } from '@app/models/status';

import { keepPreviousData, useQuery } from '@tanstack/react-query';

export function useClinicNames() {
  const query = useQuery({
    queryKey: ['studies', 'names'],
    queryFn: async () => ClinicApi.names(Status.ACTIVE),
    placeholderData: keepPreviousData,
  });

  return {
    clinicNames: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error,
  };
}
