import { InfoCard } from '@app/components/info-card';
import { SourceSpecimenTable } from '@app/components/specimens/source-specimens-table';
import { Button } from '@app/components/ui/button';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { cn } from '@app/utils';

import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';

import { SourceSpecimen } from '@app/models/specimen';

export const SpecimensCollapsible: React.FC<{ sourceSpecimens: SourceSpecimen[] }> = ({ sourceSpecimens }) => {
  const [open, setOpen] = useState(false);

  const handleOpenChange = (isOpen: boolean) => {
    setOpen(isOpen);
  };

  return (
    <Collapsible onOpenChange={handleOpenChange} className="rounded-md border-2 border-solid" defaultOpen>
      <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
        <h4 className="text-sm text-slate-700">Specimens: {sourceSpecimens.length}</h4>
        <CollapsibleTrigger asChild>
          <Button variant="ghost" size="sm" className="w-9 p-0">
            <FontAwesomeIcon icon={faChevronRight} className={cn('duration-300 ease-in-out', { 'rotate-90': open })} />
            <span className="sr-only">Toggle</span>
          </Button>
        </CollapsibleTrigger>
      </div>
      <CollapsibleContent>
        {sourceSpecimens.length <= 0 ? (
          <InfoCard className="p-2" title="No Specimens" message="Specimens have been added to this visit yet" />
        ) : (
          <SourceSpecimenTable specimens={sourceSpecimens} />
        )}
      </CollapsibleContent>
    </Collapsible>
  );
};
