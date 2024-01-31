import { Status, StatusLabels } from '@app/models/status';
import { MutatorProps } from './mutator';
import { MutatorRadio } from './mutator-radio';

export function MutatorStatus({ propertyName, title, value, required, open, onClose }: MutatorProps<unknown>) {
  const statusOptions = (Object.keys(Status) as Array<keyof typeof Status>).map((key) => ({
    id: Status[key],
    label: StatusLabels[Status[key]]
  }));

  return (
    <MutatorRadio
      propertyName={propertyName}
      title={title}
      open={open}
      label="Status"
      value={value}
      required={required}
      propertyOptions={statusOptions}
      onClose={onClose}
    />
  );
}
