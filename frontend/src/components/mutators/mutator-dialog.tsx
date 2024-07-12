import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@app/components/ui/dialog';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@app/components/ui/tooltip';
import { cn } from '@app/utils';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faPencil } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { VariantProps, cva } from 'class-variance-authority';

const mutatorVariants = cva('', {
  variants: {
    size: {
      sm: 'md:min-w-[425px]',
      md: 'md:min-w-[600px]',
      lg: 'md:min-w-[800px]',
    },
  },
  defaultVariants: {
    size: 'md',
  },
});

type MutatorVariantProps = VariantProps<typeof mutatorVariants>;

export const MutatorDialog: React.FC<
  React.PropsWithChildren<
    MutatorVariantProps & {
      title: string;
      icon?: IconProp;
      tooltip?: string;
    }
  >
> = ({ title, icon = faPencil, tooltip = 'Update', size, children }) => {
  return (
    <TooltipProvider delayDuration={200}>
      <Dialog>
        <Tooltip>
          <TooltipTrigger asChild>
            <DialogTrigger asChild>
              <button className="bg-basic-300 text-secondary-300 hover:bg-basic-400 flex cursor-pointer items-center rounded-r-lg p-3">
                <FontAwesomeIcon icon={icon} />
              </button>
            </DialogTrigger>
          </TooltipTrigger>
          <TooltipContent className="text-xs">{tooltip}</TooltipContent>
        </Tooltip>
        <DialogContent className={cn('min-w-fit', mutatorVariants({ size }))}>
          <DialogHeader>
            <DialogTitle className="text-primary-600">{title}</DialogTitle>
          </DialogHeader>
          {children}
        </DialogContent>
      </Dialog>
    </TooltipProvider>
  );
};
