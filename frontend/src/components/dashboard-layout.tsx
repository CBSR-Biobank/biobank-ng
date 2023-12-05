import { useUserStore } from '@app/store';
import { cn } from '@app/utils';
import { Button } from '@components/ui/button';
import { Menubar, MenubarContent, MenubarItem, MenubarMenu, MenubarTrigger } from '@components/ui/menubar';
import { faBars, faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ReactNode } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { DashboardDrawer } from './dashboard-drawer';

const bgColor = 'bg-gray-700';
const hoverColor = 'hover:bg-gray-600';
const activeColor = 'active:bg-gray-600';

const logoClasses = [
  'p-2 text-white transition duration-150 ease-in-out hover:rounded-md active:bg-primary-300',
  bgColor,
  hoverColor
];

export const DashboardLayout: React.FC<{ children?: ReactNode }> = ({ children }) => {
  const navigate = useNavigate();
  const { isDrawerOpen, setDrawerOpen, checkingAuth, loggedIn, user, setLoggedIn, setUserToken } = useUserStore();

  const toggleDrawer = () => {
    setDrawerOpen(!isDrawerOpen);
  };

  const handleLoginClicked = () => {
    navigate('/login');
  };

  const handleLogout = async () => {
    try {
      setLoggedIn(false);
      setUserToken(null);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="relative">
      <div className={cn('absolute flex h-16 w-full grid-cols-2 items-center justify-between text-slate-200', bgColor)}>
        <div className="flex items-center gap-4 px-2">
          {!checkingAuth && loggedIn && (
            <Button className={cn(logoClasses)} onClick={toggleDrawer}>
              <FontAwesomeIcon icon={faBars} size="2x" />
            </Button>
          )}
          <Link to="/" className="flex items-center gap-2 px-4">
            <span className="text-md font-sans font-semibold tracking-tight">CBSR Biobank</span>
          </Link>
        </div>
        {!loggedIn && (
          <Button className={cn('px-4', bgColor, hoverColor)} onClick={handleLoginClicked}>
            Login
          </Button>
        )}
        {loggedIn && user && (
          <Menubar className={cn('boder-0 border-none px-4', bgColor, hoverColor, activeColor)}>
            <MenubarMenu>
              <MenubarTrigger
                className={cn(
                  'focus:bg-primary-600 data-[state=open]:bg-primary-600 flex items-center gap-2',
                  bgColor,
                  hoverColor,
                  activeColor
                )}
              >
                <FontAwesomeIcon icon={faCircleUser} />
                {user.username}
              </MenubarTrigger>
              <MenubarContent>
                <MenubarItem onSelect={handleLogout}>Logout</MenubarItem>
              </MenubarContent>
            </MenubarMenu>
          </Menubar>
        )}
      </div>
      <div className="flex min-h-screen w-full flex-grow pt-16">
        {!checkingAuth && loggedIn && <DashboardDrawer open={isDrawerOpen} />}
        <div className="w-full bg-gray-200 p-4">{children}</div>
      </div>
    </div>
  );
};
