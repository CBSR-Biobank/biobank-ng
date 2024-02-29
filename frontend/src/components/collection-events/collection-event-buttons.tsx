import { BackButton } from '@app/components/back-button';
import { Chip } from '@app/components/chip';
import { CommentAddDialog } from '@app/components/comment-add-dialog';
import { EntityDeleteDialog } from '@app/components/entity-delete-dialog';
import { useCeventCommentAdd, useCollectionEventDelete } from '@app/hooks/use-collection-event';
import { CollectionEvent } from '@app/models/collection-event';

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useSourceSpecimenAdd } from '@app/hooks/use-specimen';
import { SourceSpecimenAdd } from '@app/models/specimen';
import { SourceSpecimenAddDialog } from '../specimens/source-specimen-add-dialog';
import { SpecimenAddErrorDialog } from './specimen-add-error-dialog';
import { VisitDeleteNotAllowed } from './visit-delete-not-allowed';

export const CollectionEventButtons: React.FC<{ collectionEvent: CollectionEvent }> = ({ collectionEvent }) => {
  const navigate = useNavigate();
  const commentAddMutation = useCeventCommentAdd();
  const deleteMutation = useCollectionEventDelete();
  const deleteAllowed = collectionEvent.sourceSpecimens.length <= 0;
  const specimenaddMutation = useSourceSpecimenAdd();
  const [specimenAddError, setSpecimenAddError] = useState<unknown | undefined>(undefined);

  // TODO: add mutation to add a source specimen

  const backClicked = () => {
    navigate('..');
  };

  const handleCommentAdded = (newComment: string) => {
    commentAddMutation.mutate({ message: newComment });
  };

  const handleDelete = (result: boolean) => {
    if (!result) {
      return;
    }

    deleteMutation.mutate(collectionEvent, {
      onSuccess: () => {
        navigate('..');
      },
      onError: () => {
        navigate('./delete-no-privileges');
      },
    });
  };

  const handleSpecimenAdd = (specimen: SourceSpecimenAdd) => {
    specimenaddMutation.mutate(
      {
        ...specimen,
        pnumber: collectionEvent.pnumber,
        vnumber: collectionEvent.vnumber,
      },
      {
        onError: (error: any) => {
          // query client config will handle the 401 error
          if ('status' in error && error.status !== 401) {
            setSpecimenAddError(error);
            return;
          }
        },
      }
    );
  };

  const handleSpecimenAddErrorClosed = () => {
    setSpecimenAddError(undefined);
  };

  return (
    <div className="flex flex-col gap-3 pt-8 md:w-max md:flex-row">
      <BackButton onClick={backClicked} />
      {deleteAllowed ? (
        <EntityDeleteDialog buttonLabel="Delete Visit" onClose={handleDelete}>
          <div className="grid grid-cols-1 place-items-center gap-4">
            <p>Are you sure you want to delete this visit?</p>
            <div className="grid grid-cols-1 place-items-center">
              <Chip message={`Visit ${collectionEvent.vnumber}`} size="sm" />
            </div>
          </div>
        </EntityDeleteDialog>
      ) : (
        <VisitDeleteNotAllowed />
      )}
      <SourceSpecimenAddDialog
        studyNameShort={collectionEvent.studyNameShort}
        title="Visit"
        onSubmit={handleSpecimenAdd}
      />
      <CommentAddDialog title="Visit" onSubmit={handleCommentAdded} />
      {!!specimenAddError && (
        <SpecimenAddErrorDialog open={true} error={specimenAddError} onClose={handleSpecimenAddErrorClosed} />
      )}
    </div>
  );
};
