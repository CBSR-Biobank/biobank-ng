import { SpecimenApi } from '@app/api/specimen-api';
import { BbAlert } from '@app/components/bb-alert';
import { BbButton } from '@app/components/bb-button';
import { CircularProgress } from '@app/components/circular-progress';
import { StatusChip } from '@app/components/status-chip';
import { DataTable } from '@app/components/table/data-table';
import { DataTableColumnHeader } from '@app/components/table/table-column-header';
import { SpecimenPull } from '@app/models/specimen-pull';
import { faCircleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ColumnDef, createColumnHelper } from '@tanstack/react-table';
import { format } from 'date-fns';
import { useMemo, useRef, useState } from 'react';
import { AdminPage } from '../admin-page';

const MAX_FILE_SIZE = 500000;

function getColumns(): ColumnDef<SpecimenPull, any>[] {
  const columnHelper = createColumnHelper<SpecimenPull>();
  return [
    columnHelper.accessor('pnumber', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Patient" />,
      cell: ({ row }) => row.getValue('pnumber'),
      sortingFn: 'alphanumeric',
    }),
    columnHelper.accessor('inventoryId', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Inventory Id" />,
      cell: ({ row }) => row.getValue('inventoryId'),
    }),
    columnHelper.accessor('dateDrawn', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Date Drawn" />,
      cell: ({ row }) => format(row.getValue('dateDrawn'), 'yyyy-MM-dd'),
    }),
    columnHelper.accessor('specimenType', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Type" />,
      cell: ({ row }) => row.getValue('specimenType'),
    }),
    columnHelper.accessor('location', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Location" />,
      cell: ({ row }) => row.getValue('location'),
    }),
    columnHelper.accessor('activityStatus', {
      header: ({ column }) => <DataTableColumnHeader column={column} title="Status" />,
      cell: ({ row }) => <StatusChip status={row.original.activityStatus} variant="table" size="xs" />,
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

  const columns = useMemo(() => getColumns(), []);

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

  const handleCsvDownload = () => {
    const fileName = prompt('Enter file name (the .csv extension will be added automatically)', 'data');
    if (!fileName) return; // If the user cancels, exit the function

    const csvRows = [];
    csvRows.push('pnumber,inventoryId,dateDrawn,specimenType,location,activityStatus');

    results.forEach((pull) => {
      const values = [
        pull.pnumber,
        pull.inventoryId,
        format(pull.dateDrawn, 'yyy-MM-dd'),
        pull.specimenType,
        pull.location,
        pull.activityStatus,
      ];
      csvRows.push(values.join(','));
    });

    // Convert rows to a CSV blob
    const csvString = csvRows.join('\n');
    const blob = new Blob([csvString], { type: 'text/csv' });

    // Create a download link and trigger download
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${fileName}.csv`;
    a.click();

    // Clean up
    window.URL.revokeObjectURL(url);
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

        <div className="flex gap-1">
          <BbButton size="xl" onClick={handleCsvUpload} disabled={!selectedFile || csvUploading}>
            Generate
          </BbButton>
          <BbButton variant="secondary" size="xl" onClick={handleCsvDownload} disabled={!resultsReady}>
            Download CSV
          </BbButton>
        </div>

        {selectedFile && csvUploading && (
          <div className="flex items-center justify-start">
            <BbAlert variant="info" title="Processing request...">
              <CircularProgress />
            </BbAlert>
          </div>
        )}

        {selectedFile && resultsReady && <DataTable data={results} columns={columns} totalItems={results.length} />}
      </div>
    </AdminPage>
  );
};
