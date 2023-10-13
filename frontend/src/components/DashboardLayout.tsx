import { useUserStore } from '@app/store';
import { cn } from '@app/utils';
import { Button } from '@components/ui/button';
import { Menubar, MenubarContent, MenubarItem, MenubarMenu, MenubarTrigger } from '@components/ui/menubar';
import { faBars, faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useEffect, useState } from 'react';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import { DashboardDrawer } from './DashboardDrawer';

const bgColor = 'bg-gray-700';
const hoverColor = 'hover:bg-gray-600';
const activeColor = 'active:bg-gray-600';

const logoClasses = [
  'p-2 text-white transition duration-150 ease-in-out hover:rounded-md active:bg-primary-300',
  bgColor,
  hoverColor
];

export function DashboardLayout() {
  const navigate = useNavigate();
  const { loggedIn, user, setLoggedIn } = useUserStore();

  const [drawerOpen, setDrawerOpen] = useState(false);

  useEffect(() => {
    if (!loggedIn) {
      navigate('/login');
    }
  }, [loggedIn]);

  const toggleDrawer = () => {
    setDrawerOpen(!drawerOpen);
  };

  const handleLoginClicked = () => {
    navigate('/login');
  };

  const handleLogout = async () => {
    try {
      setLoggedIn(false);
    } catch (e) {
      console.error(e);
    }
  };

  console.log({ loggedIn });

  return (
    <div className="relative">
      <div className={cn('absolute flex h-16 w-full grid-cols-2 items-center justify-between text-slate-200', bgColor)}>
        <div className="flex items-center gap-4 px-2">
          {loggedIn && (
            <Button className={cn(logoClasses)} onClick={toggleDrawer}>
              <FontAwesomeIcon icon={faBars} size="2x" />
            </Button>
          )}
          <Link to="/" className="flex items-center gap-2 px-4">
            <span className="text-md font-sans font-semibold tracking-tight">Biobank</span>
          </Link>
        </div>
        {!loggedIn && (
          <Button className={cn('px-4', bgColor, hoverColor)} onClick={handleLoginClicked}>
            Login
          </Button>
        )}
        {/* {loggedIn && user && ( */}
        {loggedIn && (
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
                User
                {/* {user.username} */}
              </MenubarTrigger>
              <MenubarContent>
                <MenubarItem onSelect={handleLogout}>Logout</MenubarItem>
              </MenubarContent>
            </MenubarMenu>
          </Menubar>
        )}
      </div>
      <div className="flex h-screen w-full flex-grow pt-16">
        {loggedIn && <DashboardDrawer open={drawerOpen} />}
        <div className="w-full bg-gray-200 p-4">
          <Outlet />
        </div>
      </div>
    </div>
  );
}
