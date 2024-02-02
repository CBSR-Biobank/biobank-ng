import { cn } from '@app/utils';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faPen } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Chip } from './chip';
import { Tooltip } from './tooltip';

export const EntityProperty: React.FC<
  React.PropsWithChildren<{
    className?: string;
    propName: string;
    label: string;
    icon?: IconProp;
    tooltip?: string;
    allowChanges?: boolean;
    handleChange?: (propertyName: string) => void;
  }>
> = ({ className, propName, label, icon = faPen, tooltip = 'Update', allowChanges, handleChange, children }) => {
  const valueClasses = cn(
    'border-r-solid flex grow flex-wrap items-center rounded-l-lg border-gray-200 bg-gray-300/50 p-3 text-slate-700 ',
    {
      'border-r': handleChange,
      'rounded-r-lg': !handleChange
    }
  );

  const handleClick = () => {
    if (handleChange) {
      handleChange(propName);
    }
  };

  return (
    <div className={cn('grid grid-cols-1 content-start gap-2', className)}>
      <p className="text-sm font-semibold text-gray-400">{label}</p>
      <div className="flex gap-0">
        <div className={valueClasses}>
          {children !== null && <>{children}</>}
          {children === null && <Chip message="Not avaiable" size="sm" className="flex items-center gap-1" />}
        </div>
        {allowChanges && (
          <div
            className="flex cursor-pointer items-center rounded-r-lg bg-gray-300/50 p-3 text-sky-400 hover:bg-gray-400"
            onClick={handleClick}
          >
            <Tooltip message={tooltip} orientation="right">
              <FontAwesomeIcon icon={icon} />
            </Tooltip>
          </div>
        )}
      </div>
    </div>
  );
};
