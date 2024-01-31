import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle
} from '@app/components/ui/dialog';
import { cn } from '@app/utils';
import { CancelButton } from '../cancel-button';
import { OkButton } from '../ok-button';

export const MutatorDialog: React.FC<
  React.PropsWithChildren<{
    title: string;
    message?: string;
    valid: boolean;
    required?: boolean;
    open: boolean;
    size: 'default' | 'sm' | 'md' | 'lg';
    onOk: () => void;
    onCancel: () => void;
  }>
> = ({ title, message, valid, open, size, onOk, onCancel, children }) => {
  const handleOk = () => {
    onOk();
  };

  const handleCancel = () => {
    onCancel();
  };

  const contentClass = cn({
    'w-[425px]': size === 'sm',
    'w-[600px]': size === 'md' || size === 'default',
    'w-[800px]': size === 'lg'
  });

  return (
    <Dialog open={open} onOpenChange={handleCancel}>
      <DialogContent className={contentClass}>
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
