import { Button } from '@app/components/ui/button';
import { Popover, PopoverContent, PopoverTrigger } from '@app/components/ui/popover';
import { ScrollArea } from '@app/components/ui/scroll-area';
import { cn } from '@app/utils';
import { Check, ChevronsUpDown } from 'lucide-react';
import { useState } from 'react';
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem } from '../ui/command';
import { MutatorProps, PropertyOption } from './mutator';
import { MutatorDialog } from './mutator-dialog';

export function MutatorSelect({
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
  const [popoverOpen, setPopoverOpen] = useState(false);

  const handleChange = (option: PropertyOption<unknown>) => {
    setInput(option);
    setPopoverOpen(false);
  };

  const handleOk = () => {
    onClose('ok', propertyName, input?.id as string);
    handleOk();
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
      <Popover open={popoverOpen} onOpenChange={setPopoverOpen}>
        <PopoverTrigger asChild>
          <Button
            variant="outline"
            role="combobox"
            aria-expanded={popoverOpen}
            className="text-basic-600 w-full justify-between"
          >
            <>{input ? input.label : 'Select one...'}</>
            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-full overflow-y-auto bg-gray-100 p-2" asChild align="start">
          <ScrollArea className="h-[200px] w-96 text-gray-700">
            <Command>
              <CommandInput placeholder="Search..." className="m-1" />
              <CommandEmpty className="bg-warning-600 text-basic-100 ml-5 mt-5 rounded-md px-3 py-2">
                That option does not exist
              </CommandEmpty>
              <CommandGroup>
                {propertyOptions &&
                  propertyOptions.map((option, index) => (
                    <CommandItem key={index} onSelect={() => handleChange(option)}>
                      <Check className={cn('mr-2 h-4 w-4', input === option ? 'opacity-100' : 'opacity-0')} />
                      {option.label}
                    </CommandItem>
                  ))}
              </CommandGroup>
            </Command>
          </ScrollArea>
        </PopoverContent>
      </Popover>
    </MutatorDialog>
  );
}
