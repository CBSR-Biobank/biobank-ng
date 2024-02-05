import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@app/components/ui/dialog';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@app/components/ui/tooltip';
import { cn } from '@app/utils';
import { faPencil } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { VariantProps, cva } from 'class-variance-authority';

const mutatorVariants = cva('', {
  variants: {
    size: {
      sm: 'md:w-[425px]',
      md: 'md:w-[600px]',
      lg: 'md:w-[800px]'
    }
  },
  defaultVariants: {
    size: 'md'
  }
});

type MutatorVariantProps = VariantProps<typeof mutatorVariants>;

export const MutatorDialog: React.FC<
  React.PropsWithChildren<
    MutatorVariantProps & {
      title: string;
    }
  >
> = ({ title, size, children }) => {
  return (
    <TooltipProvider>
      <Dialog>
        <Tooltip>
          <TooltipTrigger asChild>
            <DialogTrigger asChild>
              <button className="flex cursor-pointer items-center rounded-r-lg bg-gray-300/50 p-3 text-sky-400 hover:bg-gray-400">
                <FontAwesomeIcon icon={faPencil} />
              </button>
            </DialogTrigger>
          </TooltipTrigger>
          <TooltipContent>
            <p>Update</p>
          </TooltipContent>
        </Tooltip>
        <DialogContent className={cn(mutatorVariants({ size }))}>
          <DialogHeader>
            <DialogTitle className="text-primary-600">{title}</DialogTitle>
          </DialogHeader>
          {children}
        </DialogContent>
      </Dialog>
    </TooltipProvider>
  );
};
