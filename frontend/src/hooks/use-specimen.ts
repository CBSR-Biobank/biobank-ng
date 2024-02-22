import { ApiError } from '@app/api/api';
import { SpecimenApi } from '@app/api/specimen-api';

import { useQuery } from '@tanstack/react-query';

export function useAliquots(parentInventoryId: string) {
  return useQuery({
    queryKey: ['specimen', 'aliquots', parentInventoryId],
    queryFn: async () => SpecimenApi.getByParentInventoryId(parentInventoryId),
    retry: (failureCount, error: ApiError) => failureCount <= 3 && error.status !== 404 && error.status !== 400,
  });
}
