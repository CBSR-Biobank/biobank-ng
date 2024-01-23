import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { LabelledInput } from '../forms/labelled-input';
import { PropertyChangerNumberProps } from './property-changer';
import { PropertyChangerDialog } from './property-changer-dialog';

export const PropertyChangerNumber: React.FC<PropertyChangerNumberProps> = ({
  propertyName,
  title,
  label,
  value,
  required,
  open,
  onClose,
  min = 0,
  max = Number.MAX_SAFE_INTEGER
}) => {
  const requiredSchema = z.object({
    value: z.coerce.number().gte(min).lte(max)
  });

  const optionalSchema = z.object({
    value: z.union([z.literal(''), z.coerce.number().gte(min).lte(max)])
  });

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
    onClose('ok', propertyName, value === '' ? null : Number(value));
  };

  const handleCancel = () => {
    onClose('cancel', propertyName, null);
  };

  return (
    <PropertyChangerDialog
      title={title}
      required={required}
      open={open}
      size="lg"
      onOk={handleOk}
      onCancel={handleCancel}
      valid={isValid}
    >
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 gap-6">
          <LabelledInput type="number" label={label} errorMessage={errors?.value?.message} {...register('value')} />
        </div>
      </form>
    </PropertyChangerDialog>
  );
};
