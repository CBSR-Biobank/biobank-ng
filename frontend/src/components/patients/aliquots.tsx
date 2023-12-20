import { Button } from '@app/components/ui/button';
import { Collapsible, CollapsibleContent } from '@app/components/ui/collapsible';
import { SourceSpecimen } from '@app/models/specimen';
import { AliquotsTable } from '@app/pages/specimens/aliquots-table';
import { cn } from '@app/utils';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { CollapsibleTrigger } from '@radix-ui/react-collapsible';
import { useState } from 'react';

export const Aliquots: React.FC<{ parentSpecimen: SourceSpecimen }> = ({ parentSpecimen }) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <Collapsible open={isOpen} onOpenChange={setIsOpen} className="rounded-md border-2 border-solid">
      <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
        <h4 className="text-sm font-normal text-slate-700">
          Aliquots for {parentSpecimen.inventoryId}
          <p className="text-xs text-slate-700/60">({parentSpecimen.specimenTypeNameShort})</p>
        </h4>
        <CollapsibleTrigger asChild>
          <Button variant="ghost" size="sm" className="w-9 p-0">
            <FontAwesomeIcon
              icon={faChevronRight}
              className={cn('text-sky-600 duration-300 ease-in-out', { 'rotate-90': isOpen })}
            />
            <span className="sr-only">Toggle</span>
          </Button>
        </CollapsibleTrigger>
      </div>
      <CollapsibleContent className="space-y-2">
        {isOpen && <AliquotsTable parentSpecimen={parentSpecimen} />}
      </CollapsibleContent>
    </Collapsible>
  );
};
