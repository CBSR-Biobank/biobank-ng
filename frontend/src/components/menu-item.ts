import { UserRole } from '@app/models/user-role';
import { IconProp } from '@fortawesome/fontawesome-svg-core';

export type MenuItem = {
  title: string;
  icon: IconProp;
  route: string;
  gap?: boolean;
  requiredRoles?: UserRole[];
  submenu?: MenuItem[];
};
