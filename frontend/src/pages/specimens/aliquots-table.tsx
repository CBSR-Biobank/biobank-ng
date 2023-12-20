import { Alert, AlertDescription } from '@app/components/alert';
import { CircularProgress } from '@app/components/circular-progress';
import { ShowError } from '@app/components/show-error';
import { StatusChip } from '@app/components/status-chip';
import { useAliquots } from '@app/hooks/use-specimen';
import { SourceSpecimen } from '@app/models/specimen';
import { faWarning } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import format from 'date-fns/format';

export const AliquotsTable: React.FC<{ parentSpecimen: SourceSpecimen }> = ({ parentSpecimen }) => {
  const specimensQry = useAliquots(parentSpecimen.inventoryId);
  const { data: specimens } = specimensQry;

  if (specimensQry.isError) {
    return <ShowError error={specimensQry.error} />;
  }

  if (specimensQry.isLoading || !specimens) {
    return <CircularProgress />;
  }

  if (specimens.length <= 0) {
    return (
      <div className='m-5'>
        <Alert className="border-amber-500 bg-amber-200 text-slate-600">
          <FontAwesomeIcon icon={faWarning} />
          <AlertDescription>
            No aliquots for this source specimen yet.
          </AlertDescription>
        </Alert>
      </div>);
  }

  return (
    <div className="grid grid-cols-1 gap-8 overflow-x-auto">
      <table className="w-full min-w-max table-auto text-left">
        <thead>
          <tr>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Inventory ID
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Quantity
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Specimen Type
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Position
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Status
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Created At
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Origin Center
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Current Center
              </p>
            </th>
            <th className="border-b border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Comments
              </p>
            </th>
            <th className="border-b border-gray-200 p-3"></th>
          </tr>
        </thead>
        <tbody>
          {specimens.map((specimen) => (
            <tr key={specimen.id}>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.inventoryId}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.quantity}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.specimenTypeNameShort}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.position}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <StatusChip status={specimen.status} variant="table" />
              </td>

              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {format(specimen.createdAt, 'yyyy-MM-dd')}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.originCenterNameShort}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.currentCenterNameShort}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.hasComments ? "Y" : "N"}
                </p>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
