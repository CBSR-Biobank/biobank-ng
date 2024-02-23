import { useClinicNames } from '@app/hooks/use-clinic';
import { useStudySourceSpecimenTypes } from '@app/hooks/use-study';
import { SourceSpecimenAdd } from '@app/models/specimen';
import { Status } from '@app/models/status';
import { faVial } from '@fortawesome/free-solid-svg-icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { CircularProgress } from '../circular-progress';
import { EntityAddDialog } from '../entity-add-dialog';
import { ClinicSelect } from '../forms/clinic-select';
import { FormLabel } from '../forms/form-label';
import { LabelledInput } from '../forms/labelled-input';
import { SourceSpecimenTypeSelect } from '../forms/source-specimen-type-select';
import { StatusSelect } from '../forms/status-select';
import { ShowError } from '../show-error';

const schema = z.object({
  clinicNameShort: z.string().or(z.literal('')),
  inventoryId: z.string().min(1, { message: 'an ID is required' }),
  typeNameShort: z.string().or(z.literal('')),
  timeDrawn: z.string().min(1, { message: 'a time is required' }),
  status: z.nativeEnum(Status),
  comment: z.string().optional(),
});

export type FormInputs = z.infer<typeof schema>;

export const SourceSpecimenAddDialog: React.FC<{
  studyNameShort: string;
  title?: string;
  onSubmit: (newSpecimen: SourceSpecimenAdd) => void;
}> = ({ studyNameShort, title, onSubmit }) => {
  const clinicNamesQry = useClinicNames();
  const query = useStudySourceSpecimenTypes(studyNameShort);

  const now = new Date();
  now.setUTCHours(0, 0, 0, 0);

  const {
    control,
    register,
    getValues,
    reset,
    formState: { isValid, errors },
  } = useForm<FormInputs>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: {
      clinicNameShort: '',
      typeNameShort: '',
      status: Status.ACTIVE,
      timeDrawn: now.toISOString().substring(0, 16),
    },
  });

  const handleSubmit = () => {
    const values = getValues();
    onSubmit({
      inventoryId: values.inventoryId,
      timeDrawn: new Date(values.timeDrawn),
      status: values.status,
      specimenTypeNameShort: values.typeNameShort,
      originCenterNameShort: values.clinicNameShort,
    });
    reset();
  };

  const handleCancel = () => {
    reset();
  };

  if (clinicNamesQry.isLoading || !clinicNamesQry.clinicNames) {
    return <CircularProgress />;
  }

  return (
    <EntityAddDialog
      title={title ?? 'Entity'}
      message="Add a source specimen"
      buttonLabel="Add Specimen"
      buttonIcon={faVial}
      okButtonEnabled={isValid}
      onOk={handleSubmit}
      onCancel={handleCancel}
    >
      <form>
        <div className="grid grid-cols-1 gap-6">
          {query.isError ? (
            <ShowError error={query.error} />
          ) : (
            <>
              {clinicNamesQry.isLoading || !clinicNamesQry.clinicNames ? (
                <CircularProgress />
              ) : (
                <ClinicSelect control={control} name="clinicNameShort" clinics={clinicNamesQry.clinicNames} />
              )}

              <LabelledInput
                id="inventoryId"
                label="Inventory ID"
                errorMessage={errors?.inventoryId?.message}
                {...register('inventoryId')}
              />

              {query.isLoading || !query.sourceSpecimenTypes ? (
                <CircularProgress />
              ) : (
                <SourceSpecimenTypeSelect
                  control={control}
                  name="typeNameShort"
                  specimenTypes={query.sourceSpecimenTypes}
                />
              )}

              <LabelledInput
                type="datetime-local"
                label="Time Drawn"
                errorMessage={errors?.timeDrawn?.message}
                {...register('timeDrawn')}
                required
              />

              <StatusSelect control={control} name="status" />

              <FormLabel>Comment</FormLabel>
              <textarea
                {...register('comment')}
                className="min-h-[150px] rounded-md"
                placeholder="type your comment here"
              />
              {errors?.comment?.message && (
                <div className="text-sm text-red-600">
                  <span role="alert">{errors?.comment?.message}</span>
                </div>
              )}
            </>
          )}
        </div>
      </form>
    </EntityAddDialog>
  );
};
