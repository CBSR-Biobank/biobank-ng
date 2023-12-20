import { ApiError } from '@app/api/api';
import { SpecimenApi } from '@app/api/specimen-api';
import { useQuery } from '@tanstack/react-query';

export function useAliquots(parentInventoryId?: string) {
  return useQuery(['specimen', 'aliquots', parentInventoryId], async () => SpecimenApi.getByParentInventoryId(parentInventoryId), {
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400
  });
}
