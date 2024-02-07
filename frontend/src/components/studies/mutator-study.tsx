import { StudyApi } from '@app/api/study-api';
import { DialogClose, DialogFooter } from '@app/components/ui/dialog';
import { Status } from '@app/models/status';
import { zodResolver } from '@hookform/resolvers/zod';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { SubmitHandler, useForm } from 'react-hook-form';
import { z } from 'zod';
import { CancelButton } from '../cancel-button';
import { CircularProgress } from '../circular-progress';
import { StudySelect } from '../forms/study-select';
import { MutatorProps } from '../mutators/mutator';
import { OkButton } from '../ok-button';

const requiredSchema = z.object({
  studyNameShort: z.string()
});

const optionalSchema = z.object({
  studyNameShort: z.string().or(z.literal(''))
});

export function MutatorStudy({ value, required, onClose }: MutatorProps<string>) {
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

  const studyNamesQuery = useQuery({
    queryKey: ['studies', 'names'],
    queryFn: async () => StudyApi.names(Status.ACTIVE),
    placeholderData: keepPreviousData
  });

  const onSubmit: SubmitHandler<z.infer<typeof schema>> = () => {
    handleOk();
  };

  const handleOk = () => {
    const value = getValues().studyNameShort;
    onClose(value ?? null);
  };

  if (studyNamesQuery.isLoading || !studyNamesQuery.data) {
    return <CircularProgress />;
  }

  return (
    <>
      <form onSubmit={handleSubmit(onSubmit)}>
        <StudySelect control={control} name="studyNameShort" studies={studyNamesQuery.data} />
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
