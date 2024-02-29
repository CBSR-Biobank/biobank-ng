import { OkButton } from '@app/components/ok-button';
import { Dialog, DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@app/components/ui/dialog';

import { useState } from 'react';

export const SpecimenAddErrorDialog: React.FC<{ open: boolean; error: any; onClose: () => void }> = ({
  open: initialOpen,
  error,
  onClose,
}) => {
  const [open, setOpen] = useState(initialOpen);

  const handleOpenChange = (isOpen: boolean) => {
    setOpen(isOpen);
    onClose();
  };

  const errMessage = error?.error?.message;

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogContent className="sm:max-w-[700px]">
        <DialogTitle className="text-center">Error</DialogTitle>
        <DialogHeader className="flex items-center">
          {errMessage && <>{errMessage}</>}
          <pre>{JSON.stringify(error, null, 2)}</pre>
        </DialogHeader>
        <DialogFooter className="gap-4 pt-10 sm:justify-center">
          <DialogClose asChild>
            <OkButton type="button" />
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
