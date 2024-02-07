import { ApiError } from '@app/api/api';
import { PatientApi } from '@app/api/patient-api';
import { CommentAdd } from '@app/models/comment';
import { PatientAdd, PatientUpdate } from '@app/models/patient';
import { usePatientStore } from '@app/store';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';

export function usePatient(pnumber: string) {
  const { setPatient } = usePatientStore();
  const query = useQuery({
    queryKey: ['patients', pnumber],
    queryFn: async () => PatientApi.getByPnumber(pnumber),
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

export function usePatientAdd() {
  const queryClient = useQueryClient();

  const addPatientMutation = useMutation({
    mutationFn: (patient: PatientAdd) => PatientApi.add(patient),
    onSuccess: (data, _variables, _context) => {
      queryClient.setQueryData(['patients', data.id], data);
      queryClient.invalidateQueries({ queryKey: ['patients'] });
    }
  });
  return addPatientMutation;
}

export function usePatientUpdate() {
  const queryClient = useQueryClient();
  const { patient } = usePatientStore();

  const updatePatientMutation = useMutation({
    mutationFn: (updatedPatient: PatientUpdate) => {
      if (!patient) {
        throw new Error('patient is invalid');
      }
      return PatientApi.update(patient.pnumber, updatedPatient);
    },
    onSuccess: (data, variables, _context) => {
      // check if the patient number was updated
      if (variables.pnumber != data.pnumber) {
        queryClient.removeQueries({ queryKey: ['patients', variables.pnumber] });
      } else {
        queryClient.setQueryData(['patients', variables.pnumber], () => data);
      }

      queryClient.invalidateQueries({ queryKey: ['patients', variables.pnumber] });
    }
  });
  return updatePatientMutation;
}

export function usePatientComments(pnumber: string) {
  return useQuery({
    queryKey: ['patients', pnumber, 'comments'],
    queryFn: async () => PatientApi.getPatientComments(pnumber),
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400
  });
}

export function usePatientCommentAdd() {
  const queryClient = useQueryClient();
  const { patient } = usePatientStore();

  const commentMutation = useMutation({
    mutationFn: (newComment: CommentAdd) => {
      if (!patient) {
        return Promise.resolve(null);
      }

      return PatientApi.addComment(patient, newComment);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['patients', patient?.pnumber] });
    }
  });

  return commentMutation;
}
