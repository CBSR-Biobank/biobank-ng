import { Alert, AlertDescription } from '@app/components/ui/alert';
import { AdminPage } from '@app/pages/admin-page';

import { faPlusCircle, faWarning } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useParams } from 'react-router-dom';

import { BackButton } from '../back-button';
import { Button } from '../ui/button';

export const PatientNotExist: React.FC = () => {
  const { pnumber } = useParams();

  return (
    <>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Patient Does Not Exist</p>
      </AdminPage.Title>
      <div className="grid grid-cols-1 gap-8">
        <Alert variant="warning">
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
            <Button variant="secondary" icon={faPlusCircle}>
              Add Patient
            </Button>
          </Link>
        </div>
      </div>
    </>
  );
};
