import { StatusChip } from '@app/components/status-chip';
import { SourceSpecimen } from '@app/models/specimen';
import format from 'date-fns/format';

export const SourceSpecimenTable: React.FC<{ specimens: SourceSpecimen[] }> = ({ specimens }) => {
  return (
    <div className="grid grid-cols-1 gap-8 overflow-x-auto">
      <table className="w-full min-w-max table-auto text-left">
        <thead>
          <tr>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Inventory ID
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Quantity
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Specimen Type
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Status
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Time Drawn
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Current Center
              </p>
            </th>
            <th className="border-y border-gray-200 p-3"></th>
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
                <div className="flex">
                  <StatusChip status={specimen.status} />
                </div>
              </td>

              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {format(specimen.timeDrawn, 'yyyy-MM-dd')}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {specimen.currentCenterNameShort}
                </p>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
