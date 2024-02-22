import { DashboardLayout } from '@app/components/dashboard/dashboard-layout';

import { Outlet } from 'react-router-dom';

export const BasicPage: React.FC = () => {
  return (
    <DashboardLayout>
      <Outlet />
    </DashboardLayout>
  );
};
