import { StatusChip } from '@app/components/status-chip';
import { Button } from '@app/components/ui/button';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@app/components/ui/table';
import { SourceSpecimen } from '@app/models/specimen';
import { cn } from '@app/utils';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import format from 'date-fns/format';
import { useState } from 'react';
import { AliquotsTable } from './aliquots-table';

type SpecimenState = Record<string, boolean>;

export const SourceSpecimenTable: React.FC<{ specimens: SourceSpecimen[] }> = ({ specimens }) => {
  const [sourceSpecimenOpen, setSourceSpecimenOpen] = useState<SpecimenState>({});

  const handleOpenChange = (inventoryId: string, open: boolean) => {
    setSourceSpecimenOpen((openState) => {
      const newState: SpecimenState = { ...openState };
      newState[inventoryId] = open;
      return newState;
    });
  };

  return (
    <div className="grid grid-cols-1 gap-8 overflow-x-auto">
      <Table className="w-full min-w-max table-auto text-left">
        <TableHeader>
          <TableRow>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Inventory ID</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Quantity</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Specimen Type</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Position</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Worksheet</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Created At</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Status</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Origin Center</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Current Center</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Comments</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {specimens.map((specimen) => (
            <Collapsible
              key={specimen.id}
              asChild
              onOpenChange={(open) => handleOpenChange(specimen.inventoryId, open)}
            >
              <>
                <TableRow>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    <CollapsibleTrigger asChild>
                      <Button variant="ghost" size="sm" className="pl-1">
                        <FontAwesomeIcon
                          icon={faChevronRight}
                          className={cn('text-sky-600 duration-300 ease-in-out', {
                            'rotate-90': sourceSpecimenOpen[specimen.inventoryId]
                          })}
                        />
                        <span className="sr-only">Toggle</span>
                      </Button>
                    </CollapsibleTrigger>
                    {specimen.inventoryId}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {specimen.quantity}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {specimen.specimenTypeNameShort}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {specimen.position}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {specimen.worksheet}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {format(specimen.createdAt, 'yyyy-MM-dd hh:mm')}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    <StatusChip status={specimen.status} variant="table" />
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {specimen.originCenterNameShort}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {specimen.currentCenterNameShort}
                  </TableCell>
                  <TableCell className=" text-sm font-normal leading-normal text-slate-700">
                    {specimen.hasComments ? 'Y' : 'N'}
                  </TableCell>
                </TableRow>
                <CollapsibleContent asChild>
                  <AliquotsTable parentSpecimen={specimen} />
                </CollapsibleContent>
              </>
            </Collapsible>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};
