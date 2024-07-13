import { StudyName } from '@app/models/study';
import { cn } from '@app/utils';

import { Check, ChevronsUpDown } from 'lucide-react';
import { useState } from 'react';
import { Control, FieldPathByValue, FieldValues, useController } from 'react-hook-form';

import { CommandList } from 'cmdk';
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem } from '../ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '../ui/popover';
import { ScrollArea } from '../ui/scroll-area';
import { FormLabel } from './form-label';

const classes =
  'flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-400 disabled:cursor-not-allowed disabled:opacity-50 border-2 border-slate-400';

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
  studies,
}: StudySelectProps<T, U>) {
  const { field } = useController({ control, name });
  const [popoverOpen, setPopoverOpen] = useState(false);
  const findStudy = (nameShort: String) => studies.find((study) => study.nameShort === nameShort);

  return (
    <div className="grid grid-cols-1 gap-2">
      <FormLabel>{label}</FormLabel>

      <Popover open={popoverOpen} onOpenChange={setPopoverOpen}>
        <PopoverTrigger>
          <span role="combobox" aria-expanded={popoverOpen} className={cn(classes, 'flex w-full justify-between')}>
            {findStudy(field.value)?.nameShort ?? 'Select a study'}
            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
          </span>
        </PopoverTrigger>
        <PopoverContent className="w-full overflow-y-auto bg-gray-100 p-2" asChild align="start">
          <ScrollArea className="w-full p-2 text-gray-700 md:h-[450px]">
            <Command>
              <CommandInput placeholder="Search..." className="m-1" />
              <CommandList>
                <CommandEmpty className="bg-warning-600 text-basic-100 ml-5 mt-5 rounded-md px-3 py-2 text-sm font-medium text-red-500">
                  That study does not exist
                </CommandEmpty>
                <CommandGroup>
                  {studies.map((option) => (
                    <CommandItem
                      key={option.id}
                      value={`${option.name} ${option.nameShort}`}
                      onSelect={() => {
                        setPopoverOpen(false);
                        field.onChange(option.nameShort);
                      }}
                    >
                      <Check
                        className={cn('mr-2 h-4 w-4', field.value === option.nameShort ? 'opacity-100' : 'opacity-0')}
                      />
                      <div className="flex flex-col items-start">
                        <p>{option.nameShort}</p>
                        <p className="text-sm text-muted-foreground md:max-w-[40rem]">{option.name}</p>
                      </div>
                    </CommandItem>
                  ))}
                </CommandGroup>
              </CommandList>
            </Command>
          </ScrollArea>
        </PopoverContent>
      </Popover>
    </div>
  );
}
