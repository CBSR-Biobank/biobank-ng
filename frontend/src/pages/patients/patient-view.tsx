import { Alert, AlertDescription, AlertTitle } from '@app/components/alert';
import { BiobankButton } from '@app/components/biobank-button';
import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { usePatient } from '@app/hooks/use-patient';
import { faWarning } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import format from 'date-fns/format';
import { Link, useParams } from 'react-router-dom';
import { AdminPage } from '../admin-page';
import { CollectionEventTable } from '../collection-events/collection-event-table';

export function PatientView() {
  const params = useParams();

  if (!params.pnumber) {
    throw new Error('pnumber is invalid');
  }

  const patientQry = usePatient(params.pnumber);
  const { data: patient } = patientQry;

  const handlePropChange = (_propertyName: string) => {
    // setPropertyToUpdate(propertyName);
    // setOpen(true);
  };

  if (patientQry.isError) {
    if (patientQry.error.status === 404) {
      return (
        <div className="grid grid-cols-1 gap-8">
          <Alert className="border-amber-500 bg-amber-200">
            <FontAwesomeIcon icon={faWarning} />
            <AlertTitle>Patient Does Not Exist</AlertTitle>
            <AlertDescription>
              The patient with number <b>{params.pnumber}</b> is not in the system.
            </AlertDescription>
          </Alert>
          <div className="flex gap-8">
            <Link to="/patients">
              <BiobankButton>Back</BiobankButton>
            </Link>
            <Link to="/patients/add">
              <BiobankButton className="w-[10rem] whitespace-nowrap">Add Patient</BiobankButton>
            </Link>
          </div>
        </div>
      );
    }
    return <>Error</>;
  }

  if (patientQry.isLoading || !patient) {
    return <CircularProgress />;
  }

  return (
    <>
      <PatientBreadcrumbs patient={patient} />
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-sm text-gray-400">Patient</p>
          <p className="text-4xl font-semibold text-sky-600">{patient.pnumber}</p>
        </AdminPage.Title>

        <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
          <EntityProperty propName="pnumber" label="Patient Number" allowChanges={true} handleChange={handlePropChange}>
            {patient.pnumber}
          </EntityProperty>

          <EntityProperty propName="createdAt" label="Created" allowChanges={true} handleChange={handlePropChange}>
            {format(patient.createdAt, 'yyyy-MM-dd')}
          </EntityProperty>

          <EntityProperty propName="study" label="Study" allowChanges={false}>
            {patient.studyNameShort}
          </EntityProperty>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <EntityProperty propName="specimenCount" label="Specimen Count" allowChanges={false}>
              {patient.specimenCount}
            </EntityProperty>

            <EntityProperty propName="aliquotCount" label="Aliquot Count" allowChanges={false}>
              {patient.aliquotCount}
            </EntityProperty>
          </div>

          <CollectionEventTable collectionEvents={patient.collectionEvents} />
        </div>
      </AdminPage>
    </>
  );
}
