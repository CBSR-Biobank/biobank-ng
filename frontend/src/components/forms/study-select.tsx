import { StudyName } from '@app/models/study';

import { useState } from 'react';
import { Control, FieldPathByValue, FieldValues, useController } from 'react-hook-form';
import { Autocomplete } from '../autocomplete';

export interface StudySelectProps<T extends FieldValues, U extends FieldPathByValue<T, string>> {
  label?: string;
  control?: Control<T>;
  name: U;
  studies: StudyName[];
}

/**
 * Allows the user topd select a Study.
 *
 * Meant to be used in a react-hook-form.
 */
export function StudySelect<T extends FieldValues, U extends FieldPathByValue<T, string>>({
  label = 'Study',
  control,
  name,
  studies: allStudies,
}: StudySelectProps<T, U>) {
  const { field } = useController({ control, name });
  const [studies, setStudies] = useState<StudyName[] | undefined>(undefined);

  const handleInputChange = (value: string) => {
    setStudies(
      allStudies.filter(
        (study) => study.nameShort.toLowerCase().includes(value) || study.name.toLowerCase().includes(value)
      )
    );
  };

  const handleSelect = (value?: string) => {
    field.onChange(value);
  };

  const options = (studies ?? []).map((study) => ({
    id: study.nameShort,
    label: `${study.name} (${study.nameShort})`,
  }));

  return (
    <Autocomplete
      label={label}
      value={undefined}
      placeholder="Enter words used in the study name..."
      selectItemMsg="Select a study..."
      noResultMsg="That study does not exist"
      options={options}
      size="lg"
      onInputChange={handleInputChange}
      onSelect={handleSelect}
    />
  );
}
