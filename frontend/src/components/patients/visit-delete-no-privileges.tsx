import { BackButton } from '@app/components/back-button';
import { AlertDescription } from '@app/components/ui/alert';
import { AdminPage } from '@app/pages/admin-page';

import { faCircleExclamation } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import { BbAlert } from '../bb-alert';

export const VisitDeleteNoPrivileges: React.FC = () => {
  const navigate = useNavigate();

  const backClicked = () => {
    navigate('..');
  };

  return (
    <>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">No Privileges</p>
      </AdminPage.Title>
      <div className="grid gap-4">
        <BbAlert variant="warning" icon={faCircleExclamation}>
          <AlertDescription>You do not have privileges to delete patient visits</AlertDescription>
        </BbAlert>
        <div className="flex flex-col gap-3 md:w-max md:flex-row">
          <BackButton onClick={backClicked} />
        </div>
      </div>
    </>
  );
};
