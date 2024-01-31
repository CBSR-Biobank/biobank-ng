import { CollectionEventApi } from '@app/api/collection-event-api';
import { Annotations } from '@app/components/annotations/annotation';
import { BackButton } from '@app/components/back-button';
import { Chip } from '@app/components/chip';
import { CircularProgress } from '@app/components/circular-progress';
import { CollectionEventComments } from '@app/components/collection-events/collection-event-comments';
import { CommentAddDialog } from '@app/components/comment-add-dialog';
import { EntityDeleteDialog } from '@app/components/entity-delete-dialog';
import { EntityProperty } from '@app/components/entity-property';
import { InfoCard } from '@app/components/info-card';
import { OkButton } from '@app/components/ok-button';
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
  DialogTrigger
} from '@app/components/ui/dialog';
import { useCollectionEvent } from '@app/hooks/use-collection-event';
import { CommentAdd } from '@app/models/comment';
import { Patient } from '@app/models/patient';
import { usePatientStore } from '@app/store';
import { cn } from '@app/utils';
import { faChevronRight, faTrash, faTriangleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { AdminPage } from '../admin-page';

function DeleteNotAllowed() {
  return (
    <Dialog>
      <DialogTrigger className="focus-visible:outline-destructive-600 flex h-10 items-center gap-1 rounded-md bg-destructive px-4 text-sm font-normal text-destructive-foreground hover:bg-destructive/90">
        <FontAwesomeIcon icon={faTrash} />
        Delete Visit
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

const CollectionEventViewInternal: React.FC<{ patient: Patient; vnumber: number }> = ({ patient, vnumber }) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const collectionEventQry = useCollectionEvent(patient.pnumber, vnumber);
  const { data: collectionEvent } = collectionEventQry;
  const { setCollectionEvent } = usePatientStore();
  const [commentsSectionIsOpen, setCommentsSectionIsOpen] = useState(false);

  const deleteCollectionEvent = useMutation(() => CollectionEventApi.delete(patient, vnumber), {
    onSuccess: () => {
      queryClient.invalidateQueries(['patients', patient?.pnumber]);
    }
  });

  const addComment = useMutation(
    (newComment: CommentAdd) => CollectionEventApi.addComment(patient.pnumber, vnumber, newComment),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['patients', patient?.pnumber, 'collection-events', vnumber]);
      }
    }
  );

  useEffect(() => {
    if (collectionEvent) {
      setCollectionEvent(collectionEvent);
    }
  }, [collectionEvent]);

  if (collectionEventQry.isError) {
    return <ShowError error={collectionEventQry.error} />;
  }

  if (!patient || collectionEventQry.isLoading || !collectionEvent) {
    return <CircularProgress />;
  }

  const deleteAllowed = collectionEvent.sourceSpecimens.length <= 0;

  const handlePropChange = (_propertyName: string) => {
    // setPropertyToUpdate(propertyName);
    // setOpen(true);
  };

  const handleDelete = (result?: boolean) => {
    if (result) {
      deleteCollectionEvent.mutate();
      navigate('..');
    }
  };

  const handleCommentAdded = (newComment: string) => {
    addComment.mutate({ message: newComment });
  };

  const backClicked = () => {
    navigate('..');
  };

  return (
    <>
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-sm font-semibold text-gray-400">
            {patient.studyNameShort} Patient: {patient.pnumber}
          </p>
          <p className="text-4xl font-semibold text-sky-600">Visit {vnumber}</p>
        </AdminPage.Title>

        <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
          <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
            <EntityProperty propName="patientNumber" label="Patient Number" allowChanges={false}>
              {collectionEvent.patientNumber}
            </EntityProperty>

            <EntityProperty propName="visitNumber" label="Visit" allowChanges={true} handleChange={handlePropChange}>
              {collectionEvent.visitNumber}
            </EntityProperty>

            <EntityProperty propName="studyNameShort" label="Study" allowChanges={false}>
              {collectionEvent.studyNameShort}
            </EntityProperty>

            <EntityProperty propName="status" label="Status" allowChanges={true} handleChange={handlePropChange}>
              <StatusChip status={collectionEvent.status} size="xs" />
            </EntityProperty>

            <Annotations annotations={collectionEvent.annotations} onClick={handlePropChange} />
          </div>

          <Collapsible
            open={commentsSectionIsOpen}
            onOpenChange={setCommentsSectionIsOpen}
            className="rounded-md border-2 border-solid"
          >
            <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
              <h4 className="text-sm text-slate-700">Comments: {collectionEvent.commentCount}</h4>
              <CollapsibleTrigger asChild>
                <Button variant="ghost" size="sm" className="w-9 p-0">
                  <FontAwesomeIcon
                    icon={faChevronRight}
                    className={cn('duration-300 ease-in-out', { 'rotate-90': commentsSectionIsOpen })}
                  />
                  <span className="sr-only">Toggle</span>
                </Button>
              </CollapsibleTrigger>
            </div>
            <CollapsibleContent>
              {collectionEvent.commentCount <= 0 ? (
                <InfoCard className="p-2" title="No Comments" message="This visit has no comments on record" />
              ) : (
                <CollectionEventComments pnumber={patient.pnumber} vnumber={collectionEvent.visitNumber} />
              )}
            </CollapsibleContent>
          </Collapsible>

          {collectionEvent.sourceSpecimens.length > 0 && (
            <div className="space-y-2 rounded-md border-2 border-solid">
              <h4 className="bg-gray-300/50 px-4 py-2 text-sm font-normal text-slate-700">Specimens</h4>
              <SourceSpecimenTable specimens={collectionEvent.sourceSpecimens} />
            </div>
          )}
        </div>
        <div className="flex flex-col gap-3 pt-8 md:w-max md:flex-row">
          <BackButton onClick={backClicked} />
          {deleteAllowed ? (
            <EntityDeleteDialog buttonLabel="Delete Visit" onClose={handleDelete}>
              <div className="grid grid-cols-1 place-items-center gap-4">
                <p>Are you sure you want to delete this visit?</p>
                <div className="grid grid-cols-1 place-items-center">
                  <Chip message={`Visit ${collectionEvent.visitNumber}`} variant="default" size="sm" />
                </div>
              </div>
            </EntityDeleteDialog>
          ) : (
            <DeleteNotAllowed />
          )}
          <CommentAddDialog title="Visit" onSubmit={handleCommentAdded} />
        </div>
      </AdminPage>
    </>
  );
};

export function CollectionEventView() {
  const params = useParams();
  const vnumber = params.vnumber ? parseInt(params?.vnumber) : undefined;
  const { patient } = usePatientStore();

  if (!vnumber) {
    return <ShowError message="visit number is invalid" />;
  }

  if (!patient) {
    return <ShowError message="patient is invalid" />;
  }

  return <CollectionEventViewInternal patient={patient} vnumber={vnumber} />;
}
