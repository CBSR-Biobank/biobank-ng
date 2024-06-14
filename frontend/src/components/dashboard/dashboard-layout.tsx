import { fetchAuthenticated } from '@app/api/api';
import { userSchema } from '@app/models/user';
import { useUserStore } from '@app/store';
import { cn } from '@app/utils';

import { faBars } from '@fortawesome/free-solid-svg-icons';
import { ReactNode, useEffect } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';

import { AdminPage } from '@app/pages/admin-page';
import { ZodError } from 'zod';
import { IconButton } from '../bb-button';
import { CircularProgress } from '../circular-progress';
import { DashboardDrawer } from './dashboard-drawer';
import { UserMenu } from './user-menu';

const logoClasses = 'text-white rounded-md active:bg-primary-300 bg-gray-700 hover:bg-gray-600';

export const DashboardLayout: React.FC<{ children?: ReactNode }> = ({ children }) => {
  const navigate = useNavigate();
  const { isDrawerOpen, setDrawerOpen, checkingAuth, loggedIn, user, setCheckingAuth, setLoggedIn, setUser } =
    useUserStore();

  useEffect(() => {
    const fetchData = async () => {
      setCheckingAuth(true);
      setLoggedIn(false);

      try {
        const result = await fetchAuthenticated();
        setCheckingAuth(false);

        if (result.username) {
          setLoggedIn(true);
          setUser(userSchema.parse({ ...user, ...result }));
        } else {
          navigate('/login');
        }
      } catch (e) {
        if (e instanceof ZodError) {
          console.log(e.issues);
        }
        setCheckingAuth(false);
      }
    };
    fetchData();
  }, []);

  const toggleDrawer = () => {
    setDrawerOpen(!isDrawerOpen);
  };

  if (checkingAuth) {
    return (
      <AdminPage className="grid h-screen content-center justify-items-center gap-8">
        <CircularProgress />
      </AdminPage>
    );
  }

  if ((!checkingAuth && !loggedIn) || user === undefined) {
    return <Navigate to="/login" />;
  }

  return (
    <div className="relative">
      <div
        className={cn('absolute flex h-16 w-full grid-cols-2 items-center justify-between bg-gray-700 text-slate-200')}
      >
        <div className="flex items-center gap-4 px-2">
          <IconButton
            intent="soft"
            className={cn(logoClasses)}
            icon={faBars}
            onClick={toggleDrawer}
            hiddenLabel="toggles display of drawer"
          />
          <Link to="/" className="flex items-center gap-2 px-4">
            <span className="text-md font-sans font-semibold tracking-tight">CBSR Biobank</span>
          </Link>
        </div>
        <UserMenu />
      </div>
      <div className="flex min-h-screen w-full flex-grow pt-16">
        <DashboardDrawer open={isDrawerOpen} />
        <div className="w-full bg-white p-4">{children}</div>
      </div>
    </div>
  );
};
