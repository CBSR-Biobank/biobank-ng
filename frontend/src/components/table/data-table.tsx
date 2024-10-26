import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@app/components/ui/table';
import {
  ColumnDef,
  SortingState,
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  useReactTable,
} from '@tanstack/react-table';
import { TableViewOptions } from './table-view-options';

export function sortingToStringArray(sorting: SortingState) {
  return sorting.map((s) => `${s.desc ? '-' : ''}${s.id}`);
}

export function DataTable<TData>({
  data,
  columns,
  // page,
  // pageSize,
  totalItems,
  // sorting,
  // onSortingChange,
}: {
  data: TData[];
  columns: ColumnDef<TData, any>[];
  page?: number;
  pageSize?: number;
  totalItems: number;
  sorting?: SortingState;
  onSortingChange?: (sorting: SortingState) => void;
}) {
  const table = useReactTable({
    // debugTable: true,
    columns,
    data,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    // onSortingChange: (updater) => {
    //   if (!sorting || !onSortingChange) {
    //     return;
    //   }
    //   const newSortingValue = updater instanceof Function ? updater(sorting) : updater;
    //   onSortingChange(newSortingValue);
    // },
    //manualSorting: sorting !== undefined,
    rowCount: totalItems,
    // state: {
    //   pagination: {
    //     pageIndex: page ?? 1,
    //     pageSize: pageSize ?? Infinity,
    //   },
    //   sorting,
    // },
  });

  return (
    <div className="w-full">
      <TableViewOptions table={table} />
      <Table>
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <TableHead key={header.id}>
                  {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                </TableHead>
              ))}
            </TableRow>
          ))}
        </TableHeader>
        <TableBody>
          {table.getRowModel().rows?.length ? (
            table.getRowModel().rows.map((row) => (
              <TableRow key={row.id} data-state={row.getIsSelected() && 'selected'}>
                {row.getVisibleCells().map((cell) => (
                  <TableCell key={cell.id} className="p-3">
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </TableCell>
                ))}
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={columns.length} className="h-24 text-center">
                No results.
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </div>
  );
}
