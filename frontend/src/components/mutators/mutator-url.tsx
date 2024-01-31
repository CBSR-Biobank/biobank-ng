import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { LabelledInput } from '../forms/labelled-input';
import { MutatorProps } from './mutator';
import { MutatorDialog } from './mutator-dialog';

const requiredSchema = z.object({
  value: z.string().trim().url()
});

const optionalSchema = z.object({
  value: z.union([z.literal(''), z.string().trim().url()])
});

export const MutatorUrl: React.FC<MutatorProps<string>> = ({
  propertyName,
  title,
  label,
  value,
  required,
  open,
  onClose
}) => {
  const schema = required ? requiredSchema : optionalSchema;

  const {
    register,
    getValues,
    formState: { isValid, errors }
  } = useForm<z.infer<typeof schema>>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: { value }
  });

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    handleOk();
  };

  const handleOk = () => {
    const values = getValues();
    const value = values?.value;
    onClose('ok', propertyName, value === '' ? null : value);
  };

  const handleCancel = () => {
    onClose('cancel', propertyName, null);
  };

  return (
    <MutatorDialog
      title={title}
      open={open}
      required={required}
      size="md"
      onOk={handleOk}
      onCancel={handleCancel}
      valid={isValid}
    >
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 gap-6">
          <LabelledInput type="url" label={label} errorMessage={errors?.value?.message} {...register('value')} />
        </div>
      </form>
    </MutatorDialog>
  );
};
