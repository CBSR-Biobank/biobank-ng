import { Outlet } from 'react-router-dom';
import { AdminPage } from '../admin-page';

export const CataloguePage: React.FC = () => {
  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Study Catalogue</p>
      </AdminPage.Title>
      <Outlet />
    </AdminPage>
  );
};
