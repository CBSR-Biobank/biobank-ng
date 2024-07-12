import { zodResolver } from '@hookform/resolvers/zod';
import { format } from 'date-fns';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

import { LabelledInput } from '../forms/labelled-input';
import { MutatorProps } from './mutator';
import { MutatorFooter } from './mutator-footer';

export type MutatorDateProps = MutatorProps<Date> & {
  minDate?: Date;
  maxDate?: Date;
};

export const MutatorDate = ({ label, value, onClose }: MutatorDateProps) => {
  const schema = z.object({
    value: z.string(),
  });

  const {
    register,
    getValues,
    formState: { isValid, errors },
  } = useForm<z.infer<typeof schema>>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: { value: value ? format(value, 'yyyy-MM-dd') : '' },
  });

  const handleOk = () => {
    const values = getValues();
    const value = values?.value;
    const date = new Date(value);
    onClose(date);
  };

  return (
    <>
      <LabelledInput
        type="date"
        label={label}
        errorMessage={errors?.value?.message}
        {...register('value')}
        pattern="\d{4}-\d{2}-\d{2}"
      />
      <MutatorFooter isValueValid={isValid} onOk={handleOk} />
    </>
  );
};
