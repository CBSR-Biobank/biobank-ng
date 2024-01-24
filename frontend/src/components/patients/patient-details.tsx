import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { AdminPage } from '@app/pages/admin-page';
import { CollectionEventTable } from '@app/pages/collection-events/collection-event-table';
import { usePatientStore } from '@app/store';
import { cn } from '@app/utils';
import { faChevronRight, faComment, faPlusCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import format from 'date-fns/format';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { BackButton } from '../back-button';
import { Button } from '../ui/button';
import { PatientComments } from './patient-comments';
import { PatientPropertyChanger } from './patient-property-changer';

export function PatientDetails() {
  const navigate = useNavigate();
  const { patient, setCollectionEvent } = usePatientStore();
  const [open, setOpen] = useState(false);
  const [propertyToUpdate, setPropertyToUpdate] = useState<string | null>(null);
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    setCollectionEvent(undefined);
  }, []);

  const handlePropChange = (propertyName: string) => {
    setPropertyToUpdate(propertyName);
    setOpen(true);
  };

  const handleUpdate = () => {
    setOpen(false);
  };

  const backClicked = () => {
    navigate('/patients');
  };

  if (!patient) {
    return <CircularProgress />;
  }

  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-sm font-semibold text-gray-400">{patient.studyNameShort} Patient</p>
        <p className="text-4xl font-semibold text-sky-600">{patient.pnumber}</p>
      </AdminPage.Title>

      <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
        <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
          <EntityProperty propName="pnumber" label="Patient Number" allowChanges handleChange={handlePropChange}>
            {patient.pnumber}
          </EntityProperty>

          <EntityProperty propName="study" label="Study" allowChanges handleChange={handlePropChange}>
            {patient.studyNameShort}
          </EntityProperty>

          <EntityProperty propName="createdAt" label="Created" allowChanges handleChange={handlePropChange}>
            {format(patient.createdAt, 'yyyy-MM-dd')}
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

        <Collapsible open={isOpen} onOpenChange={setIsOpen} className="rounded-md border-2 border-solid">
          <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
            <h4 className="text-sm text-slate-700">Comments: {patient.commentCount}</h4>
            <CollapsibleTrigger asChild>
              <Button variant="ghost" size="sm" className="w-9 p-0">
                <FontAwesomeIcon
                  icon={faChevronRight}
                  className={cn('duration-300 ease-in-out', { 'rotate-90': isOpen })}
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
      <div className="flex gap-4 pt-8">
        <BackButton onClick={backClicked} />
        <Button variant="secondary" icon={faPlusCircle}>
          Add Visit
        </Button>
        <Button variant="secondary" icon={faComment}>
          Add Comment
        </Button>
      </div>

      {open && propertyToUpdate && (
        <PatientPropertyChanger patient={patient} propertyToUpdate={propertyToUpdate} onClose={handleUpdate} />
      )}
    </AdminPage>
  );
}
