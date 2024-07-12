import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { LabelledInput } from '../forms/labelled-input';
import { MutatorProps } from './mutator';
import { MutatorFooter } from './mutator-footer';

export type MutatorNumberProps = MutatorProps<number> & {
  min?: number;
  max?: number;
};

export const MutatorNumber: React.FC<MutatorNumberProps> = ({
  label,
  value,
  required,
  onClose,
  min = 0,
  max = Number.MAX_SAFE_INTEGER,
}) => {
  const requiredSchema = z.object({
    value: z.coerce.number().gte(min).lte(max),
  });

  const optionalSchema = z.object({
    value: z.union([z.literal(''), z.coerce.number().gte(min).lte(max)]),
  });

  const schema = required ? requiredSchema : optionalSchema;

  const {
    register,
    getValues,
    formState: { isValid, errors },
  } = useForm<z.infer<typeof schema>>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: { value: value ?? '' },
  });

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    handleOk();
  };

  const handleOk = () => {
    const values = getValues();
    const value = values?.value;
    onClose(value === '' ? undefined : Number(value));
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 gap-6">
          <LabelledInput type="number" label={label} errorMessage={errors?.value?.message} {...register('value')} />
        </div>
      </form>
      <MutatorFooter isValueValid={isValid} onOk={handleOk} />
    </>
  );
};
