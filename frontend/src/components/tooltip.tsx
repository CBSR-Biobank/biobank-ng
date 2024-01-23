import { VariantProps, cva } from 'class-variance-authority';

import { cn } from '@app/utils';

const tooltipVariants = cva(
  'absolute text-white bg-sky-400 text-xs p-2 rounded scale-0 transition-all group-hover:scale-100 whitespace-nowrap font-semibold',
  {
    variants: {
      orientation: {
        top: 'top-8',
        right: 'top-8 right-5'
      }
    }
  }
);

export interface TooltipProps extends React.PropsWithChildren, VariantProps<typeof tooltipVariants> {
  message: string;
}

export const Tooltip: React.FC<TooltipProps> = ({ message, orientation, children }) => {
  return (
    <div className="group relative flex">
      {children}
      <span className={cn(tooltipVariants({ orientation }))}>{message}</span>
    </div>
  );
};
