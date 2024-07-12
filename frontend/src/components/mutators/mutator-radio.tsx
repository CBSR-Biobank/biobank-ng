import { cn } from '@app/utils';
import { useState } from 'react';
import { Label } from '../ui/label';
import { RadioGroup, RadioGroupItem } from '../ui/radio-group';
import { MutatorProps, PropertyOption } from './mutator';
import { MutatorFooter } from './mutator-footer';

export function MutatorRadio<T>({ label, value, onClose, propertyOptions }: MutatorProps<T>) {
  const selectedOption = propertyOptions?.find((option) => option.id === value);
  const [input, setInput] = useState<PropertyOption<T> | undefined>(selectedOption);

  const handleChange = (newValue: string) => {
    const opt = propertyOptions?.find((option) => `${option.id}` === newValue);
    if (!opt) {
      throw new Error('could not assign value from selection');
    }
    setInput(opt);
  };

  const handleOk = () => {
    onClose(input?.id);
  };

  const defaultValue = propertyOptions?.find((opt) => opt.id === value);

  return (
    <>
      <p className={cn('text-sm font-semibold text-gray-500')}>{label}</p>
      <RadioGroup defaultValue={`${defaultValue?.id}`} onValueChange={handleChange}>
        {(propertyOptions || []).map((option) => (
          <div key={`${option.id}`} className="flex items-center space-x-2">
            <RadioGroupItem value={`${option.id}`} className="border-primary-300 border-2" />
            <Label htmlFor={`${option.id}`} className="text-md py-1">
              {option.label}
            </Label>
          </div>
        ))}
      </RadioGroup>
      <MutatorFooter isValueValid={true} onOk={handleOk} />
    </>
  );
}
