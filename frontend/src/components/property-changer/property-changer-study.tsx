import { StudyApi } from '@app/api/study-api';
import { Status } from '@app/models/status';
import { zodResolver } from '@hookform/resolvers/zod';
import { useQuery } from '@tanstack/react-query';
import { SubmitHandler, useForm } from 'react-hook-form';
import { z } from 'zod';
import { CircularProgress } from '../circular-progress';
import { StudySelect } from '../forms/study-select';
import { PropertyChangerProps } from './property-changer';
import { PropertyChangerDialog } from './property-changer-dialog';

const requiredSchema = z.object({
  studyNameShort: z.string()
});

const optionalSchema = z.object({
  studyNameShort: z.string().or(z.literal(''))
});

export function PropertyChangerStudy({
  propertyName,
  title,
  value,
  required,
  open,
  onClose
}: PropertyChangerProps<string>) {
  const schema = required ? requiredSchema : optionalSchema;

  const {
    control,
    getValues,
    handleSubmit,
    formState: { isValid }
  } = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    mode: 'all',
    reValidateMode: 'onChange',
    defaultValues: {
      studyNameShort: value ?? ''
    }
  });

  const studyNamesQuery = useQuery(['studies', 'names'], async () => StudyApi.names(Status.ACTIVE), {
    keepPreviousData: true
  });

  const onSubmit: SubmitHandler<z.infer<typeof schema>> = () => {
    handleOk();
  };

  const handleOk = () => {
    const value = getValues().studyNameShort;
    onClose('ok', propertyName, value ?? null);
  };

  const handleCancel = () => {
    onClose('cancel', propertyName, null);
  };

  if (studyNamesQuery.isLoading || !studyNamesQuery.data) {
    return <CircularProgress />;
  }

  return (
    <PropertyChangerDialog
      size="md"
      title={title}
      required={required}
      open={open}
      onOk={handleOk}
      onCancel={handleCancel}
      valid={isValid}
    >
      <form onSubmit={handleSubmit(onSubmit)}>
        <StudySelect control={control} name="studyNameShort" studies={studyNamesQuery.data} />
      </form>
    </PropertyChangerDialog>
  );
}
