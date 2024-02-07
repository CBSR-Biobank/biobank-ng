import { InfoCard } from '@app/components/info-card';
import { Button } from '@app/components/ui/button';
import { CollectionEventBrief } from '@app/models/collection-event';
import { cn } from '@app/utils';
import { format } from 'date-fns';
import { Link } from 'react-router-dom';

const buttonClass = 'transition ease-in-out delay-150 hover:scale-[1.15] duration-200';

// see https://tailwindcomponents.com/component/table-4

export const CollectionEventTable: React.FC<{ collectionEvents: CollectionEventBrief[] }> = ({ collectionEvents }) => {
  if (collectionEvents.length <= 0) {
    return <InfoCard title="No Visits" message="This patient does not have any visits on record" />;
  }

  return (
    <div className="grid grid-cols-1 gap-8 overflow-x-auto pt-6">
      <table className="w-full min-w-max table-auto text-left">
        <thead>
          <tr>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Visit
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Date
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Specimens
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Aliquots
              </p>
            </th>
            <th className="border-y border-gray-200 p-3"></th>
          </tr>
        </thead>
        <tbody>
          {collectionEvents.map((cevent) => (
            <tr key={cevent.id}>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {cevent.visitNumber}
                </p>
              </td>

              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {cevent.createdAt ? format(cevent.createdAt, 'yyyy-MM-dd') : ''}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {cevent.specimenCount}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {cevent.aliquotCount}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <Link to={`${cevent.visitNumber}`} className="place-self-end">
                  <Button className={cn(buttonClass, 'h-6 py-1 text-xs uppercase')} size="sm">
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
