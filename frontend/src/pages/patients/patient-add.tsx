import { AdminPage } from '../admin-page';

export function PatientAdd() {
  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Add Patient</p>
      </AdminPage.Title>
    </AdminPage>
  );
}
