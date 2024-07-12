import { Status, StatusLabels } from '@app/models/status';
import { useMemo } from 'react';
import { MutatorProps } from './mutator';
import { MutatorRadio } from './mutator-radio';

export function MutatorStatus({ value, required, onClose }: MutatorProps<Status>) {
  const statusOptions = useMemo(
    () =>
      (Object.keys(Status) as Array<keyof typeof Status>).map((key) => ({
        id: Status[key],
        label: StatusLabels[Status[key]],
      })),
    []
  );

  return (
    <MutatorRadio label="Status" value={value} required={required} propertyOptions={statusOptions} onClose={onClose} />
  );
}
