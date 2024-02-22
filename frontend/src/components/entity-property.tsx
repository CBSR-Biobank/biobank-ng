import { cn } from '@app/utils';

import { ReactNode } from 'react';

import { Chip } from './chip';

export const EntityProperty: React.FC<
  React.PropsWithChildren<{
    className?: string;
    propName: string;
    label: string;
    mutator?: ReactNode;
  }>
> = ({ className, label, children, mutator }) => {
  const valueClasses = cn(
    'border-r-solid flex grow flex-wrap items-center rounded-l-lg border-gray-200 bg-gray-300/50 p-3 text-slate-700 ',
    {
      'border-r': !!mutator,
      'rounded-r-lg': !mutator,
    }
  );

  return (
    <div className={cn('grid grid-cols-1 content-start gap-2', className)}>
      <p className="text-sm font-semibold text-gray-400">{label}</p>
      <div className="flex gap-0">
        <div className={valueClasses}>
          {children !== null && <>{children}</>}
          {children === null && <Chip message="Not avaiable" size="sm" className="flex items-center gap-1" />}
        </div>
        {mutator && <>{mutator}</>}
      </div>
    </div>
  );
};
