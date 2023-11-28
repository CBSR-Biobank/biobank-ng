import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { CircularProgress } from '@app/components/circular-progress';
import { PatientNotExist } from '@app/components/patients/patient-not-exist';
import { ShowError } from '@app/components/show-error';
import { usePatient } from '@app/hooks/use-patient';
import { usePatientStore } from '@app/store';
import { useEffect } from 'react';
import { Outlet, useParams } from 'react-router-dom';

export function PatientPage() {
  const params = useParams();
  const { setPatient } = usePatientStore();
  const patientQry = usePatient(params.pnumber);
  const { data: patient } = patientQry;
  const doesNotExist = patientQry.isError && patientQry.error.status === 404;

  useEffect(() => {
    if (patient) {
      setPatient(patient);
    }
  }, [patient]);

  if (!params.pnumber) {
    return <ShowError message="pnumber is invalid" />;
  }

  if (patientQry.isLoading) {
    return <CircularProgress />;
  }

  return (
    <>
      <PatientBreadcrumbs />
      {doesNotExist ? <PatientNotExist pnumber={params.pnumber} /> : <Outlet />}
    </>
  );
}
