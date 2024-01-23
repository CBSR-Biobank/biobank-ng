import { Alert, AlertDescription } from '@app/components/alert';
import { AdminPage } from '@app/pages/admin-page';
import { faPlusCircle, faWarning } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { BackButton } from '../back-button';
import { Button } from '../ui/button';

export const PatientNotExist: React.FC<{ pnumber: string }> = ({ pnumber }) => {
  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Patient Does Not Exist</p>
      </AdminPage.Title>
      <div className="grid grid-cols-1 gap-8">
        <Alert className="border-orange-500 bg-yellow-300 text-slate-600">
          <FontAwesomeIcon icon={faWarning} />
          <AlertDescription>
            The patient with number <b>{pnumber}</b> is not on record
          </AlertDescription>
        </Alert>
        <div className="flex gap-4">
          <Link to="/patients">
            <BackButton />
          </Link>
          <Link to={`/patients/add?pnumber=${pnumber}`}>
            <Button icon={faPlusCircle}>Add Patient</Button>
          </Link>
        </div>
      </div>
    </AdminPage>
  );
};
