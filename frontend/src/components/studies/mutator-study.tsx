import { DialogClose, DialogFooter } from '@app/components/ui/dialog';
import { useStudyNames } from '@app/hooks/use-study';

import { zodResolver } from '@hookform/resolvers/zod';
import { SubmitHandler, useForm } from 'react-hook-form';
import { z } from 'zod';

import { CancelButton } from '../cancel-button';
import { CircularProgress } from '../circular-progress';
import { StudySelect } from '../forms/study-select';
import { MutatorProps } from '../mutators/mutator';
import { OkButton } from '../ok-button';

const requiredSchema = z.object({
  studyNameShort: z.string(),
});

const optionalSchema = z.object({
  studyNameShort: z.string().or(z.literal('')),
});

export function MutatorStudy({ value, required, onClose }: MutatorProps<string>) {
  const schema = required ? requiredSchema : optionalSchema;
  const studyNamesQuery = useStudyNames();

  const {
    control,
    getValues,
    handleSubmit,
    formState: { isValid },
  } = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    mode: 'all',
    reValidateMode: 'onChange',
    defaultValues: {
      studyNameShort: value ?? undefined,
    },
  });

  const onSubmit: SubmitHandler<z.infer<typeof schema>> = () => {
    handleOk();
  };

  const handleOk = () => {
    const value = getValues().studyNameShort;
    onClose(value === '' ? null : value);
  };

  if (studyNamesQuery.isLoading || !studyNamesQuery.studyNames) {
    return <CircularProgress />;
  }

  return (
    <>
      <form onSubmit={handleSubmit(onSubmit)}>
        <StudySelect control={control} name="studyNameShort" studies={studyNamesQuery.studyNames} />
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
