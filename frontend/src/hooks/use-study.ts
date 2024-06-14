import { StudyApi } from '@app/api/study-api';
import { Status } from '@app/models/status';

import { keepPreviousData, useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

export function useStudyNames() {
  const query = useQuery({
    queryKey: ['studies', 'names'],
    queryFn: async () => StudyApi.names(Status.ACTIVE),
    placeholderData: keepPreviousData,
  });

  return {
    studyNames: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error,
  };
}

export function useCatalogueRequest() {
  const queryClient = useQueryClient();

  const addPatientMutation = useMutation({
    mutationFn: (nameShort: string) => StudyApi.catalogue(nameShort),
    onSuccess: (data, _variables, _context) => {
      queryClient.setQueryData(['studies', 'catalogue', data], data);
      queryClient.invalidateQueries({ queryKey: ['studies', 'catalogue'] });
    },
  });
  return addPatientMutation;
}

export function useStudyAnnotationTypes(nameshort: string) {
  const query = useQuery({
    queryKey: ['studies', nameshort, 'attribute-types'],
    queryFn: async () => StudyApi.annotationTypes(nameshort, Status.ACTIVE),
    placeholderData: keepPreviousData,
  });

  return {
    annotationTypes: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error,
  };
}

export function useStudySourceSpecimenTypes(nameshort: string) {
  const query = useQuery({
    queryKey: ['studies', nameshort, 'source-specimen-types'],
    queryFn: async () => StudyApi.sourceSpecimenTypes(nameshort),
    placeholderData: keepPreviousData,
  });

  return {
    sourceSpecimenTypes: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error,
  };
}
