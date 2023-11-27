import { userHasRoles } from '@app/models/user';
import { UserRole } from '@app/models/user-role';
import { useUserStore } from '@app/store';
import { cn } from '@app/utils';
import {
  faBook,
  faContactBook,
  faCreditCard,
  faGear,
  faNoteSticky,
  faProjectDiagram,
  faSuitcase,
  faUserGear,
  faUserGroup,
  faUserTag,
  faUsers,
  faWrench
} from '@fortawesome/free-solid-svg-icons';
import { useState } from 'react';
import { DrawerMenuItem } from './drawer-menu-item';
import { MenuItem } from './menu-item';

// https://codepen.io/devdojo/pen/NWyPrGe
// https://github.com/Sridhar-C-25/sidebar_reactTailwind/blob/main/src/App.jsx
const menuItems: MenuItem[] = [
  {
    title: 'People',
    icon: faUsers,
    route: '/people',
    requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PEOPLE_ADMIN, UserRole.NORWEB_PEOPLE_USER],
    submenu: [
      {
        title: 'Emp. Categories',
        icon: faGear,
        route: '/categories',
        requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PEOPLE_ADMIN]
      },
      {
        title: 'Titles',
        icon: faUserTag,
        route: '/titles',
        requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PEOPLE_ADMIN]
      },
      {
        title: 'Research Groups',
        icon: faUserGroup,
        route: '/groups',
        requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PEOPLE_ADMIN]
      }
    ]
  },
  {
    title: 'Publications',
    icon: faBook,
    route: '/publications',
    requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PUBLICATION_ADMIN, UserRole.NORWEB_PUBLICATION_USER],
    submenu: [
      {
        title: 'Publication Types',
        icon: faNoteSticky,
        route: '/publication-types',
        requiredRoles: [UserRole.SUPERUSER, UserRole.NORWEB_PUBLICATION_ADMIN]
      }
    ]
  },
  {
    title: 'Projects',
    icon: faProjectDiagram,
    route: '/projects',
    requiredRoles: [UserRole.SUPERUSER],
    submenu: [
      { title: 'Funders', icon: faCreditCard, route: '/funders', requiredRoles: [UserRole.SUPERUSER] },
      { title: 'Research Areas', icon: faContactBook, route: '/research-areas', requiredRoles: [UserRole.SUPERUSER] },
      { title: 'Project Roles', icon: faUserGear, route: '/project-roles', requiredRoles: [UserRole.SUPERUSER] },
      { title: 'Resources', icon: faWrench, route: '/resources', requiredRoles: [UserRole.SUPERUSER] }
    ]
  },
  { title: 'Visitors', icon: faSuitcase, route: '/visitors', requiredRoles: [UserRole.SUPERUSER] }
];

export const DashboardDrawer: React.FC<{ open: boolean }> = ({ open }) => {
  const { user } = useUserStore();
  const [drawerOpen, _setDrawerOpen] = useState(open);

  if (!user) {
    return null;
  }

  const allowedMenuItems = menuItems.filter((item) => userHasRoles(user, item?.requiredRoles ?? []));

  return (
    <div className={cn('pt-2 text-white', { 'w-72': drawerOpen }, { 'w-14': !drawerOpen })}>
      <ul>
        {allowedMenuItems.map((item, index) => (
          <DrawerMenuItem key={index} item={item} drawerOpen={open} />
        ))}
      </ul>
    </div>
  );
};
