import { Button } from '@app/components/ui/button';
import { CollectionEvent } from '@app/models/collection-event';
import { cn } from '@app/utils';
import format from 'date-fns/format';
import { Link } from 'react-router-dom';

const buttonClass = 'transition ease-in-out delay-150 hover:scale-[1.15] duration-200';

export const CollectionEventTable: React.FC<{ collectionEvents: CollectionEvent[] }> = ({ collectionEvents }) => {
  return (
    <div className="grid grid-cols-1 gap-8 overflow-x-auto">
      <table className="w-full min-w-max table-auto text-left">
        <thead>
          <tr>
            <th className="border-y border-gray-200 p-4">
              <p className="block font-sans text-sm font-normal leading-none text-gray-900 antialiased opacity-70">
                Visit
              </p>
            </th>
            <th className="border-y border-gray-200 p-4">
              <p className="block font-sans text-sm font-normal leading-none text-gray-900 antialiased opacity-70">
                Date
              </p>
            </th>
            <th className="border-y border-gray-200 p-4">
              <p className="block font-sans text-sm font-normal leading-none text-gray-900 antialiased opacity-70">
                Specimens
              </p>
            </th>
            <th className="border-y border-gray-200 p-4">
              <p className="block font-sans text-sm font-normal leading-none text-gray-900 antialiased opacity-70">
                Aliquots
              </p>
            </th>
            <th className="border-y border-gray-200 p-4"></th>
          </tr>
        </thead>
        <tbody>
          {collectionEvents.map((cevent) => (
            <tr key={cevent.id}>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-gray-900 antialiased">
                  {cevent.visitNumber}
                </p>
              </td>

              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-gray-900 antialiased">
                  {format(cevent.createdAt, 'yyyy-MM-dd')}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-gray-900 antialiased">
                  {cevent.specimenCount}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-gray-900 antialiased">
                  {cevent.aliquotCount}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <Link to="" className="place-self-end">
                  <Button
                    className={cn(
                      buttonClass,
                      'h-6 bg-sky-200 py-1 text-xs font-semibold text-sky-500 hover:bg-sky-300'
                    )}
                    size="sm"
                  >
                    View
                  </Button>
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
