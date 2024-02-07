import { ApiError } from '@app/api/api';
import { CollectionEventApi } from '@app/api/collection-event-api';
import { CollectionEventAdd, CollectionEventUpdate } from '@app/models/collection-event';
import { CommentAdd } from '@app/models/comment';
import { usePatientStore } from '@app/store';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';

export function useCollectionEvent(pnumber: string, vnumber: number) {
  const { setCollectionEvent } = usePatientStore();
  const query = useQuery({
    queryKey: ['patients', pnumber, 'collection-events', vnumber],
    queryFn: async () => CollectionEventApi.getCollectionEvent(pnumber, vnumber),
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404
  });
  const { data: collectionEvent } = query;

  useEffect(() => {
    if (collectionEvent) {
      setCollectionEvent(collectionEvent);
    }
  }, [collectionEvent]);

  return {
    collectionEvent,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error
  };
}

export function useCollectionEventComments(pnumber: string, vnumber: number) {
  return useQuery({
    queryKey: ['patients', pnumber, 'collection-events', vnumber, 'comments'],
    queryFn: async () => CollectionEventApi.getComments(pnumber, vnumber),
    retry: (count, error: ApiError) => count <= 3 && error.status !== 404 && error.status !== 400
  });
}

export function useCollectionEventAdd() {
  const queryClient = useQueryClient();

  const collectionEventMutation = useMutation({
    mutationFn: ({ pnumber, newVisit }: { pnumber: string; newVisit: CollectionEventAdd }) =>
      CollectionEventApi.add(pnumber, newVisit),
    onSuccess: (_data, variables, _context) => {
      queryClient.invalidateQueries({ queryKey: ['patients', variables.pnumber] });
    }
  });

  return collectionEventMutation;
}

export function useCollectionEventUpdate() {
  const queryClient = useQueryClient();

  const updateMutation = useMutation({
    mutationFn: ({ pnumber, vnumber, cevent }: { pnumber: string; vnumber: number; cevent: CollectionEventUpdate }) =>
      CollectionEventApi.update(pnumber, vnumber, cevent),
    onSuccess: (data, variables, _context) => {
      // check if the visit number was updated
      if (variables.vnumber != data.vnumber) {
        queryClient.removeQueries({
          queryKey: ['patients', variables.pnumber, 'collection-events', variables.vnumber]
        });
      } else {
        queryClient.setQueryData(['patients', variables.pnumber, 'collection-events', variables.vnumber], () => data);
      }

      queryClient.invalidateQueries({ queryKey: ['patients', variables.pnumber] });
    }
  });

  return updateMutation;
}

export function useCollectionEventDelete() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ pnumber, vnumber }: { pnumber: string; vnumber: number }) =>
      CollectionEventApi.delete(pnumber, vnumber),
    onSuccess: (_data, variables, _context) => {
      queryClient.removeQueries({ queryKey: ['patients', variables.pnumber, 'collection-events', variables.vnumber] });
      queryClient.invalidateQueries({ queryKey: ['patients', variables.pnumber] });
    }
  });
}

export function useCeventCommentAdd() {
  const queryClient = useQueryClient();
  const { collectionEvent: cevent } = usePatientStore();

  const commentMutation = useMutation({
    mutationFn: (newComment: CommentAdd) => {
      if (!cevent) {
        return Promise.resolve(null);
      }
      return CollectionEventApi.addComment(cevent.pnumber, cevent.vnumber, newComment);
    },
    onSuccess: () => {
      if (cevent) {
        queryClient.invalidateQueries({ queryKey: ['patients', cevent.pnumber, 'collection-events', cevent.vnumber] });
      }
    }
  });

  return commentMutation;
}
