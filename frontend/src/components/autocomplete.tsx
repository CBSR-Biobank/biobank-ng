import { Button } from '@app/components/ui/button';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@app/components/ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@app/components/ui/popover';
import { ScrollArea } from '@app/components/ui/scroll-area';
import { cn } from '@app/utils';
import { cva, VariantProps } from 'class-variance-authority';
import { Check, ChevronsUpDown } from 'lucide-react';
import { useState } from 'react';
import { useDebouncedCallback } from 'use-debounce';

type AutocompleteOption<T> = {
  id: T;
  label: string;
};

const variants = cva('', {
  variants: {
    size: {
      sm: 'w-[40ch]',
      md: 'w-[60ch]',
      lg: 'w-[85ch]',
    },
  },
  defaultVariants: {
    size: 'md',
  },
});

export function Autocomplete<T>({
  label,
  value,
  placeholder = 'Search...',
  selectItemMsg = 'Select one...',
  unselect = false,
  unselectMsg = '-- None --',
  noResultMsg = 'that option does not exist',
  options,
  size,
  onInputChange,
  onSelect,
}: VariantProps<typeof variants> & {
  label: string;
  value?: T;
  placeholder?: string;
  selectItemMsg?: string;
  unselect?: boolean;
  unselectMsg?: string;
  noResultMsg?: string;
  options: AutocompleteOption<T>[];
  onInputChange?: (input: string) => void;
  onSelect?: (value?: T) => void;
}) {
  const selectedOption = options?.find((option) => option.id === value);
  const [input, setInput] = useState<AutocompleteOption<T> | undefined>(selectedOption);
  const [open, setOpen] = useState(false);

  const handlePopoverOpen = (open: boolean) => {
    setOpen(open);
    if (onInputChange) {
      onInputChange('');
    }
  };

  const handleSearch = useDebouncedCallback((value: string) => {
    if (onInputChange) {
      onInputChange(value);
    }
  }, 500);

  const handleUnselect = () => {
    setInput(undefined);
    setOpen(false);
    if (onSelect) {
      onSelect(undefined);
    }
  };

  const handleSelect = (option: AutocompleteOption<T>) => {
    setInput(option);
    setOpen(false);
    if (onSelect) {
      onSelect(option.id);
    }
  };

  return (
    <>
      <p className={cn('text-sm font-semibold text-gray-500')}>{label}</p>
      <Popover open={open} onOpenChange={handlePopoverOpen} modal={true}>
        <PopoverTrigger asChild>
          <Button
            type="button"
            variant="outline"
            role="combobox"
            aria-expanded={open}
            className="text-basic-600 w-full justify-between"
          >
            <>{input ? input.label : selectItemMsg}</>
            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="h-96 w-full bg-gray-100 p-2" asChild align="start">
          <Command shouldFilter={!onInputChange}>
            <CommandInput placeholder={placeholder} className="m-1" onValueChange={handleSearch} />
            <ScrollArea className={cn('flex h-full flex-col overflow-y-auto text-gray-700', variants({ size }))}>
              <CommandList>
                <CommandEmpty className="bg-warning-600 text-basic-100 ml-5 mt-5 rounded-md px-3 py-2">
                  {noResultMsg}
                </CommandEmpty>
                <CommandGroup>
                  {unselect && (
                    <CommandItem key="unselect" value="" onSelect={handleUnselect}>
                      <Check className={cn('mr-2', input ? 'opacity-0' : 'opacity-100')} />
                      {unselectMsg}
                    </CommandItem>
                  )}
                  {options.map((option) => (
                    <CommandItem key={`${option.id}`} onSelect={() => handleSelect(option)}>
                      <Check className={cn('mr-2 h-4 w-4', input?.id === option.id ? 'opacity-100' : 'opacity-0')} />
                      {option.label}
                    </CommandItem>
                  ))}
                </CommandGroup>
              </CommandList>
            </ScrollArea>
          </Command>
        </PopoverContent>
      </Popover>
    </>
  );
}
