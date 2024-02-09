import { StudyApi } from '@app/api/study-api';
import { Status } from '@app/models/status';
import { keepPreviousData, useQuery } from '@tanstack/react-query';

export function useStudyNames() {
  const query = useQuery({
    queryKey: ['studies', 'names'],
    queryFn: async () => StudyApi.names(Status.ACTIVE),
    placeholderData: keepPreviousData
  });

  return {
    studyNames: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error
  };
}

export function useStudyAnnotationTypes(nameshort: string) {
  const query = useQuery({
    queryKey: ['studies', nameshort, 'attribute-types'],
    queryFn: async () => StudyApi.annotationTypes(nameshort, Status.ACTIVE),
    placeholderData: keepPreviousData
  });

  return {
    annotationTypes: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error
  };
}
