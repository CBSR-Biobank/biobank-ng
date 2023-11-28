import { Status, StatusLabels } from '@app/models/status';
import { cn } from '@app/utils';
import { VariantProps, cva } from 'class-variance-authority';
import { Chip } from './chip';

const chipVariants = cva('rounded-lg font-semibold uppercase px-2 py-1', {
  variants: {
    variant: {
      default: 'flex items-center text-xs',
      table: 'text-xs'
    },
    size: {
      default: 'md',
      sm: 'sm',
      md: 'md',
      lg: 'lg'
    }
  },
  defaultVariants: {
    variant: 'default',
    size: 'default'
  }
});

export interface StatusChipProps extends React.PropsWithChildren, VariantProps<typeof chipVariants> {
  className?: string;
  status: Status | null;
}

export const StatusChip: React.FC<StatusChipProps> = ({ className, status, variant, size }) => {
  if (!status) {
    return null;
  }

  const classes = cn(
    {
      'text-amber-500': status === Status.NONE,
      'bg-amber-100/70': status === Status.NONE
    },
    {
      'text-sky-600': status === Status.ACTIVE,
      'bg-sky-300/80': status === Status.ACTIVE
    },
    {
      'text-slate-600': status === Status.CLOSED,
      'bg-slate-400/70': status === Status.CLOSED
    }
  );
  return (
    <Chip message={StatusLabels[status]} size={size} className={cn(chipVariants({ variant }), classes, className)} />
  );
};
