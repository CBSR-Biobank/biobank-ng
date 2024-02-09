import { ApiError } from '@app/api/api';
import { CollectionEventApi } from '@app/api/collection-event-api';
import { useQuery } from '@tanstack/react-query';

export function useCollectionEventComments(pnumber: string, vnumber: number) {
  return useQuery(
    ['patients', pnumber, 'collection-events', vnumber, 'comments'],
    async () => CollectionEventApi.getComments(pnumber, vnumber),
    {
      retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400
    }
  );
}
