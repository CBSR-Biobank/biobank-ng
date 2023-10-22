import { cn } from '@app/utils';
import { faChevronRight, faHouse } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';

export type Breadcrumb = {
  label: string;
  route: string;
  isLast?: boolean;
};

// https://stackoverflow.com/questions/71592649/change-breadcrumb-component-that-was-done-with-react-router-v5-to-react-router-v

// https://pavsaund.com/post/2022-02-23-dynamic-breadcrumbs-and-routes-with-react-router/

export const Breadcrumbs: React.FC<{ crumbs: Breadcrumb[] }> = ({ crumbs }) => {
  return (
    <nav className="pb-4 pt-2 text-sm">
      <ul className="flex list-none items-center text-indigo-800">
        <li key="homepage" className="px-2">
          <Link to={'/'} className={cn('flex items-center gap-2 text-indigo-600')}>
            <FontAwesomeIcon icon={faHouse} />
            <FontAwesomeIcon icon={faChevronRight} size="2xs" />
          </Link>
        </li>

        {crumbs.map(({ label, route, isLast }, index) => {
          if (isLast) {
            return (
              <li key={label} className={cn('text-primary-600 max-w-[12rem] truncate pl-2 font-semibold')}>
                {label}
              </li>
            );
          }

          return (
            <li key={label} className="px-2">
              <Link to={route} className={cn('flex items-baseline gap-2 text-indigo-600')}>
                <p className="max-w-[12rem] truncate">{label}</p>
                <FontAwesomeIcon icon={faChevronRight} size="2xs" />
              </Link>
            </li>
          );
        })}
      </ul>
    </nav>
  );
};
