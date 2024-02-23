import { SourceSpecimenType } from '@app/models/source-specimen-type';

import { Control, FieldPathByValue, FieldValues, useController } from 'react-hook-form';

import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '../ui/select';
import { FormLabel } from './form-label';

export interface StudySelectProps<T extends FieldValues, U extends FieldPathByValue<T, string>> {
  control?: Control<T>;
  name: U;
  specimenTypes: SourceSpecimenType[];
}

/**
 * Allows the user to select a Study.
 *
 * Meant to be used in a react-hook-form.
 */
export function SourceSpecimenTypeSelect<T extends FieldValues, U extends FieldPathByValue<T, string>>({
  control,
  name,
  specimenTypes,
}: StudySelectProps<T, U>) {
  const { field } = useController({ control, name });

  return (
    <div className="grid grid-cols-1 gap-2">
      <FormLabel>Specimen Type</FormLabel>
      <Select>
        <SelectTrigger className="w-full overflow-y-auto">
          <SelectValue placeholder="Select a specimen type" />
        </SelectTrigger>
        <SelectContent>
          <SelectGroup>
            <SelectLabel>Specimen Type</SelectLabel>
            {specimenTypes.map((option) => (
              <SelectItem
                key={option.nameShort}
                value={option.nameShort}
                onSelect={() => {
                  field.onChange(option.nameShort);
                }}
              >
                {option.nameShort}
              </SelectItem>
            ))}
          </SelectGroup>
        </SelectContent>
      </Select>
    </div>
  );
}
