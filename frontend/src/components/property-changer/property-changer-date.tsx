import { zodResolver } from '@hookform/resolvers/zod';
import { format } from 'date-fns';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { LabelledInput } from '../forms/labelled-input';
import { PropertyChangerDateProps } from './property-changer';
import { PropertyChangerDialog } from './property-changer-dialog';

export const PropertyChangerDate = ({
  propertyName,
  title,
  label,
  value,
  required,
  open,
  onClose
}: PropertyChangerDateProps) => {
  const requiredSchema = z.object({
    value: z.string()
  });

  const optionalSchema = z.object({
    value: z.string().or(z.literal('')) // allows empty string
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
    defaultValues: { value: value ? format(value, 'yyyy-MM-dd') : '' }
  });

  const handleOk = () => {
    const values = getValues();
    const value = values?.value;
    const date = value === '' ? null : new Date(value);
    onClose('ok', propertyName, date);
  };

  const handleCancel = () => {
    onClose('cancel', propertyName, null);
  };

  return (
    <PropertyChangerDialog
      title={title}
      required={required}
      open={open}
      size="sm"
      onOk={handleOk}
      onCancel={handleCancel}
      valid={isValid}
    >
      <LabelledInput
        type="date"
        label={label}
        errorMessage={errors?.value?.message}
        {...register('value')}
        pattern="\d{4}-\d{2}-\d{2}"
      />
    </PropertyChangerDialog>
  );
};
