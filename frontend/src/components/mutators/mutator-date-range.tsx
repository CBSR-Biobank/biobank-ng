import { DialogClose, DialogFooter } from '@app/components/ui/dialog';

import { zodResolver } from '@hookform/resolvers/zod';
import { format } from 'date-fns';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

import { CancelButton } from '../cancel-button';
import { LabelledInput } from '../forms/labelled-input';
import { OkButton } from '../ok-button';
import { MutatorProps } from './mutator';

export type DateRange = {
  startDate: Date;
  endDate: Date;
};

export type MutatorDateRangeProps = MutatorProps<DateRange>;

const schema = z
  .object({
    startDate: z.string(),
    endDate: z.string().or(z.literal('')), // allows empty string for endDate
  })
  .refine(
    (data) => {
      if (!data.startDate || !data.endDate) {
        return true;
      }
      const startDate = new Date(data.startDate);
      const endDate = new Date(data.endDate);
      return startDate <= endDate;
    },
    { message: 'must be later than start date' }
  );

export function MutatorDateRange({ value, onClose }: MutatorDateRangeProps) {
  const {
    register,
    getValues,
    watch,
    formState: { isValid, errors },
  } = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    mode: 'all',
    reValidateMode: 'onChange',
    defaultValues: {
      startDate: value?.startDate ? format(value.startDate, 'yyyy-MM-dd') : '',
      endDate: value?.endDate ? format(value.endDate, 'yyyy-MM-dd') : '',
    },
  });

  const watchStartDate = watch('startDate', undefined);
  const watchEndDate = watch('endDate', undefined);

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    handleOk();
  };

  const handleOk = () => {
    const values = getValues();
    onClose({
      startDate: new Date(values.startDate),
      endDate: new Date(values.endDate),
    });
  };

  return (
    <>
      <form onSubmit={handleSubmit} className="grid-columns-1 grid gap-4">
        <LabelledInput
          type="date"
          label="Start"
          errorMessage={errors?.startDate?.message}
          {...register('startDate')}
          max={watchEndDate}
          pattern="\d{4}-\d{2}-\d{2}"
        />
        <LabelledInput
          type="date"
          label="End"
          errorMessage={errors?.endDate?.message}
          {...register('endDate')}
          min={watchStartDate}
          pattern="\d{4}-\d{2}-\d{2}"
        />
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
}
