import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { LabelledInput } from '../forms/labelled-input';
import { MutatorProps } from './mutator';
import { MutatorFooter } from './mutator-footer';

export type MutatorRegexProps = MutatorProps<string> & {
  regex: RegExp;
};

export function MutatorRegex({ label, value, required, regex, onClose }: MutatorRegexProps) {
  const valueSchema = z.string().regex(regex, { message: 'Invalid value' });
  const requiredSchema = z.object({ value: valueSchema });

  const optionalSchema = z.object({
    value: z.union([z.literal(''), valueSchema]),
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
}
