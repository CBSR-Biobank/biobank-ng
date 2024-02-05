import { ApiError } from '@app/api/api';
import { CollectionEventApi } from '@app/api/collection-event-api';
import { PatientApi } from '@app/api/patient-api';
import { CollectionEventAdd } from '@app/models/collection-event';
import { CommentAdd } from '@app/models/comment';
import { usePatientStore } from '@app/store';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';

export function usePatient(pnumber: string) {
  const { setPatient } = usePatientStore();
  const query = useQuery(['patients', pnumber], async () => PatientApi.getByPnumber(pnumber), {
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400
  });
  const { data: patient } = query;

  useEffect(() => {
    if (patient) {
      setPatient(patient);
    }
  }, [patient]);

  return {
    patient,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error
  };
}

export function usePatientCollectionEventAdd() {
  const queryClient = useQueryClient();
  const { patient } = usePatientStore();

  const collectionEventMutator = useMutation(
    (newVisit: CollectionEventAdd) => {
      if (!patient) {
        throw new Error('patient is invalid');
      }
      return CollectionEventApi.add(patient, newVisit);
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['patients', patient?.pnumber]);
      }
    }
  );

  return collectionEventMutator;
}

export function usePatientCommentAdd() {
  const queryClient = useQueryClient();
  const { patient } = usePatientStore();

  const commentMutator = useMutation(
    (newComment: CommentAdd) => {
      if (!patient) {
        return Promise.resolve(null);
      }

      return PatientApi.addComment(patient, newComment);
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['patients', patient?.pnumber]);
      }
    }
  );

  return commentMutator;
}
