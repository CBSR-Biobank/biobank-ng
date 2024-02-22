import { fetchAuthenticated } from '@app/api/api';
import { userSchema } from '@app/models/user';
import { useUserStore } from '@app/store';
import { cn } from '@app/utils';

import { Button } from '@components/ui/button';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { ReactNode, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { DashboardDrawer } from './dashboard-drawer';
import { UserMenu } from './user-menu';

const logoClasses =
  'p-2 text-white transition duration-150 ease-in-out hover:rounded-md active:bg-primary-300 bg-gray-700 hover:bg-gray-600';

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
        // do nothing
      }
    };
    fetchData();
  }, []);

  const toggleDrawer = () => {
    setDrawerOpen(!isDrawerOpen);
  };

  return (
    <div className="relative">
      <div
        className={cn('absolute flex h-16 w-full grid-cols-2 items-center justify-between bg-gray-700 text-slate-200')}
      >
        <div className="flex items-center gap-4 px-2">
          {!checkingAuth && loggedIn && <Button className={cn(logoClasses)} icon={faBars} onClick={toggleDrawer} />}
          <Link to="/" className="flex items-center gap-2 px-4">
            <span className="text-md font-sans font-semibold tracking-tight">CBSR Biobank</span>
          </Link>
        </div>
        <UserMenu />
      </div>
      <div className="flex min-h-screen w-full flex-grow pt-16">
        {!checkingAuth && loggedIn && <DashboardDrawer open={isDrawerOpen} />}
        <div className="w-full bg-gray-200 p-4">{children}</div>
      </div>
    </div>
  );
};
