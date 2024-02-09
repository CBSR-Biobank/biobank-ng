import { DialogClose, DialogFooter } from '@app/components/ui/dialog';
import { useStudySourceSpecimenTypes } from '@app/hooks/use-study';
import { zodResolver } from '@hookform/resolvers/zod';
import { SubmitHandler, useForm } from 'react-hook-form';
import { z } from 'zod';
import { CancelButton } from '../cancel-button';
import { CircularProgress } from '../circular-progress';
import { SourceSpecimenTypeSelect } from '../forms/source-specimen-type-select';
import { MutatorProps } from '../mutators/mutator';
import { OkButton } from '../ok-button';

const requiredSchema = z.object({
  nameShort: z.string()
});

const optionalSchema = z.object({
  nameShort: z.string().or(z.literal(''))
});

export const MutatorSourceSpecimenType: React.FC<MutatorProps<string> & { studyNameShort: string }> = ({
  studyNameShort,
  value,
  required,
  onClose
}) => {
  const schema = required ? requiredSchema : optionalSchema;
  const query = useStudySourceSpecimenTypes(studyNameShort);

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
      nameShort: value ?? ''
    }
  });

  const onSubmit: SubmitHandler<z.infer<typeof schema>> = () => {
    handleOk();
  };

  const handleOk = () => {
    const value = getValues().nameShort;
    onClose(value ?? null);
  };

  if (query.isLoading || !query.sourceSpecimenTypes) {
    return <CircularProgress />;
  }

  return (
    <>
      <form onSubmit={handleSubmit(onSubmit)}>
        <SourceSpecimenTypeSelect control={control} name="nameShort" specimenTypes={query.sourceSpecimenTypes} />
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
};
