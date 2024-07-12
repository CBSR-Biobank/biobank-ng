import { MutatorProps } from './mutator';
import { MutatorRadio } from './mutator-radio';

export function MutatorYesNo({ value, required, onClose }: MutatorProps<boolean>) {
  const options = [
    { id: true, label: 'Yes' },
    { id: false, label: 'No' }
  ];

  return <MutatorRadio label="Status" value={value} required={required} propertyOptions={options} onClose={onClose} />;
}
