import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from '@app/components/ui/dialog';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { CancelButton } from './cancel-button';
import { OkButton } from './ok-button';
import { Button } from './ui/button';

export const EntityAddDialog: React.FC<
  React.PropsWithChildren<{
    title: string;
    message?: string;
    buttonLabel?: string;
    buttonIcon?: IconProp;
    okButtonEnabled: GLboolean;
    onOk: () => void;
    onCancel: () => void;
  }>
> = ({ title, message, buttonLabel, buttonIcon, okButtonEnabled, onOk, onCancel, children }) => {
  const handleOk = () => {
    onOk();
  };

  const handleCancel = () => {
    onCancel();
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="secondary" icon={buttonIcon ?? faPlus}>
          {buttonLabel ?? 'Add'}
        </Button>
      </DialogTrigger>
      <DialogContent onPointerDownOutside={(e) => e.preventDefault()}>
        <DialogHeader>
          <DialogTitle className="text-primary-600">{title}</DialogTitle>
          {message && <DialogDescription>{message}</DialogDescription>}
        </DialogHeader>

        {children}

        <DialogFooter className="grid-cols-1 gap-4 lg:grid-cols-2">
          <DialogClose asChild>
            <CancelButton type="button" onClick={handleCancel} />
          </DialogClose>
          <DialogClose asChild>
            <OkButton type="button" disabled={!okButtonEnabled} onClick={handleOk} />
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
