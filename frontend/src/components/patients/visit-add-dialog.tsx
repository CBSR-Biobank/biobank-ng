import { faPlusCircle } from '@fortawesome/free-solid-svg-icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { EntityAddDialog } from '../entity-add-dialog';
import { LabelledInput } from '../forms/labelled-input';

export const VisitAddDialog: React.FC<{
  disallow: number[];
  onSubmit: (newVnumber: number) => void;
}> = ({ disallow, onSubmit }) => {
  const schema = z
    .object({
      vnumber: z.union([z.number().int().min(1, { message: 'should be 1 or greater' }), z.nan()]).optional()
    })
    .refine(
      (data) => {
        if (!data.vnumber) {
          return true;
        }
        return !disallow.includes(data.vnumber);
      },
      {
        path: ['vnumber'],
        message: 'already taken'
      }
    );

  const {
    register,
    getValues,
    reset,
    formState: { isValid, errors }
  } = useForm<z.infer<typeof schema>>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: {
      vnumber: undefined
    }
  });

  const handleSubmit = () => {
    const values = getValues();
    if (!values.vnumber) {
      throw new Error('invalid value for vnumber');
    }
    onSubmit(values.vnumber);
    reset();
  };

  const handleCancel = () => {
    reset();
  };

  return (
    <EntityAddDialog
      title="Patient: Add Visit"
      buttonLabel="Add Visit"
      buttonIcon={faPlusCircle}
      okButtonEnabled={isValid}
      onOk={handleSubmit}
      onCancel={handleCancel}
    >
      <form>
        <LabelledInput
          id="vnumber"
          type="number"
          label="Visit Number"
          min="1"
          errorMessage={errors?.vnumber?.message}
          {...register('vnumber', { required: true, valueAsNumber: true })}
        />
      </form>
    </EntityAddDialog>
  );
};
