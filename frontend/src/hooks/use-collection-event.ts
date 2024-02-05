import { ApiError } from '@app/api/api';
import { CollectionEventApi } from '@app/api/collection-event-api';
import { useQuery } from '@tanstack/react-query';

export function useCollectionEvent(pnumber: string, vnumber: number) {
  return useQuery(
    ['patients', pnumber, 'collection-events', vnumber],
    async () => CollectionEventApi.getCollectionEvent(pnumber, vnumber),
    {
      retry: (count, error: ApiError) => count <= 3 && error.status !== 404
    }
  );
}
