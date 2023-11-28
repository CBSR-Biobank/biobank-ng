import { Alert, AlertDescription } from '@app/components/alert';
import { BiobankButton } from '@app/components/biobank-button';
import { AdminPage } from '@app/pages/admin-page';
import { faWarning } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';

export const PatientNotExist: React.FC<{ pnumber: string }> = ({ pnumber }) => {
  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Patient Does Not Exist</p>
      </AdminPage.Title>
      <div className="grid grid-cols-1 gap-8">
        <Alert className="border-amber-500 bg-amber-200 text-slate-600">
          <FontAwesomeIcon icon={faWarning} />
          <AlertDescription>
            The patient with number <b>{pnumber}</b> is not in the system.
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
    </AdminPage>
  );
};
