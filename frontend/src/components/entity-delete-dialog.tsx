import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@app/components/ui/dialog';

import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { PropsWithChildren } from 'react';

import { CancelButton } from './cancel-button';
import { Button } from './ui/button';

export const EntityDeleteDialog: React.FC<
  PropsWithChildren<{
    title?: string;
    buttonLabel?: string;
    buttonIcon?: IconProp;
    onClose: (result: boolean) => void;
  }>
> = ({ title = 'Confirm Deletion', buttonLabel, buttonIcon, onClose, children }) => {
  const handleDelete = () => {
    onClose(true);
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="destructive" icon={buttonIcon ?? faTrash}>
          {buttonLabel ?? 'Add'}
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-center">{title}</DialogTitle>
        </DialogHeader>
        {children}
        <DialogFooter className="gap-4 pt-10 sm:justify-center">
          <DialogClose asChild>
            <CancelButton type="button" />
          </DialogClose>
          <DialogClose asChild>
            <Button type="button" variant="destructive" icon={faTrash} onClick={handleDelete}>
              Delete
            </Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
