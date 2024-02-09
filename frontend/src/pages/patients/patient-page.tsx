import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { CircularProgress } from '@app/components/circular-progress';
import { PatientNotExist } from '@app/components/patients/patient-not-exist';
import { ShowError } from '@app/components/show-error';
import { usePatient } from '@app/hooks/use-patient';
import { Outlet, useParams } from 'react-router-dom';

const PatientPageInternal: React.FC<{ pnumber: string }> = ({ pnumber }) => {
  const { isLoading, isError, error } = usePatient(pnumber);
  const doesNotExist = isError && error?.status === 404;

  if (isLoading) {
    return <CircularProgress />;
  }

  if (isError && error && !doesNotExist) {
    if (error.error?.message) {
      if (error.error.message.includes('permission')) {
        return <ShowError message="You do not have the privileges to view this patient" />;
      }
    }
    return <ShowError error={error} />;
  }

  return (
    <>
      <PatientBreadcrumbs />
      {doesNotExist ? <PatientNotExist pnumber={pnumber} /> : <Outlet />}
    </>
  );
};

export function PatientPage() {
  const { pnumber } = useParams();

  if (!pnumber) {
    return <ShowError message="pnumber is invalid" />;
  }

  return <PatientPageInternal pnumber={pnumber} />;
}
