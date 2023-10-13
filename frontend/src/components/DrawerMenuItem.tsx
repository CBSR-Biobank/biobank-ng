import { userHasRoles } from '@app/models/user';
import { useUserStore } from '@app/store';
import { cn } from '@app/utils';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { PopupSubMenu } from './PopupSubMenu';
import { SubMenu } from './SubMenu';
import { MenuItem } from './menuItem';

export const DrawerMenuItem: React.FC<{
  item: MenuItem;
  drawerOpen: boolean;
}> = ({ item, drawerOpen }) => {
  const navigate = useNavigate();
  const { user } = useUserStore();

  const [submenuOpen, setSubmenuOpen] = useState(false);

  const handleClick = (event: React.MouseEvent<HTMLDivElement>) => {
    if (!drawerOpen) {
      throw new Error('should not be called when drawer is closed');
    }
    event.preventDefault();
    navigate(item.route);
  };

  const handleSubmenuClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setSubmenuOpen(!submenuOpen);
    event.preventDefault();
  };

  const liClasses = cn([
    'text-basic-600 flex items-center justify-between gap-4 overflow-x-hidden rounded px-2 text-sm',
    { ' hover:bg-basic-300': drawerOpen },
    { 'mt-9': item?.gap },
    { 'mt-2': !item?.gap }
  ]);

  if (!user) {
    return null;
  }

  const allowedSubmenuItems = (item?.submenu ?? []).filter((item) => userHasRoles(user, item?.requiredRoles ?? []));

  return (
    <>
      <li className={liClasses}>
        <div className="my-2 flex">
          {!drawerOpen && <PopupSubMenu menuItem={item} icon={item.icon} />}
          {drawerOpen && (
            <div className={cn('mx-1 flex cursor-pointer items-center gap-4')} onClick={handleClick}>
              <FontAwesomeIcon icon={item.icon} />
              <span className={cn('font-semibold')}>{item.title}</span>
            </div>
          )}
        </div>
        {drawerOpen && allowedSubmenuItems.length > 0 && (
          <div className={cn('duration-300 ease-in-out', { 'rotate-90': submenuOpen })}>
            <button className="rounded-full" onClick={handleSubmenuClick}>
              <FontAwesomeIcon icon={faChevronRight} size="sm" />
            </button>
          </div>
        )}
      </li>
      {item.submenu && allowedSubmenuItems.length > 0 && (
        <SubMenu menuItems={item.submenu} open={drawerOpen && submenuOpen} />
      )}
    </>
  );
};
