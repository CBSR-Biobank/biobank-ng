import { Status, StatusLabels } from '@app/models/status';
import { PropertyChangerProps } from './property-changer';
import { PropertyChangerRadio } from './property-changer-radio';

export function PropertyChangerStatus({
  propertyName,
  title,
  value,
  required,
  open,
  onClose
}: PropertyChangerProps<unknown>) {
  const statusOptions = (Object.keys(Status) as Array<keyof typeof Status>).map((key) => ({
    id: Status[key],
    label: StatusLabels[Status[key]]
  }));

  return (
    <PropertyChangerRadio
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
