import { PG_ELLIPSIS, cn, paginationRange } from '@app/utils';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { cva } from 'class-variance-authority';

const foreground = 'text-slate-600';
const foregroundHover = 'hover:text-slate-600';
const background = 'bg-white';
const backgroundAlt = 'bg-slate-100';
const backgroundHover = 'hover:bg-slate-100';
const borderColor = 'border-slate-300';

const pgItemVariants = cva(
  [foreground, background, borderColor, 'relative inline-flex items-center font-semibold text-sm border py-2 px-4'],
  {
    variants: {
      intent: {
        item: [foregroundHover, backgroundHover, 'focus:z-20 focus:outline-offset-0 '],
        currentItem: [foreground, backgroundAlt, 'focus:z-20 focus:outline-offset-0'],
        prev: [foreground, backgroundHover, 'px-2 rounded-l-md focus:z-20 focus:outline-offset-0'],
        next: [foreground, backgroundHover, 'px-2 rounded-r-md focus:z-20 focus:outline-offset-0'],
        ellipsis: [foreground, 'px-4 focus:outline-offset-0'],
      },
    },
    defaultVariants: {
      intent: 'item',
    },
  }
);

const PgItem: React.FC<{ label: string; onClick: () => void }> = ({ label, onClick }) => {
  return (
    <button className={cn(pgItemVariants())} onClick={onClick}>
      {label}
    </button>
  );
};

const PgItemCurrent: React.FC<{ label: string }> = ({ label }) => {
  return <button className={cn(pgItemVariants({ intent: 'currentItem' }))}>{label}</button>;
};

const PgItemPrev: React.FC<{ onClick: () => void }> = ({ onClick }) => {
  return (
    <button className={cn(pgItemVariants({ intent: 'prev' }))} onClick={onClick}>
      <span className="sr-only">Previous</span>
      <FontAwesomeIcon icon={faChevronLeft} className="h-5 w-5" aria-hidden="true" />
    </button>
  );
};

const PgItemNext: React.FC<{ onClick: () => void }> = ({ onClick }) => {
  return (
    <button className={cn(pgItemVariants({ intent: 'next' }))} onClick={onClick}>
      <span className="sr-only">Next</span>
      <FontAwesomeIcon icon={faChevronRight} className="h-5 w-5" aria-hidden="true" />
    </button>
  );
};

const PgEllipsis: React.FC = () => {
  return <span className={cn(pgItemVariants({ intent: 'ellipsis' }))}>...</span>;
};

export const Pagination: React.FC<{ page: number; total: number; onChange: (page: number) => void }> = ({
  page,
  total,
  onChange,
}) => {
  const handlePageChange = (newPage: number) => {
    if (newPage > 0 && newPage <= total) {
      onChange(newPage);
    }
  };

  const pgRange = paginationRange(page, total);

  if (pgRange.length <= 0) {
    return null;
  }

  return (
    <div className="flex justify-end border-gray-200">
      <nav className="isolate inline-flex -space-x-px rounded-md shadow-sm" aria-label="Pagination">
        <PgItemPrev onClick={() => handlePageChange(page - 1)} />
        {pgRange.map((pg, index) => {
          if (pg === PG_ELLIPSIS) {
            return <PgEllipsis key={index} />;
          }

          if (pg === page) {
            return <PgItemCurrent key={index} label={`${pg}`} />;
          }

          return <PgItem key={index} label={`${pg}`} onClick={() => handlePageChange(pg)} />;
        })}
        <PgItemNext onClick={() => handlePageChange(page + 1)} />
      </nav>
    </div>
  );
};
