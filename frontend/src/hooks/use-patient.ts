import { ApiError } from '@app/api/api';
import { CollectionEventApi } from '@app/api/collection-event-api';
import { PatientApi } from '@app/api/patient-api';
import { CollectionEventAdd } from '@app/models/collection-event';
import { CommentAdd } from '@app/models/comment';
import { PatientUpdate } from '@app/models/patient';
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

export function usePatientUpdate() {
  const queryClient = useQueryClient();
  const { patient } = usePatientStore();

  const updatePatientMutation = useMutation(
    (updatedPatient: PatientUpdate) => {
      if (!patient) {
        throw new Error('patient is invalid');
      }
      return PatientApi.update(patient.pnumber, updatedPatient);
    },
    {
      onSuccess: (updated) => {
        queryClient.invalidateQueries(['patients', updated.pnumber]);
      }
    }
  );
  return updatePatientMutation;
}

export function usePatientCollectionEventAdd() {
  const queryClient = useQueryClient();
  const { patient } = usePatientStore();

  const collectionEventMutation = useMutation(
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

  return collectionEventMutation;
}

export function usePatientCommentAdd() {
  const queryClient = useQueryClient();
  const { patient } = usePatientStore();

  const commentMutation = useMutation(
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

  return commentMutation;
}
