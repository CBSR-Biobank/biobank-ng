import { CircularProgress } from '@app/components/circular-progress';
import { InfoCard } from '@app/components/info-card';
import { ShowError } from '@app/components/show-error';
import { StatusChip } from '@app/components/status-chip';
import { TableCell, TableRow } from '@app/components/ui/table';
import { useAliquots } from '@app/hooks/use-specimen';
import { SourceSpecimen } from '@app/models/specimen';

import { format } from 'date-fns';
import { forwardRef } from 'react';

export type AliquotsTableProps = {
  parentSpecimen: SourceSpecimen;
};

export const AliquotsTable = forwardRef<HTMLTableElement, AliquotsTableProps>(({ parentSpecimen }, forwardedRef) => {
  const specimensQry = useAliquots(parentSpecimen.inventoryId);
  const { data: specimens } = specimensQry;

  if (specimensQry.isError) {
    return (
      <TableRow>
        <TableCell colSpan={10}>
          <ShowError error={specimensQry.error} />
        </TableCell>
      </TableRow>
    );
  }

  if (specimensQry.isLoading || !specimens) {
    return (
      <TableRow>
        <TableCell colSpan={10}>
          <CircularProgress />
        </TableCell>
      </TableRow>
    );
  }

  if (specimens.length <= 0) {
    return (
      <TableRow>
        <TableCell colSpan={10}>
          <InfoCard
            ref={forwardedRef}
            className="pl-8"
            title="No Aliquots"
            message="This source specimen has no aliquots on record."
          />
        </TableCell>
      </TableRow>
    );
  }

  return (
    <>
      {specimens.map((specimen) => (
        <TableRow key={specimen.id}>
          <TableCell className="text-sm font-normal leading-normal text-slate-700">
            <p className="pl-8">{specimen.inventoryId}</p>
          </TableCell>
          <TableCell className=" text-sm font-normal leading-normal text-slate-700">{specimen.quantity}</TableCell>
          <TableCell className=" text-sm font-normal leading-normal text-slate-700">
            {specimen.specimenTypeNameShort}
          </TableCell>
          <TableCell className=" text-sm font-normal leading-normal text-slate-700">{specimen.position}</TableCell>
          <TableCell className=" text-sm font-normal leading-normal text-slate-700">{specimen.worksheet}</TableCell>
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
      ))}
    </>
  );
});
