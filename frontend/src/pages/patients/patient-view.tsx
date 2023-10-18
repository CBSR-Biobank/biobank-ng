import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { Button } from '@app/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@app/components/ui/card';
import { usePatient } from '@app/hooks/use-patient';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
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

        <div className="grid grid-cols-3 gap-8">
          {patient.collectionEvents.map((cevent) => (
            <Card key={cevent.id} className="bg-gray-300">
              <CardHeader className="py-2">
                <CardTitle className="text-md grid grid-cols-2">
                  Collection Event #{cevent.visitNumber}
                  <Link to="" className="place-self-end">
                    <Button className="bg-sky-500">
                      View
                      <FontAwesomeIcon icon={faArrowRight} className="ml-2" />
                    </Button>
                  </Link>
                </CardTitle>
              </CardHeader>
              <CardContent className="grid grid-cols-2 gap-2 py-2 text-sm text-gray-500">
                <p>Specimen Count</p>
                <p className="place-self-end text-gray-900">{cevent.specimenCount}</p>
                <p>Aliquot Count</p>
                <p className="place-self-end text-gray-900">{cevent.aliquotCount}</p>
                <p>Status</p>
                <p className="place-self-end text-gray-900">{cevent.status}</p>
              </CardContent>
              <CardFooter className="py-2"></CardFooter>
            </Card>
          ))}
        </div>
      </div>
    </AdminPage>
  );
}
