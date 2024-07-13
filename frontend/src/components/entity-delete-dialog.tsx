import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@app/components/ui/dialog';

import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { PropsWithChildren } from 'react';

import { BbButton } from './bb-button';
import { CancelButton } from './cancel-button';
import { DeleteButton } from './delete-button';

export const EntityDeleteDialog: React.FC<
  PropsWithChildren<{
    title?: string;
    buttonLabel?: string;
    buttonIcon?: IconDefinition;
    onClose: (result: boolean) => void;
  }>
> = ({ title = 'Confirm Deletion', buttonLabel, buttonIcon, onClose, children }) => {
  const handleDelete = () => {
    onClose(true);
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <BbButton variant="destructive" trailingIcon={buttonIcon ?? faTrash}>
          {buttonLabel ?? 'Add'}
        </BbButton>
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
            <DeleteButton type="button" onClick={handleDelete} />
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
