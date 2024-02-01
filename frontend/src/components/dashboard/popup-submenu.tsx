import {
  ContextMenu,
  ContextMenuContent,
  ContextMenuItem,
  ContextMenuSeparator,
  ContextMenuTrigger
} from '@app/components/ui/context-menu';
import { userHasGroups } from '@app/models/user';
import { useUserStore } from '@app/store';
import { cn } from '@app/utils';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MenuItem } from './menu-item';

export const PopupSubMenu: React.FC<{
  menuItem: MenuItem;
  icon: IconProp;
}> = ({ menuItem, icon }) => {
  const navigate = useNavigate();
  const { user } = useUserStore();

  // keep track of the context menu being opened
  const [open, setOpen] = useState(false);

  const handleOpened = (opened: boolean) => {
    setOpen(opened);
  };

  // handles when the user has left clicked on the menu's icon
  const handleParentClick = () => {
    if (!open) {
      navigate(menuItem.route);
    }
  };

  const handleSelected = (_event: Event, item: MenuItem) => {
    navigate(item.route);
  };

  if (!user) {
    return null;
  }

  const allowedSubmenuItems = (menuItem?.submenu ?? []).filter((item) =>
    userHasGroups(user, item?.requiredGroups ?? [])
  );

  return (
    <div className={cn('hover:bg-basic-300 relative cursor-pointer rounded-md px-2 py-1')} onClick={handleParentClick}>
      <ContextMenu onOpenChange={handleOpened}>
        <ContextMenuTrigger className="flex items-center justify-center">
          <FontAwesomeIcon icon={icon} />
        </ContextMenuTrigger>
        <ContextMenuContent className="absolute -top-4 left-6 z-10 w-64 bg-white">
          <ContextMenuItem
            className="flex gap-2 font-semibold"
            onSelect={(event: Event) => handleSelected(event, menuItem)}
          >
            {menuItem.title}
          </ContextMenuItem>
          <ContextMenuSeparator className="bg-gray-300" />
          {menuItem.submenu &&
            allowedSubmenuItems.map((item, index) => (
              <ContextMenuItem
                key={index}
                inset
                className="flex gap-2"
                onSelect={(event: Event) => handleSelected(event, item)}
              >
                <FontAwesomeIcon icon={item.icon} className="text-basic-500" />
                {item.title}
              </ContextMenuItem>
            ))}
        </ContextMenuContent>
      </ContextMenu>
    </div>
  );
};
