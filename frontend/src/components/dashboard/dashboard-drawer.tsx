import { useUserStore } from '@app/store';
import { cn } from '@app/utils';
import { faFileLines, faSquarePlus, faUserDoctor, faUsers } from '@fortawesome/free-solid-svg-icons';
import { DrawerMenuItem } from './drawer-menu-item';
import { MenuItem } from './menu-item';

// https://codepen.io/devdojo/pen/NWyPrGe
// https://github.com/Sridhar-C-25/sidebar_reactTailwind/blob/main/src/App.jsx
const menuItems: MenuItem[] = [
  {
    title: 'Patients',
    icon: faUsers,
    route: '/patients',
    //requiredGroups: [UserRole.SUPERUSER, UserRole.NORWEB_PEOPLE_ADMIN, UserRole.NORWEB_PEOPLE_USER],
    submenu: [
      {
        title: 'Add',
        icon: faSquarePlus,
        route: '/patients/add'
        //requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PEOPLE_ADMIN]
      },
      {
        title: 'Visits',
        icon: faUserDoctor,
        route: '/patients'
        //requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PEOPLE_ADMIN]
      }
    ]
  },
  {
    title: 'Logging',
    icon: faFileLines,
    route: '/logging',
    submenu: []
  }
];

export const DashboardDrawer: React.FC<{ open: boolean }> = ({ open }) => {
  const { user } = useUserStore();

  if (!user) {
    return null;
  }

  //const allowedMenuItems = menuItems.filter((item) => userHasGroups(user, item?.requiredGroups ?? []));
  const allowedMenuItems = menuItems;

  return (
    <div className={cn('pt-2 text-white', { 'w-72': open }, { 'w-14': !open })}>
      <ul>
        {allowedMenuItems.map((item, index) => (
          <DrawerMenuItem key={index} item={item} drawerOpen={open} />
        ))}
      </ul>
    </div>
  );
};
