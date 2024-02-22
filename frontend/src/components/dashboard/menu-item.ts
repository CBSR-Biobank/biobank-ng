import { UserGroup } from '@app/models/user-group';

import { IconProp } from '@fortawesome/fontawesome-svg-core';

export type MenuItem = {
  title: string;
  icon: IconProp;
  route: string;
  gap?: boolean;
  requiredGroups?: UserGroup[];
  submenu?: MenuItem[];
};
