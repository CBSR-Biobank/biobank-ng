import { useUserStore } from '@app/store';
import { cn } from '@app/utils';

import { Button } from '@components/ui/button';
import { Menubar, MenubarContent, MenubarItem, MenubarMenu, MenubarTrigger } from '@components/ui/menubar';
import { faAddressCard } from '@fortawesome/free-regular-svg-icons';
import { faArrowRightFromBracket, faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useNavigate } from 'react-router-dom';

const bgColor = 'bg-gray-700';
const hoverColor = 'hover:bg-gray-600';
const activeColor = 'active:bg-gray-600';

export const UserMenu = () => {
  const navigate = useNavigate();
  const { loggedIn, setLoggedIn, setUserToken, user } = useUserStore();

  const handleProfileSelected = () => {
    navigate('/profile');
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

  if (!loggedIn || !user) {
    return (
      <Button className={cn('px-4', bgColor, hoverColor)} onClick={handleLoginClicked}>
        Login
      </Button>
    );
  }

  return (
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
          <MenubarItem className="flex gap-2" onSelect={handleProfileSelected}>
            <FontAwesomeIcon icon={faAddressCard} />
            My Profile
          </MenubarItem>
          <MenubarItem className="flex gap-2" onSelect={handleLogout}>
            <FontAwesomeIcon icon={faArrowRightFromBracket} />
            Logout
          </MenubarItem>
        </MenubarContent>
      </MenubarMenu>
    </Menubar>
  );
};
