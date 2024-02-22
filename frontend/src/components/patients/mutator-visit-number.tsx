import { DialogClose, DialogFooter } from '@app/components/ui/dialog';

import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

import { CancelButton } from '../cancel-button';
import { LabelledInput } from '../forms/labelled-input';
import { OkButton } from '../ok-button';

export const MutatorVisitNumber: React.FC<{
  vnumber: number;
  disallow: number[];
  onClose: (vnumber: number) => void;
}> = ({ vnumber, disallow, onClose }) => {
  const schema = z
    .object({
      vnumber: z.union([z.number().int().min(1, { message: 'should be 1 or greater' }), z.nan()]),
    })
    .superRefine((data, ctx) => {
      if (!data.vnumber) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          // path not assigned here since an error message would be displayed if the cancel button is pressed
        });
      }

      if (disallow.includes(data.vnumber)) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ['vnumber'],
          message: 'already taken',
        });
      }
    });

  const {
    register,
    getValues,
    formState: { isValid, errors },
  } = useForm<z.infer<typeof schema>>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: { vnumber },
  });

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    handleOk();
  };

  const handleOk = () => {
    const values = getValues();
    const vnumber = values?.vnumber;
    onClose(vnumber);
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 gap-6">
          <LabelledInput
            type="number"
            label="Visit Number"
            min="1"
            errorMessage={errors?.vnumber?.message}
            {...register('vnumber', { required: true, valueAsNumber: true })}
          />
        </div>
      </form>
      <DialogFooter className="grid-cols-1 gap-3 lg:grid-cols-2">
        <DialogClose asChild>
          <CancelButton />
        </DialogClose>
        <DialogClose asChild>
          <OkButton onClick={handleOk} disabled={!isValid} />
        </DialogClose>
      </DialogFooter>
    </>
  );
};
