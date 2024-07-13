import { Dialog, DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@app/components/ui/dialog';

import { faCircleXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { DialogTrigger } from '@radix-ui/react-dialog';

import { CancelButton } from '../cancel-button';
import { Chip } from '../chip';
import { DeleteButton } from '../delete-button';

export const AnnotationValueDeleteDialog: React.FC<{
  value: string;
  onClose: (result: boolean) => void;
}> = ({ value, onClose }) => {
  const handleDelete = () => {
    onClose(true);
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <button className="ml-1 rounded-full outline-none ring-offset-background focus:ring-2 focus:ring-ring focus:ring-offset-2">
          <FontAwesomeIcon icon={faCircleXmark} className="text-muted-white h-4 w-4 hover:text-slate-400" />
        </button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-center">Confirm Removal</DialogTitle>
        </DialogHeader>
        <div className="grid grid-cols-1 place-items-center gap-4">
          <p>Are you sure you want to remove this value?</p>
          <div className="grid grid-cols-1 place-items-center">
            <Chip variant="basic" size="sm">
              {value}
            </Chip>
          </div>
        </div>
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
