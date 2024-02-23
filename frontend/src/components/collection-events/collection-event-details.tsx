import { Annotations } from '@app/components/annotations/annotation';
import { BackButton } from '@app/components/back-button';
import { Chip } from '@app/components/chip';
import { CircularProgress } from '@app/components/circular-progress';
import { CollectionEventComments } from '@app/components/collection-events/collection-event-comments';
import { CommentAddDialog } from '@app/components/comment-add-dialog';
import { EntityDeleteDialog } from '@app/components/entity-delete-dialog';
import { EntityProperty } from '@app/components/entity-property';
import { InfoCard } from '@app/components/info-card';
import { MutatorDialog } from '@app/components/mutators/mutator-dialog';
import { MutatorStatus } from '@app/components/mutators/mutator-status';
import { OkButton } from '@app/components/ok-button';
import { MutatorVisitNumber } from '@app/components/patients/mutator-visit-number';
import { ShowError } from '@app/components/show-error';
import { SourceSpecimenTable } from '@app/components/specimens/source-specimens-table';
import { StatusChip } from '@app/components/status-chip';
import { Button } from '@app/components/ui/button';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@app/components/ui/dialog';
import {
  useCeventCommentAdd,
  useCollectionEvent,
  useCollectionEventDelete,
  useCollectionEventUpdate,
} from '@app/hooks/use-collection-event';
import { useStudyAnnotationTypes } from '@app/hooks/use-study';
import { Annotation } from '@app/models/annotation';
import { CollectionEvent } from '@app/models/collection-event';
import { Patient, takenVisitNumbers } from '@app/models/patient';
import { Status } from '@app/models/status';
import { AdminPage } from '@app/pages/admin-page';
import { cn } from '@app/utils';

import { faChevronRight, faTrash, faTriangleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useEffect, useState } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import { SourceSpecimenAdd } from '@app/models/specimen';
import { SourceSpecimenAddDialog } from '../specimens/source-specimen-add-dialog';

function DeleteNotAllowed() {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="destructive" icon={faTrash}>
          Delete Visit
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-center">Cannot Delete</DialogTitle>
        </DialogHeader>
        <div className="flex gap-4">
          <FontAwesomeIcon icon={faTriangleExclamation} size="3x" className="text-red-500" />
          <div className="flex flex-col gap-2 self-center">
            <p>This visit cannot be deleted since it contains specimens.</p>
            <p>Remove the specimens if you want to delete it.</p>
          </div>
        </div>
        <DialogFooter className="pt-10 sm:justify-center">
          <DialogClose asChild>
            <OkButton type="button" />
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

const CommentsCollapsible: React.FC<{ pnumber: string; vnumber: number; count: number }> = ({
  pnumber,
  vnumber,
  count,
}) => {
  const [open, setOpen] = useState(false);

  const handleOpenChange = (isOpen: boolean) => {
    setOpen(isOpen);
  };

  return (
    <Collapsible onOpenChange={handleOpenChange} className="rounded-md border-2 border-solid">
      <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
        <h4 className="text-sm text-slate-700">Comments: {count}</h4>
        <CollapsibleTrigger asChild>
          <Button variant="ghost" size="sm" className="w-9 p-0">
            <FontAwesomeIcon icon={faChevronRight} className={cn('duration-300 ease-in-out', { 'rotate-90': open })} />
            <span className="sr-only">Toggle</span>
          </Button>
        </CollapsibleTrigger>
      </div>
      <CollapsibleContent>
        {count <= 0 ? (
          <InfoCard className="p-2" title="No Comments" message="This visit has no comments on record" />
        ) : (
          <CollectionEventComments pnumber={pnumber} vnumber={vnumber} />
        )}
      </CollapsibleContent>
    </Collapsible>
  );
};

const Buttons: React.FC<{ collectionEvent: CollectionEvent }> = ({ collectionEvent }) => {
  const navigate = useNavigate();
  const commentAddMutation = useCeventCommentAdd();
  const deleteMutation = useCollectionEventDelete();
  const deleteAllowed = collectionEvent.sourceSpecimens.length <= 0;

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
    console.log(specimen);
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
        <DeleteNotAllowed />
      )}
      <SourceSpecimenAddDialog
        studyNameShort={collectionEvent.studyNameShort}
        title="Visit"
        onSubmit={handleSpecimenAdd}
      />
      <CommentAddDialog title="Visit" onSubmit={handleCommentAdded} />
    </div>
  );
};

export const CollectionEventAnnotations: React.FC<{
  collectionEvent: CollectionEvent;
  onClick: (annotation: Annotation, value?: string) => void;
}> = ({ collectionEvent, onClick }) => {
  const { annotationTypes, isLoading, isError, error } = useStudyAnnotationTypes(collectionEvent.studyNameShort);

  if (isError) {
    return <ShowError error={error} />;
  }

  if (isLoading || !annotationTypes) {
    return <CircularProgress />;
  }

  return (
    <Annotations
      mutatorTitle="Update Visit"
      annotations={collectionEvent.annotations}
      annotationTypes={annotationTypes}
      onUpdate={onClick}
    />
  );
};

export const CollectionEventDetails: React.FC<{ patient: Patient; vnumber: number }> = ({ patient, vnumber }) => {
  const navigate = useNavigate();
  const { collectionEvent, isLoading, isError, error } = useCollectionEvent(patient.pnumber, vnumber);
  const updateMutation = useCollectionEventUpdate();

  useEffect(() => {
    if (isError && error) {
      if (error?.status === 404) {
        navigate('./not-exist');
      }
    }
  }, [isError, error]);

  if (isError && error) {
    if (error?.status !== 404) {
      return <ShowError error={error} />;
    }
  }

  if (isLoading) {
    return <CircularProgress />;
  }

  if (!collectionEvent) {
    return <Outlet />;
  }

  const handleVnumberUpdated = (value?: number) => {
    if (!value) {
      return; // do nothing
    }

    updateMutation.mutate(
      {
        pnumber: collectionEvent.pnumber,
        vnumber: collectionEvent.vnumber,
        cevent: { ...collectionEvent, vnumber: value },
      },
      {
        onSuccess: () => {
          navigate(`../${value}`);
        },
      }
    );
  };

  const handleStatusUpdated = (value?: Status) => {
    if (!value) {
      return; // do nothing
    }

    updateMutation.mutate({
      pnumber: collectionEvent.pnumber,
      vnumber: collectionEvent.vnumber,
      cevent: { ...collectionEvent, status: value },
    });
  };

  const handleAnnotationChange = (annotation: Annotation, value?: string) => {
    const annotations = collectionEvent.annotations.filter((a) => a.name !== annotation.name);
    annotations.push({ ...annotation, value: value ?? '' });
    updateMutation.mutate({
      pnumber: collectionEvent.pnumber,
      vnumber: collectionEvent.vnumber,
      cevent: { ...collectionEvent, annotations },
    });
  };

  return (
    <>
      <AdminPage.Title>
        <p className="text-sm font-semibold text-gray-400">
          {collectionEvent.studyNameShort} Patient: {patient.pnumber}
        </p>
        <p className="text-4xl font-semibold text-sky-600">Visit {vnumber}</p>
      </AdminPage.Title>

      <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
          <EntityProperty propName="patientNumber" label="Patient Number">
            {collectionEvent.pnumber}
          </EntityProperty>

          <EntityProperty propName="studyNameShort" label="Study">
            {collectionEvent.studyNameShort}
          </EntityProperty>

          <EntityProperty
            propName="visitNumber"
            label="Visit"
            mutator={
              <MutatorDialog title="Update Patient">
                <MutatorVisitNumber
                  vnumber={collectionEvent.vnumber}
                  disallow={takenVisitNumbers(patient)}
                  onClose={handleVnumberUpdated}
                />
              </MutatorDialog>
            }
          >
            {collectionEvent.vnumber}
          </EntityProperty>

          <EntityProperty
            propName="status"
            label="Status"
            mutator={
              <MutatorDialog title="Update Visit">
                <MutatorStatus label="Status" value={collectionEvent.status} onClose={handleStatusUpdated} required />
              </MutatorDialog>
            }
          >
            <StatusChip status={collectionEvent.status} size="xs" />
          </EntityProperty>

          <CollectionEventAnnotations collectionEvent={collectionEvent} onClick={handleAnnotationChange} />
        </div>

        <CommentsCollapsible
          pnumber={patient.pnumber}
          vnumber={collectionEvent.vnumber}
          count={collectionEvent.commentCount}
        />

        {collectionEvent.sourceSpecimens.length > 0 && (
          <div className="space-y-2 rounded-md border-2 border-solid">
            <h4 className="bg-gray-300/50 px-4 py-2 text-sm font-normal text-slate-700">Specimens</h4>
            <SourceSpecimenTable specimens={collectionEvent.sourceSpecimens} />
          </div>
        )}
      </div>

      <Buttons collectionEvent={collectionEvent} />
    </>
  );
};
