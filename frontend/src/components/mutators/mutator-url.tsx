import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { LabelledInput } from '../forms/labelled-input';
import { MutatorProps } from './mutator';
import { MutatorFooter } from './mutator-footer';

const requiredSchema = z.object({
  value: z.string().trim().url(),
});

const optionalSchema = z.object({
  value: z.union([z.literal(''), z.string().trim().url()]),
});

export const MutatorUrl: React.FC<MutatorProps<string>> = ({ label, value, required, onClose }) => {
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
    onClose(value === '' ? undefined : value);
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 gap-6">
          <LabelledInput type="url" label={label} errorMessage={errors?.value?.message} {...register('value')} />
        </div>
      </form>
      <MutatorFooter isValueValid={isValid} onOk={handleOk} />
    </>
  );
};
