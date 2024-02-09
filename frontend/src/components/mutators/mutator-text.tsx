import { DialogClose, DialogFooter } from '@app/components/ui/dialog';
import { cn } from '@app/utils';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { CancelButton } from '../cancel-button';
import { LabelledInput } from '../forms/labelled-input';
import { OkButton } from '../ok-button';
import { MutatorProps } from './mutator';

export type MutatorTextareaProps = MutatorProps<string> & {
  multiline?: boolean;
  maxlen?: number;
};

const classes =
  'flex w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50';

function makeSchema(required: boolean, minlength?: number, maxlength?: number) {
  let valueSchema = z.string().trim();

  if (required || minlength) {
    valueSchema = valueSchema.min(minlength ?? 1, { message: 'A value is required' });
  }

  if (maxlength) {
    valueSchema = valueSchema.max(maxlength);
  }

  if (!required) {
    return z.object({
      value: z.union([z.literal(''), valueSchema])
    });
  }

  return z.object({
    value: valueSchema
  });
}

export const MutatorText: React.FC<MutatorTextareaProps> = ({ label, value, required, onClose, multiline, maxlen }) => {
  const schema = makeSchema(required, 1, maxlen);

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
    onClose(value === '' ? undefined : value);
  };

  return (
    <>
      {multiline && <p className={cn('text-sm font-semibold text-gray-500')}>{label}</p>}
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 gap-6">
          {!multiline && <LabelledInput label={label} errorMessage={errors?.value?.message} {...register('value')} />}

          {multiline && (
            <textarea
              {...register('value')}
              className={cn(classes, {
                'min-h-[80px]': !value || value.length <= 200,
                'min-h-[200px]': value && value.length > 200
              })}
            />
          )}
        </div>
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
