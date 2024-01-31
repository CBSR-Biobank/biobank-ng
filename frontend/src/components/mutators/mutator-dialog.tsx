import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle
} from '@app/components/ui/dialog';
import { cn } from '@app/utils';
import { VariantProps, cva } from 'class-variance-authority';
import { CancelButton } from '../cancel-button';
import { OkButton } from '../ok-button';

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
      message?: string;
      valid: boolean;
      required?: boolean;
      open: boolean;
      onOk: () => void;
      onCancel: () => void;
    }
  >
> = ({ title, message, valid, open, size, onOk, onCancel, children }) => {
  const handleOk = () => {
    onOk();
  };

  const handleCancel = () => {
    onCancel();
  };

  return (
    <Dialog open={open} onOpenChange={handleCancel}>
      <DialogContent className={cn(mutatorVariants({ size }))}>
        <DialogHeader>
          <DialogTitle className="text-primary-600">{title}</DialogTitle>
          {message && <DialogDescription>{message}</DialogDescription>}
        </DialogHeader>
        {children}
        <DialogFooter className="grid-cols-1 gap-3 lg:grid-cols-2">
          <CancelButton onClick={handleCancel} />
          <OkButton onClick={handleOk} disabled={!valid} />
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
