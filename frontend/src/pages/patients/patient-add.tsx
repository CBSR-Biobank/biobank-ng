import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { usePatientStore } from '@app/store';
import { useEffect } from 'react';
import { AdminPage } from '../admin-page';

export function PatientAdd() {
  const { setPatient } = usePatientStore();

  useEffect(() => {
    setPatient(undefined);
  }, []);

  return (
    <>
      <PatientBreadcrumbs />
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-4xl font-semibold text-sky-600">Add Patient</p>
        </AdminPage.Title>
      </AdminPage>
    </>
  );
}
