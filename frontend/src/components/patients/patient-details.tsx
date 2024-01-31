import { CollectionEventApi } from '@app/api/collection-event-api';
import { PatientApi } from '@app/api/patient-api';
import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { CollectionEventAdd } from '@app/models/collection-event';
import { CommentAdd } from '@app/models/comment';
import { AdminPage } from '@app/pages/admin-page';
import { CollectionEventTable } from '@app/pages/collection-events/collection-event-table';
import { usePatientStore } from '@app/store';
import { cn } from '@app/utils';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { BackButton } from '../back-button';
import { CommentAddDialog } from '../comment-add-dialog';
import { Button } from '../ui/button';
import { PatientComments } from './patient-comments';
import { PatientMutator } from './patient-mutator';
import { VisitAddDialog } from './visit-add-dialog';

export function PatientDetails() {
  const navigate = useNavigate();
  const { patient, setCollectionEvent } = usePatientStore();
  const [propertyToUpdate, setPropertyToUpdate] = useState<string | null>(null);
  const [mutatorOpen, setMutatorOpen] = useState(false);
  const [commentsSectionIsOpen, setCommentsSectionIsOpen] = useState(false);

  const queryClient = useQueryClient();

  const addCollectionEvent = useMutation(
    (newVisit: CollectionEventAdd) => {
      if (!patient) {
        return Promise.resolve(null);
      }
      return CollectionEventApi.add(patient, newVisit);
    },
    {
      onSuccess: (newCevent) => {
        queryClient.invalidateQueries(['patients', patient?.pnumber]);
        if (newCevent) {
          navigate(`${newCevent.visitNumber}`);
        }
      },
      onError: (error) => {
        console.log('here', error);
      }
    }
  );

  const addComment = useMutation(
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

  useEffect(() => {
    setCollectionEvent(undefined);
  }, []);

  const handlePropChange = (propertyName: string) => {
    setPropertyToUpdate(propertyName);
    setMutatorOpen(true);
  };

  const handleUpdate = () => {
    setMutatorOpen(false);
  };

  const backClicked = () => {
    navigate('/patients');
  };

  const handleVisitAdded = (newVisitNumber: number) => {
    addCollectionEvent.mutate({ visitNumber: newVisitNumber });
  };

  const handleCommentAdded = (newComment: string) => {
    addComment.mutate({ message: newComment });
  };

  if (!patient) {
    return <CircularProgress />;
  }

  var disallowVisitNumbers = patient.collectionEvents.map((ce) => ce.visitNumber);

  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-sm font-semibold text-gray-400">{patient.studyNameShort} Patient</p>
        <p className="text-4xl font-semibold text-sky-600">{patient.pnumber}</p>
      </AdminPage.Title>

      <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
          <EntityProperty propName="pnumber" label="Patient Number" allowChanges handleChange={handlePropChange}>
            {patient.pnumber}
          </EntityProperty>

          <EntityProperty propName="studyNameShort" label="Study" allowChanges handleChange={handlePropChange}>
            {patient.studyNameShort}
          </EntityProperty>
        </div>

        <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
          <EntityProperty propName="specimenCount" label="Specimen Count">
            {patient.specimenCount}
          </EntityProperty>

          <EntityProperty propName="aliquotCount" label="Aliquot Count">
            {patient.aliquotCount}
          </EntityProperty>
        </div>

        <Collapsible
          open={commentsSectionIsOpen}
          onOpenChange={setCommentsSectionIsOpen}
          className="rounded-md border-2 border-solid"
        >
          <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
            <h4 className="text-sm text-slate-700">Comments: {patient.commentCount}</h4>
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
          <CollapsibleContent className="space-y-2">
            <PatientComments pnumber={patient.pnumber} />
          </CollapsibleContent>
        </Collapsible>

        <CollectionEventTable collectionEvents={patient.collectionEvents} />
      </div>
      <div className="flex flex-col gap-3 pt-8 md:w-max md:flex-row">
        <BackButton onClick={backClicked} className="md:w-min" />
        <VisitAddDialog disallow={disallowVisitNumbers} onSubmit={handleVisitAdded} />
        <CommentAddDialog onSubmit={handleCommentAdded} />
      </div>

      {mutatorOpen && propertyToUpdate && (
        <PatientMutator patient={patient} propertyToUpdate={propertyToUpdate} onClose={handleUpdate} />
      )}
    </AdminPage>
  );
}
