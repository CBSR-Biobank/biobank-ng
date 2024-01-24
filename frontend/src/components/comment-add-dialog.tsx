import { faComment } from '@fortawesome/free-solid-svg-icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { EntityAddDialog } from './entity-add-dialog';

const schema = z.object({
  message: z.string()
});

export type FormInputs = z.infer<typeof schema>;

export const CommentAddDialog: React.FC<{ onSubmit: (newComment: string) => void }> = ({ onSubmit }) => {
  const {
    register,
    getValues,
    reset,
    formState: { isValid, errors }
  } = useForm<FormInputs>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: {
      message: ''
    }
  });

  const errorMessage = errors?.message?.message;

  const handleSubmit = () => {
    const values = getValues();
    onSubmit(values.message);
    reset();
  };

  return (
    <EntityAddDialog
      title="Patient"
      message="Add a comment"
      buttonLabel="Add Comment"
      buttonIcon={faComment}
      okButtonEnabled={isValid}
      onOk={handleSubmit}
    >
      <form>
        <div className="grid grid-cols-1 gap-6">
          <textarea
            {...register('message')}
            className="min-h-[150px] rounded-md"
            placeholder="type your comment here"
          />
          {errorMessage && (
            <div className="text-sm text-red-600">
              <span role="alert">{errorMessage}</span>
            </div>
          )}
        </div>
      </form>
    </EntityAddDialog>
  );
};
