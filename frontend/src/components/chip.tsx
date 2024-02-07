import { cn } from '@app/utils';
import { VariantProps, cva } from 'class-variance-authority';

const foreground = 'text-slate-100';
const background = 'bg-gray-600';

const chipVariants = cva('rounded-lg bg-slate-600 font-semibold text-gray-100', {
  variants: {
    variant: {
      default: [foreground, background, 'rounded-md'],
      basic: [foreground, 'bg-gray-500'],
      primary: ['bg-sky-300/80 text-sky-600'],
      secondary: ['text-gray-100 bg-slate-600']
    },
    size: {
      default: 'h-8 py-1 px-1 text-md',
      xs: 'h-7 py-1 px-2 text-xs',
      sm: 'h-7 py-1 px-2 text-sm',
      md: 'h-9 py-1 px-3 text-md',
      lg: 'h-11 py-1 px-4 text-lg'
    }
  },
  defaultVariants: {
    variant: 'default',
    size: 'default'
  }
});

export interface ChipProps extends React.PropsWithChildren, VariantProps<typeof chipVariants> {
  className?: string;
  message?: string;
  onClick?: (message?: string) => void;
  onDelete?: (message?: string) => void;
}

export const Chip: React.FC<ChipProps> = ({ className, message, variant, size, children, onClick }) => {
  const handleClick = () => {
    if (onClick) {
      onClick(message);
    }
  };

  return (
    <span className={cn(chipVariants({ variant, size }), className)} onClick={handleClick}>
      {message && `${message}`}
      {children}
    </span>
  );
};
