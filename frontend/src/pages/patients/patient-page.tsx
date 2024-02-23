import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { CircularProgress } from '@app/components/circular-progress';
import { ShowError } from '@app/components/show-error';
import { usePatient } from '@app/hooks/use-patient';

import { useEffect } from 'react';
import { Outlet, useNavigate, useParams } from 'react-router-dom';

import { AdminPage } from '../admin-page';

const PatientPageInternal: React.FC<{ pnumber: string }> = ({ pnumber }) => {
  const navigate = useNavigate();
  const { isLoading, isError, error } = usePatient(pnumber);

  useEffect(() => {
    if (isError && error) {
      if (error.error?.message && error.error.message.includes('permission')) {
        navigate('./no-privileges');
      } else if (error?.status === 404) {
        navigate('./not-exits');
      }
    }
  }, [isError, error]);

  if (isLoading) {
    return <CircularProgress />;
  }

  return (
    <>
      <PatientBreadcrumbs />
      <AdminPage>
        <Outlet />
      </AdminPage>
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
