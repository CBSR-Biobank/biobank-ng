import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { AdminPage } from '@app/pages/admin-page';
import { CollectionEventTable } from '@app/pages/collection-events/collection-event-table';
import { usePatientStore } from '@app/store';
import format from 'date-fns/format';
import { useEffect } from 'react';
import { Outlet } from 'react-router-dom';

export function PatientDetails() {
  const { patient, setCollectionEvent } = usePatientStore();

  useEffect(() => {
    setCollectionEvent(undefined);
  }, []);

  const handlePropChange = (_propertyName: string) => {
    // setPropertyToUpdate(propertyName);
    // setOpen(true);
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
          <EntityProperty propName="pnumber" label="Patient Number" allowChanges={true} handleChange={handlePropChange}>
            {patient.pnumber}
          </EntityProperty>

          <EntityProperty propName="study" label="Study" allowChanges={false}>
            {patient.studyNameShort}
          </EntityProperty>

          <EntityProperty propName="createdAt" label="Created" allowChanges={true} handleChange={handlePropChange}>
            {format(patient.createdAt, 'yyyy-MM-dd')}
          </EntityProperty>
        </div>

        <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
          <EntityProperty propName="specimenCount" label="Specimen Count" allowChanges={false}>
            {patient.specimenCount}
          </EntityProperty>

          <EntityProperty propName="aliquotCount" label="Aliquot Count" allowChanges={false}>
            {patient.aliquotCount}
          </EntityProperty>
        </div>

        <CollectionEventTable collectionEvents={patient.collectionEvents} />
        <Outlet />
      </div>
    </AdminPage>
  );
}
