import { CancelButton } from '../cancel-button';
import { OkButton } from '../ok-button';
import { DialogClose, DialogFooter } from '../ui/dialog';

export const MutatorFooter: React.FC<{
  isValueValid: boolean;
  onOk: () => void;
}> = ({ isValueValid, onOk }) => {
  return (
    <DialogFooter className="grid-cols-1 gap-3 lg:grid-cols-2">
      <DialogClose asChild>
        <CancelButton />
      </DialogClose>
      <DialogClose asChild>
        <OkButton onClick={onOk} disabled={!isValueValid} />
      </DialogClose>
    </DialogFooter>
  );
};
