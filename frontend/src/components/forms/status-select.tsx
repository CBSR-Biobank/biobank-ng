import { Status, StatusLabels } from '@app/models/status';

import { FieldPathByValue, FieldValues, PathValue, UseControllerProps, useController } from 'react-hook-form';

import { Label } from '../ui/label';
import { RadioGroup, RadioGroupItem } from '../ui/radio-group';
import { FormLabel } from './form-label';

/**
 * Allows the user to select a Status value.
 *
 * Meant to be used in a react-hook-form.
 */
export function StatusSelect<T extends FieldValues, U extends FieldPathByValue<T, Status>>({
  control,
  name,
}: UseControllerProps<T, U>) {
  const { field } = useController({ control, name });
  const statusOptions = (Object.keys(Status) as Array<keyof typeof Status>)
    .filter((key) => key !== 'NONE')
    .map((key) => ({
      id: Status[key],
      label: StatusLabels[Status[key]],
    }));

  return (
    <div className="grid grid-cols-1 gap-2">
      <FormLabel>Status</FormLabel>
      <RadioGroup
        defaultValue={field.value}
        onValueChange={(value) => {
          const v = value as PathValue<T, U>;
          field.onChange(v);
        }}
      >
        {statusOptions.map((status) => {
          return (
            <div key={status.id} className="flex items-center space-x-2 pl-6">
              <RadioGroupItem value={status.id} id={status.id} />
              <Label htmlFor={status.id}>{status.label}</Label>
            </div>
          );
        })}
      </RadioGroup>
    </div>
  );
}
