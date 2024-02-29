import { ApiError } from '@app/api/api';
import { SpecimenApi } from '@app/api/specimen-api';
import { SourceSpecimenAdd } from '@app/models/specimen';

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

export function useAliquots(parentInventoryId: string) {
  return useQuery({
    queryKey: ['specimens', 'aliquots', parentInventoryId],
    queryFn: async () => SpecimenApi.getByParentInventoryId(parentInventoryId),
    retry: (failureCount, error: ApiError) => failureCount <= 3 && error.status !== 404 && error.status !== 400,
  });
}

export function useSourceSpecimenAdd() {
  const queryClient = useQueryClient();

  const collectionEventMutation = useMutation({
    mutationFn: (specimen: SourceSpecimenAdd) => SpecimenApi.add(specimen),
    onSuccess: (_data, variables, _context) => {
      queryClient.invalidateQueries({ queryKey: ['specimens', variables.inventoryId] });
      queryClient.invalidateQueries({
        queryKey: ['patients', variables.pnumber, 'collection-events', variables.vnumber],
      });
    },
  });

  return collectionEventMutation;
}
