import { useLogging } from '@app/hooks/use-logging';
import { AppLogging } from '@app/models/app-logging';
import { useAppLoggingsStore } from '@app/store';
import { ColumnDef, createColumnHelper } from '@tanstack/react-table';
import { format } from 'date-fns';
import { useMemo } from 'react';
import { CircularProgress } from '../circular-progress';
import { Pagination } from '../pagination';
import { ShowError } from '../show-error';
import { DataTable } from '../table/data-table';
import { TablePageSize } from '../table/table-page-size';

function getColumns(): ColumnDef<AppLogging, any>[] {
  const columnHelper = createColumnHelper<AppLogging>();
  return [
    columnHelper.accessor('createdAt', {
      header: () => 'Date',
      cell: ({ row }) => format(row.getValue('createdAt'), 'yyyy-MM-dd hh:mm'),
    }),
    columnHelper.accessor('action', {
      header: () => 'Action',
      cell: ({ row }) => row.getValue('action'),
    }),
    columnHelper.accessor('username', {
      header: () => 'User',
      cell: ({ row }) => row.getValue('username'),
    }),
    columnHelper.accessor('type', {
      header: () => 'Type',
      cell: ({ row }) => row.getValue('type'),
    }),
    columnHelper.accessor('center', {
      header: () => 'Site',
      cell: ({ row }) => row.getValue('center'),
    }),
    columnHelper.accessor('patientNumber', {
      header: () => 'Patient Number',
      cell: ({ row }) => row.getValue('patientNumber'),
    }),
    columnHelper.accessor('inventoryId', {
      header: () => 'Inventory Id',
      cell: ({ row }) => row.getValue('inventoryId'),
    }),
    columnHelper.accessor('locationLabel', {
      header: () => 'Location',
      cell: ({ row }) => row.getValue('locationLabel'),
    }),
    columnHelper.accessor('details', {
      header: () => 'Details',
      cell: ({ row }) => <div className="text-wrap">{row?.getValue('details')}</div>,
    }),
  ];
}

export const LoggingTable: React.FC = () => {
  const { page, pageSize, setPage, setPageSize } = useAppLoggingsStore();
  const { error, isError, isLoading, data: pagination } = useLogging({ page: page - 1, size: pageSize });

  const columns = useMemo(() => getColumns(), []);

  const handlePageChange = (newPage: number) => {
    if (page !== newPage) {
      setPage(newPage);
    }
  };

  const handlePageSizeChange = (size: number) => {
    setPageSize(size);
    setPage(1);
  };

  if (isError) {
    const innerError: any = error.error;
    if (innerError?.message) {
      if (innerError.message.includes('permission')) {
        return <ShowError message="You do not have the privileges to view this logging" />;
      }
    }
    return <ShowError error={error} />;
  }

  if (isLoading || !pagination) {
    return <CircularProgress />;
  }

  return (
    <div className="space-y-4">
      <div className="bg-slate=100/50 grid grid-cols-1 gap-8 overflow-x-auto">
        <DataTable data={pagination.content} columns={columns} totalItems={pagination.totalElements} />
      </div>
      <div className="flex justify-between pt-3">
        <TablePageSize size={pageSize ?? 0} onValueChange={handlePageSizeChange} />
        {!pagination.empty && <Pagination page={page} total={pagination.totalPages} onChange={handlePageChange} />}
      </div>
    </div>
  );
};
