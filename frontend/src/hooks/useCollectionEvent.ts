import { ApiError } from '@app/api/api';
import { PatientApi } from '@app/api/patients-api';
import { useQuery } from '@tanstack/react-query';

export function useCollectionEvent(pnumber?: string, vnumber?: number) {
  return useQuery(
    ['patients', pnumber, 'collection-event', vnumber],
    async () => PatientApi.getPatientCollectionEvent(pnumber, vnumber),
    {
      retry: (count, error: ApiError) => count <= 3 && error.status !== 404
    }
  );
}
