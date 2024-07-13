import { OkButton } from '@app/components/ok-button';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@app/components/ui/dialog';

import { faTrash, faTriangleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { BbButton } from '../bb-button';

export const VisitDeleteNotAllowed: React.FC = () => {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <BbButton variant="destructive" trailingIcon={faTrash}>
          Delete Visit
        </BbButton>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-center">Cannot Delete</DialogTitle>
        </DialogHeader>
        <div className="flex gap-4">
          <FontAwesomeIcon icon={faTriangleExclamation} size="3x" className="text-red-500" />
          <div className="flex flex-col gap-2 self-center">
            <p>This visit cannot be deleted since it contains specimens.</p>
            <p>Remove the specimens if you want to delete it.</p>
          </div>
        </div>
        <DialogFooter className="pt-10 sm:justify-center">
          <DialogClose asChild>
            <OkButton type="button" />
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
