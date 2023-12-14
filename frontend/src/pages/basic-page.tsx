import { fetchAuthenticated } from '@app/api/api';
import { DashboardLayout } from '@app/components/dashboard-layout';
import { userSchema } from '@app/models/user';
import { useUserStore } from '@app/store';
import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { HomePage } from './homepage';

export const BasicPage: React.FC = () => {
  const navigate = useNavigate();
  const { loggedIn, user, setCheckingAuth, setLoggedIn, setUser } = useUserStore();

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

  return <DashboardLayout>{loggedIn ? <Outlet /> : <HomePage />}</DashboardLayout>;
};
