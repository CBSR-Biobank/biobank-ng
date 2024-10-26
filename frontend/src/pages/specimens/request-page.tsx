import { SpecimenApi } from '@app/api/specimen-api';
import { BbButton } from '@app/components/bb-button';
import { cn } from '@app/utils';
import { faCircleExclamation, faGear } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useRef, useState } from 'react';
import { AdminPage } from '../admin-page';

const MAX_FILE_SIZE = 500000;

export const RequestPage: React.FC = () => {
  const hiddenFileInput = useRef<HTMLInputElement>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [fileSizeError, setFileSizeError] = useState(false);
  const [csvUploading, setCsvUploading] = useState(false);
  const [resultsReady, setResultsReady] = useState(false);

  const handleButtonClick = () => {
    if (!hiddenFileInput?.current) {
      return;
    }
    setFileSizeError(false);
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
    if (!selectedFile) {
      throw new Error('file is invalid');
    }
    SpecimenApi.specimenRequestUpload(selectedFile)
      .then(() => {
        setCsvUploading(false);
        setResultsReady(true);
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
            <BbButton size="lg" onClick={handleButtonClick} type="button" disabled={csvUploading}>
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
            Processing file...
            <FontAwesomeIcon icon={faGear} className={cn('bg-primary-300 h-6 w-6 animate-spin')} size="3x" />
          </div>
        )}

        {selectedFile && resultsReady && <div>show results here</div>}
      </div>
    </AdminPage>
  );
};
