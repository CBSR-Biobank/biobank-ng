import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { usePatientCollectionEventAdd, usePatientCommentAdd } from '@app/hooks/use-patient';
import { AdminPage } from '@app/pages/admin-page';
import { CollectionEventTable } from '@app/pages/collection-events/collection-event-table';
import { usePatientStore } from '@app/store';
import { cn } from '@app/utils';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { BackButton } from '../back-button';
import { CommentAddDialog } from '../comment-add-dialog';
import { Button } from '../ui/button';
import { PatientComments } from './patient-comments';
import { PatientMutator } from './patient-mutator';
import { VisitAddDialog } from './visit-add-dialog';

export function PatientDetails() {
  const navigate = useNavigate();
  const { patient } = usePatientStore();
  const collectionEventAddMutation = usePatientCollectionEventAdd();
  const commentAddMutation = usePatientCommentAdd();
  const [propertyToUpdate, setPropertyToUpdate] = useState<string | null>(null);
  const [mutatorOpen, setMutatorOpen] = useState(false);
  const [commentsSectionIsOpen, setCommentsSectionIsOpen] = useState(false);

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

  const handleVisitAdd = (newVisitNumber: number) => {
    collectionEventAddMutation.mutate(
      { visitNumber: newVisitNumber },
      {
        onSuccess: () => {
          navigate(`${newVisitNumber}`);
        }
      }
    );
  };

  const handleCommentAdd = (newComment: string) => {
    commentAddMutation.mutate({ message: newComment });
  };

  if (!patient) {
    return <CircularProgress />;
  }

  const disallowVisitNumbers = patient.collectionEvents.map((ce) => ce.visitNumber);

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
        <VisitAddDialog disallow={disallowVisitNumbers} onSubmit={handleVisitAdd} />
        <CommentAddDialog onSubmit={handleCommentAdd} />
      </div>

      {mutatorOpen && propertyToUpdate && (
        <PatientMutator patient={patient} propertyToUpdate={propertyToUpdate} onClose={handleUpdate} />
      )}
    </AdminPage>
  );
}
