import { Button } from '@app/components/ui/button';
import { Column } from '@tanstack/react-table';
import { ChevronDown, ChevronUp, ChevronsLeftRightIcon } from 'lucide-react';
import { HTMLAttributes } from 'react';

export function DataTableColumnHeader<TData, TValue>({
  column,
  title,
  multiselect = false,
  className
}: HTMLAttributes<HTMLDivElement> & {
  column: Column<TData, TValue>;
  title: string;
  multiselect?: boolean;
}) {
  if (!column.getCanSort()) {
    return <div className={className}>{title}</div>;
  }

  const SortIcon = () => {
    const sort = column.getIsSorted();
    if (!sort) {
      return <ChevronsLeftRightIcon className="h-4 w-4 rotate-90" />;
    }
    return sort === 'desc' ? <ChevronDown className="h-4 w-4" /> : <ChevronUp className="h-4 w-4" />;
  };

  return (
    <div className={className}>
      <Button
        variant="ghost"
        size="sm"
        className="flex h-8 items-center gap-2"
        onClick={() => column.toggleSorting(undefined, multiselect)}
      >
        <span>{title}</span>
        <SortIcon />
      </Button>
    </div>
  );
}
