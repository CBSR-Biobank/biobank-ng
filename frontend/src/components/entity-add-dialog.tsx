import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from '@app/components/ui/dialog';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';
import { CancelButton } from './cancel-button';
import { OkButton } from './ok-button';

export const EntityAddDialog: React.FC<
  React.PropsWithChildren<{
    title: string;
    message?: string;
    buttonLabel?: string;
    buttonIcon?: IconProp;
    okButtonEnabled: GLboolean;
    onOk: () => void;
  }>
> = ({ title, message, buttonLabel, buttonIcon, okButtonEnabled, onOk, children }) => {
  const [open, setOpen] = useState(false);

  const handleOpenChange = () => {
    setOpen((open) => !open);
  };

  const handleOk = () => {
    setOpen(false);
    onOk();
  };

  const handleCancel = () => {
    setOpen(false);
  };

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogTrigger className="focus-visible:outline-primary-600 flex h-10 items-center gap-1 rounded-md bg-secondary px-4 text-sm font-normal text-secondary-foreground hover:bg-secondary/90">
        {buttonLabel ?? 'Add'}
        <FontAwesomeIcon icon={buttonIcon ?? faPlus} />
      </DialogTrigger>
      <DialogContent onPointerDownOutside={(e) => e.preventDefault()}>
        <DialogHeader>
          <DialogTitle className="text-primary-600">{title}</DialogTitle>
          {message && <DialogDescription>{message}</DialogDescription>}
        </DialogHeader>

        {children}

        <DialogFooter className="grid-cols-1 gap-3 lg:grid-cols-2">
          <CancelButton onClick={handleCancel} />
          <OkButton disabled={!okButtonEnabled} onClick={handleOk} />
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
