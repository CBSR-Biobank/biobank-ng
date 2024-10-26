import { SpecimenApi } from '@app/api/specimen-api';
import { BbButton } from '@app/components/bb-button';
import { CircularProgress } from '@app/components/circular-progress';
import { StatusChip } from '@app/components/status-chip';
import { DataTable } from '@app/components/table/data-table';
import { DataTableColumnHeader } from '@app/components/table/table-column-header';
import { RowAction } from '@app/components/table/table-row-actions';
import { Alert, AlertTitle } from '@app/components/ui/alert';
import { faCircleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ColumnDef, createColumnHelper } from '@tanstack/react-table';
import { useMemo, useRef, useState } from 'react';
import { AdminPage } from '../admin-page';

const MAX_FILE_SIZE = 500000;

type SpecimenPull = {
  pnumber: string;
  inventoryId: string;
  dateDrawn: string;
  specimenType: string;
  location: string;
  status: string;
};

function getColumns(actions: RowAction<SpecimenPull>[]): ColumnDef<SpecimenPull, any>[] {
  const columnHelper = createColumnHelper<SpecimenPull>();
  return [
    columnHelper.accessor('pnumber', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Patient" />,
      cell: ({ row }) => row.getValue('pnumber'),
    }),
    columnHelper.accessor('inventoryId', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Inventory Id" />,
      cell: ({ row }) => row.getValue('inventoryId'),
    }),
    columnHelper.accessor('dateDrawn', {
      header: () => 'Date Drawn',
      cell: ({ row }) => row.getValue('dateDrawn'),
    }),
    columnHelper.accessor('specimenType', {
      header: () => 'Type',
      cell: ({ row }) => row.getValue('specimenType'),
    }),
    columnHelper.accessor('location', {
      header: () => 'Location',
      cell: ({ row }) => row.getValue('location'),
    }),
    columnHelper.accessor('status', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Status" />,
      cell: ({ row }) => <StatusChip status={row.original.status} variant="table" size="xs" />,
    }),
  ];
}

export const RequestPage: React.FC = () => {
  const hiddenFileInput = useRef<HTMLInputElement>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [fileSizeError, setFileSizeError] = useState(false);
  const [csvUploading, setCsvUploading] = useState(false);
  const [resultsReady, setResultsReady] = useState(false);
  const [results, setResults] = useState<SpecimenPull[]>([]);

  const columns = useMemo(
    () =>
      getColumns([
        {
          label: 'Update',
          onSelect: (specimenPull: SpecimenPull) => {
            // TODO: do something here?
            console.log(specimenPull);
          },
        },
      ]),
    []
  );

  const handleBrowseClick = () => {
    if (!hiddenFileInput?.current) {
      return;
    }
    setFileSizeError(false);
    setCsvUploading(false);
    setResultsReady(false);
    setResults([]);
    setSelectedFile(null);
    hiddenFileInput.current.click();
  };

  const handleFileSelected = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files || e.target.files.length <= 0) {
      setSelectedFile(null);
      return;
    }

    const file = e.target.files[0];
    setSelectedFile(file);
    setFileSizeError(file.size > MAX_FILE_SIZE);
  };

  const handleCsvUpload = () => {
    setCsvUploading(true);
    setResultsReady(false);
    setResults([]);

    if (!selectedFile) {
      throw new Error('file is invalid');
    }
    SpecimenApi.specimenRequestUpload(selectedFile)
      .then((result) => {
        setCsvUploading(false);
        setResultsReady(true);
        setResults(result);
      })
      .catch((e) => {
        setCsvUploading(false);
        console.log(JSON.stringify(e));
      });
  };

  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Specimen Request</p>
      </AdminPage.Title>
      <div className="mt-4 grid grid-cols-1 gap-8">
        <p>Generate a list of specimen locations from information in a CSV file.</p>
        <form>
          <div className="flex items-center gap-2">
            <p className="text-basic-500">CSV File:</p>
            <p className="text-primary-400 flex-1 rounded-md border-2 p-2 shadow-md">
              {selectedFile ? selectedFile.name : 'None'}
            </p>
            <BbButton size="lg" onClick={handleBrowseClick} type="button" disabled={csvUploading}>
              Browse
            </BbButton>
          </div>
          <input ref={hiddenFileInput} className="hidden" type="file" accept=".csv" onChange={handleFileSelected} />
        </form>

        {fileSizeError && (
          <div className="bg-warning-500 flex items-center gap-2 rounded-md px-3 py-2 text-white">
            <FontAwesomeIcon icon={faCircleExclamation} />
            The file is too big. Select a different one.
          </div>
        )}

        <div className="flex">
          <BbButton size="xl" onClick={handleCsvUpload} disabled={!selectedFile || csvUploading}>
            Generate
          </BbButton>
        </div>

        {selectedFile && csvUploading && (
          <div className="flex items-center justify-start">
            <Alert variant="info">
              <AlertTitle>Processing request...</AlertTitle>
              <CircularProgress />
            </Alert>
          </div>
        )}

        {selectedFile && resultsReady && <DataTable data={results} columns={columns} totalItems={results.length} />}
      </div>
    </AdminPage>
  );
};
