import { cn } from '@app/utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useNavigate } from 'react-router-dom';
import { MenuItem } from './menuItem';

export const SubMenu: React.FC<{ menuItems: MenuItem[]; open: boolean }> = ({ menuItems, open }) => {
  const navigate = useNavigate();

  const handleItemClick = (item: MenuItem) => {
    navigate(item.route);
  };

  const ulClasses = cn(
    'ml-6 mr-2 origin-top transform overflow-hidden transition-all duration-300 ease-in-out',
    { 'scale-y-100': open },
    { 'scale-y-0': !open }
  );

  return (
    <ul className={ulClasses}>
      {open &&
        menuItems.map((item, index) => {
          const liClasses = cn(
            'text-basic-600 hover:bg-basic-300 flex cursor-pointer items-center gap-x-4 rounded p-1 text-sm',
            { 'mt-9': item?.gap },
            { 'mt-2': !item?.gap }
          );

          return (
            <li key={index} className={liClasses} onClick={() => handleItemClick(item)}>
              <FontAwesomeIcon icon={item.icon} />
              <span className="font-semibold">{item.title}</span>
            </li>
          );
        })}
    </ul>
  );
};
