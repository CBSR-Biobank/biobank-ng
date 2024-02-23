import { Alert, AlertDescription } from '@app/components/ui/alert';
import { AdminPage } from '@app/pages/admin-page';

import { faWarning } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useParams } from 'react-router-dom';

import { BackButton } from '../back-button';

export const VisitNotExist: React.FC = () => {
  const { pnumber, vnumber } = useParams();

  return (
    <>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Visit Does Not Exist</p>
      </AdminPage.Title>
      <div className="grid grid-cols-1 gap-8">
        <Alert variant="warning">
          <FontAwesomeIcon icon={faWarning} />
          <AlertDescription>
            Visit <b>{vnumber}</b> for patient <b>{pnumber}</b> does not exist
          </AlertDescription>
        </Alert>
        <div className="flex gap-4">
          <Link to="/patients">
            <BackButton />
          </Link>
        </div>
      </div>
    </>
  );
};
