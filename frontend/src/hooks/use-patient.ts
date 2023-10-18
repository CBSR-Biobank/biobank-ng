import { ApiError } from '@app/api/api';
import { PeopleApi } from '@app/api/patients-api';
import { useQuery } from '@tanstack/react-query';

export function usePatient(pnumber: string) {
  return useQuery(['patient', pnumber], async () => PeopleApi.getByPnumber(pnumber), {
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404
  });
}
