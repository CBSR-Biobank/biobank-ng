import { cn } from '@app/utils';
import { useState } from 'react';
import { Label } from '../ui/label';
import { RadioGroup, RadioGroupItem } from '../ui/radio-group';
import { MutatorProps, PropertyOption } from './mutator';
import { MutatorDialog } from './mutator-dialog';

export function MutatorRadio({
  propertyName,
  title,
  label,
  value,
  open,
  onClose,
  propertyOptions
}: MutatorProps<unknown>) {
  const selectedOption = propertyOptions?.find((option) => option.id === value);
  const [input, setInput] = useState<PropertyOption<unknown> | undefined>(selectedOption);

  const handleChange = (newValue: string) => {
    const opt = propertyOptions?.find((option) => option.id === newValue);
    if (!opt) {
      throw new Error('could not assign value from selection');
    }
    setInput(opt);
  };

  const handleOk = () => {
    onClose('ok', propertyName, input?.id as string);
  };

  const handleCancel = () => {
    onClose('cancel', propertyName, undefined);
  };

  return (
    <MutatorDialog
      title={title}
      required={false}
      open={open}
      size="md"
      onOk={handleOk}
      onCancel={handleCancel}
      valid={!!input}
    >
      <p className={cn('text-sm font-semibold text-gray-500')}>{label}</p>
      <RadioGroup defaultValue={value as string} onValueChange={handleChange}>
        {(propertyOptions || []).map((option) => (
          <div key={option.id as string} className="flex items-center space-x-2">
            <RadioGroupItem value={option.id as string} className="border-primary-300 border-2" />
            <Label htmlFor={option.id as string} className="text-md py-1">
              {option.label}
            </Label>
          </div>
        ))}
      </RadioGroup>
    </MutatorDialog>
  );
}
