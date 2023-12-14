import { Logging } from '@app/models/logging';
import format from 'date-fns/format';

export const LoggingTable: React.FC<{ logging: Logging[] }> = ({ logging }) => {
  return (
    <div className="bg-slate=100/50 grid grid-cols-1 gap-8 overflow-x-auto">
      <table className="w-full min-w-max table-auto text-left">
        <thead>
          <tr>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                User
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Date
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Action
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Type
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Site
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Patient Number
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Inventory Id
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Location
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Details
              </p>
            </th>
          </tr>
        </thead>
        <tbody>
          {logging.map((logMessage) => (
            <tr key={logMessage.id} className="bg-white odd:bg-slate-100/50 hover:bg-slate-200/50">
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.username}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {format(logMessage.createdAt, 'yyyy-MM-dd')}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.action}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.type}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.center}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.patientNumber}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.inventoryId}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.locationLabel}
                </p>
              </td>
              <td className="border-b border-gray-200 p-2">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {logMessage.details}
                </p>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
