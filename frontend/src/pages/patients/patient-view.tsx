import { Alert, AlertDescription, AlertTitle } from '@app/components/alert';
import { BiobankButton } from '@app/components/biobank-button';
import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { Button } from '@app/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@app/components/ui/card';
import { usePatient } from '@app/hooks/use-patient';
import { faArrowRight, faWarning } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import format from 'date-fns/format';
import { Link, useParams } from 'react-router-dom';
import { AdminPage } from '../admin-page';

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
    console.log(patientQry.error);
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

        <div className="grid grid-cols-1 gap-8 md:grid-cols-3">
          {patient.collectionEvents.map((cevent) => (
            <Card key={cevent.id} className="bg-gray-300">
              <CardHeader className="py-2">
                <CardTitle className="text-md grid grid-cols-1 md:grid-cols-2">Visit {cevent.visitNumber}</CardTitle>
              </CardHeader>
              <CardContent className="grid grid-cols-2 gap-2 bg-gray-100 p-4 py-2 text-sm text-gray-500">
                <p>Created</p>
                <p className="place-self-end text-gray-900">{format(cevent.createdAt, 'yyyy-MM-dd')}</p>
                <p>Specimen Count</p>
                <p className="place-self-end text-gray-900">{cevent.specimenCount}</p>
                <p>Aliquot Count</p>
                <p className="place-self-end text-gray-900">{cevent.aliquotCount}</p>
                <p>Status</p>
                <p className="place-self-end text-gray-900">{cevent.status}</p>
              </CardContent>
              <CardFooter className="bg-gray-100 px-2 py-2">
                <div className="text-md w-full">
                  <Link to="" className="place-self-end">
                    <Button className="w-full bg-sky-500 text-xs hover:bg-sky-400" size="sm">
                      View
                      <FontAwesomeIcon icon={faArrowRight} className="ml-2" />
                    </Button>
                  </Link>
                </div>
              </CardFooter>
            </Card>
          ))}
        </div>
      </div>
    </AdminPage>
  );
}
