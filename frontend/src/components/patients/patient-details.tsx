import { CircularProgress } from '@app/components/circular-progress';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { useCollectionEventAdd } from '@app/hooks/use-collection-event';
import { usePatientCommentAdd, usePatientUpdate } from '@app/hooks/use-patient';
import { CollectionEventAdd } from '@app/models/collection-event';
import { PatientUpdate, takenVisitNumbers } from '@app/models/patient';
import { AdminPage } from '@app/pages/admin-page';
import { usePatientStore } from '@app/store';
import { cn } from '@app/utils';

import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { BackButton } from '../back-button';
import { CollectionEventTable } from '../collection-events/collection-event-table';
import { CommentAddDialog } from '../comment-add-dialog';
import { EntityProperty } from '../entity-property';
import { MutatorDialog } from '../mutators/mutator-dialog';
import { MutatorText } from '../mutators/mutator-text';
import { MutatorStudy } from '../studies/mutator-study';
import { Button } from '../ui/button';
import { PatientComments } from './patient-comments';
import { VisitAddDialog } from './visit-add-dialog';

export function PatientDetails() {
  const navigate = useNavigate();
  const { patient } = usePatientStore();
  const collectionEventAddMutation = useCollectionEventAdd();
  const commentAddMutation = usePatientCommentAdd();
  const [commentsSectionIsOpen, setCommentsSectionIsOpen] = useState(false);
  const updatePatientMutation = usePatientUpdate();

  const backClicked = () => {
    navigate('/patients');
  };

  const handleVisitAdd = (newVisitNumber: number) => {
    if (!patient) {
      throw new Error('patient is invalid');
    }

    const pnumber = patient?.pnumber;
    const newVisit: CollectionEventAdd = { vnumber: newVisitNumber };

    collectionEventAddMutation.mutate(
      { pnumber, newVisit },
      {
        onSuccess: () => {
          navigate(`${newVisitNumber}`);
        },
      }
    );
  };

  const handleCommentAdd = (newComment: string) => {
    if (newComment.trim().length <= 0) {
      throw new Error('comment is empty');
    }
    commentAddMutation.mutate({ message: newComment });
  };

  const handleUpdate = (updated: PatientUpdate) => {
    if (!patient) {
      throw new Error('patient is invalid');
    }

    updatePatientMutation.mutate(updated, {
      onSuccess: (updated) => {
        if (updated.pnumber != patient.pnumber) {
          navigate(`../${updated.pnumber}`);
        }
      },
    });
  };

  const handlePnumberUpdated = (value?: string) => {
    if (!value) {
      throw new Error('new pnumber value is invalid');
    }
    if (!patient) {
      throw new Error('patient is invalid');
    }
    handleUpdate({ pnumber: value, studyNameShort: patient.studyNameShort });
  };

  const handleStudyUpdated = (value?: string) => {
    if (!value) {
      throw new Error('new study name short value is invalid');
    }
    if (!patient) {
      throw new Error('patient is invalid');
    }
    handleUpdate({ pnumber: patient.pnumber, studyNameShort: value });
  };

  if (!patient) {
    return <CircularProgress />;
  }

  return (
    <>
      <AdminPage.Title hasBorder>
        <p className="text-sm font-semibold text-gray-400">{patient.studyNameShort} Patient</p>
        <p className="text-4xl font-semibold text-sky-600">{patient.pnumber}</p>
      </AdminPage.Title>

      <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
          <EntityProperty
            propName="pnumber"
            label="Patient Number"
            mutator={
              <MutatorDialog title="Update Patient">
                <MutatorText
                  label="Patient Number"
                  value={patient.pnumber}
                  required
                  maxlen={80}
                  onClose={handlePnumberUpdated}
                />
              </MutatorDialog>
            }
          >
            {patient.pnumber}
          </EntityProperty>

          <EntityProperty
            propName="studyNameShort"
            label="Study"
            mutator={
              <MutatorDialog title="Update Patient">
                <MutatorStudy label="Study" value={patient.studyNameShort} required onClose={handleStudyUpdated} />
              </MutatorDialog>
            }
          >
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
        <VisitAddDialog disallow={takenVisitNumbers(patient)} onSubmit={handleVisitAdd} />
        <CommentAddDialog onSubmit={handleCommentAdd} />
      </div>
    </>
  );
}
