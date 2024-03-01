import { CollectionEventComments } from '@app/components/collection-events/collection-event-comments';
import { InfoCard } from '@app/components/info-card';
import { Button } from '@app/components/ui/button';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { cn } from '@app/utils';

import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';

export const VisitCommentsCollapsible: React.FC<{ pnumber: string; vnumber: number; count: number }> = ({
  pnumber,
  vnumber,
  count,
}) => {
  const [open, setOpen] = useState(false);

  const handleOpenChange = (isOpen: boolean) => {
    setOpen(isOpen);
  };

  return (
    <Collapsible onOpenChange={handleOpenChange} className="rounded-md border-2 border-solid">
      <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
        <h4 className="text-sm text-slate-700">Comments: {count}</h4>
        <CollapsibleTrigger asChild>
          <Button variant="ghost" size="sm" className="w-9 p-0">
            <FontAwesomeIcon icon={faChevronRight} className={cn('duration-300 ease-in-out', { 'rotate-90': open })} />
            <span className="sr-only">Toggle</span>
          </Button>
        </CollapsibleTrigger>
      </div>
      <CollapsibleContent>
        {count <= 0 ? (
          <InfoCard
            variant="default"
            className="p-2"
            title="No Comments"
            message="This visit has no comments on record"
          />
        ) : (
          <CollectionEventComments pnumber={pnumber} vnumber={vnumber} />
        )}
      </CollapsibleContent>
    </Collapsible>
  );
};
