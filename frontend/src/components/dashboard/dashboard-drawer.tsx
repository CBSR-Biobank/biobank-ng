import { useUserStore } from '@app/store';
import { cn } from '@app/utils';

import {
  faArrowDown,
  faFileLines,
  faSquarePlus,
  faTableCells,
  faUserDoctor,
  faUsers,
} from '@fortawesome/free-solid-svg-icons';

import { DrawerMenuItem } from './drawer-menu-item';
import { MenuItem } from './menu-item';

// https://codepen.io/devdojo/pen/NWyPrGe
// https://github.com/Sridhar-C-25/sidebar_reactTailwind/blob/main/src/App.jsx
const menuItems: MenuItem[] = [
  {
    title: 'Patients',
    icon: faUsers,
    route: '/patients',
    //requiredGroups: [UserRole.SUPERUSER],
    submenu: [
      {
        title: 'Add',
        icon: faSquarePlus,
        route: '/patients/add',
        //requiredRoles: [UserRole.SUPERUSER]
      },
      {
        title: 'Visits',
        icon: faUserDoctor,
        route: '/patients',
        //requiredRoles: [UserRole.SUPERUSER]
      },
    ],
  },
  {
    title: 'Study Catalogue',
    icon: faTableCells,
    route: '/study-catalogue',
    submenu: [],
  },
  {
    title: 'Specimen Request',
    icon: faArrowDown,
    route: '/specimen-request',
    submenu: [],
  },
  {
    title: 'Logging',
    icon: faFileLines,
    route: '/logging',
    submenu: [],
  },
];

export const DashboardDrawer: React.FC<{ open: boolean }> = ({ open }) => {
  const { user } = useUserStore();

  if (!user) {
    return null;
  }

  //const allowedMenuItems = menuItems.filter((item) => userHasGroups(user, item?.requiredGroups ?? []));
  const allowedMenuItems = menuItems;

  return (
    <div className={cn('border-r pt-2 text-white transition-all', { 'w-72': open }, { 'w-14': !open })}>
      <ul>
        {allowedMenuItems.map((item, index) => (
          <DrawerMenuItem key={index} item={item} drawerOpen={open} />
        ))}
      </ul>
    </div>
  );
};
