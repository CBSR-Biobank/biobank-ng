import { BackButton } from '@app/components/back-button';
import { Alert, AlertDescription } from '@app/components/ui/alert';
import { AdminPage } from '@app/pages/admin-page';

import { faCircleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useNavigate } from 'react-router-dom';

export const NoPrivileges: React.FC = () => {
  const navigate = useNavigate();

  const backClicked = () => {
    navigate('/patients');
  };

  return (
    <>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">No Privileges</p>
      </AdminPage.Title>
      <div className="grid gap-4">
        <Alert variant="warning">
          <FontAwesomeIcon icon={faCircleExclamation} />
          <AlertDescription>You do not have privileges to view this patient</AlertDescription>
        </Alert>
        <div className="flex flex-col gap-3 md:w-max md:flex-row">
          <BackButton onClick={backClicked} />
        </div>
      </div>
    </>
  );
};
