import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@app/components/ui/select';
import { useState } from 'react';
import { MutatorProps } from './mutator';
import { MutatorFooter } from './mutator-footer';

export type MutatorSelectProps<T> = MutatorProps<T> & {
  allowNone: boolean;
};

export function MutatorSelect<T>({ label, value, allowNone = false, onClose, propertyOptions }: MutatorSelectProps<T>) {
  const [selected, setSelected] = useState<string | undefined>(value ? `${value}` : allowNone ? 'none' : '');

  if (!propertyOptions) {
    throw new Error('invalid property options');
  }

  const handleOk = () => {
    const v = propertyOptions?.find((opt) => `${opt.id}` === selected);
    onClose(v?.id);
  };

  const handleValueChange = (newValue: string) => {
    if (allowNone && newValue === 'none') {
      setSelected(undefined);
      return;
    }

    setSelected(newValue);
  };

  const options = propertyOptions.map((opt) => ({ ...opt, id: `${opt.id}` }));

  return (
    <>
      <p className="text-sm font-semibold text-gray-500">{label}</p>
      <Select value={selected} onValueChange={handleValueChange}>
        <SelectTrigger>
          <SelectValue placeholder="Select a value" />
        </SelectTrigger>
        <SelectContent>
          {allowNone && <SelectItem value="none">-- None --</SelectItem>}
          {options.map((opt) => (
            <SelectItem value={opt.id} key={opt.id}>
              {opt.label}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
      <MutatorFooter isValueValid={true} onOk={handleOk} />
    </>
  );
}
