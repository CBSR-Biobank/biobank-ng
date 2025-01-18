import { ApiError, fetchApiFileDownload } from '@app/api/api';
import { StudyApi } from '@app/api/study-api';
import { CatalogueState } from '@app/models/catalogue-state';
import { CatalogueStatus } from '@app/models/catalogue-status';
import { AdminPage } from '@app/pages/admin-page';
import { faCircleInfo, faWarning } from '@fortawesome/free-solid-svg-icons';
import { ReactNode, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { BackButton } from '../back-button';
import { BbAlert } from '../bb-alert';
import { BbButton } from '../bb-button';
import { CircularProgress } from '../circular-progress';
import { ShowError } from '../show-error';
import { AlertDescription, AlertTitle } from '../ui/alert';

const Wrapper: React.FC<{ studyName?: string; children: ReactNode }> = ({ studyName, children }) => {
  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-sm font-semibold text-gray-400">{studyName}</p>
        <p className="text-4xl font-semibold text-sky-600">Study Catalogue</p>
      </AdminPage.Title>
      {children}
    </AdminPage>
  );
};

export const CatalogueWaitComplete: React.FC = () => {
  const navigate = useNavigate();
  const { studyName, catId } = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const [data, setData] = useState<CatalogueStatus | null>(null);
  const [error, setError] = useState<ApiError | null>(null);

  const longRunningTask = () => {
    return new Promise<CatalogueStatus>((resolve, reject) => {
      const interval = setInterval(async () => {
        let result: CatalogueStatus | null = null;

        try {
          if (studyName && catId) {
            result = await StudyApi.catalogueStatus(studyName, catId);
          }

          if (result) {
            if (result.state === CatalogueState.COMPLETED || result.state === CatalogueState.CANCELLED) {
              clearInterval(interval);
              resolve(result);
            }
          }
        } catch (err) {
          clearInterval(interval);
          reject(err);
        }
      }, 1000);
    });
  };

  const handleLongRunningTask = async () => {
    try {
      const result = await longRunningTask();
      setData(result);
    } catch (err) {
      setError(err as ApiError);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    handleLongRunningTask();
  }, []);

  const downloadFile = async () => {
    if (!data) {
      throw new Error('data is invalid');
    }

    if (!data.fileUrl) {
      throw new Error('file url is invalid');
    }

    try {
      const response = await fetchApiFileDownload(data.fileUrl);
      const path = data.fileUrl.split('/');
      const filename = path[path.length - 1];
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error(error);
    }
  };

  const backClicked = () => {
    navigate('..');
  };

  if (error) {
    if (error.error.details === 'invalid task id') {
      return (
        <Wrapper studyName={studyName}>
          <BbAlert variant="warning" icon={faWarning} title="This catalogue is no longer available" />
          <div className="flex flex-col gap-3 pt-8 md:w-max md:flex-row">
            <BackButton onClick={backClicked} />
          </div>
        </Wrapper>
      );
    }
    return <ShowError error={error} />;
  }

  if (isLoading) {
    return (
      <Wrapper studyName={studyName}>
        <BbAlert variant="info" title="Waiting for catalogue...">
          <CircularProgress />
        </BbAlert>
      </Wrapper>
    );
  }

  if (data?.state === CatalogueState.CANCELLED) {
    return (
      <Wrapper studyName={studyName}>
        <BbAlert variant="warning" icon={faWarning} title="This catalogue is no longer available" />
        <div className="flex flex-col gap-3 pt-8 md:w-max md:flex-row">
          <BackButton onClick={backClicked} />
        </div>
      </Wrapper>
    );
  }

  return (
    <Wrapper studyName={studyName}>
      <div className="flex flex-col gap-3 pt-8">
        <BbAlert variant="info" icon={faCircleInfo}>
          <AlertTitle>Catalouge file is ready</AlertTitle>
          <AlertDescription>Press the download button to save the file</AlertDescription>
        </BbAlert>
        <div className="flex flex-row gap-3 pt-8 md:w-max md:flex-row">
          <BbButton size="xl" onClick={downloadFile}>
            Download File
          </BbButton>
        </div>
      </div>
    </Wrapper>
  );
};
