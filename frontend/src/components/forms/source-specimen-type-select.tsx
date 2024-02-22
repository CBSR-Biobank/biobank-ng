import { SourceSpecimenType } from '@app/models/source-specimen-type';

import { Control, FieldPathByValue, FieldValues, useController } from 'react-hook-form';

import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '../ui/select';
import { FormLabel } from './form-label';

const classes =
  'flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-400 disabled:cursor-not-allowed disabled:opacity-50 border-2 border-slate-400';

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
