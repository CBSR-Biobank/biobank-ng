import { ApiError } from '@app/api/api';
import { PatientApi } from '@app/api/patient-api';
import { useQuery } from '@tanstack/react-query';

export function usePatientComments(pnumber: string) {
  return useQuery(['patients', pnumber, 'comments'], async () => PatientApi.getPatientComments(pnumber), {
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400
  });
}
