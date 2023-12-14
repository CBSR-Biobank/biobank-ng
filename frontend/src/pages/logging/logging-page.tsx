import { CircularProgress } from '@app/components/circular-progress';
import { LoggingTable } from '@app/components/logging/logging-table';
import { ShowError } from '@app/components/show-error';
import { useLoggingLatest } from '@app/hooks/use-logging';
import { AdminPage } from '../admin-page';

export function LoggingPage() {
  const loggingQry = useLoggingLatest();
  const { data: logging } = loggingQry;

  if (loggingQry.isError) {
    const error: any = loggingQry.error.error;
    if (error.message) {
      if (error.message.includes('permission')) {
        return <ShowError message="You do not have the privileges to view this logging" />;
      }
    }
    return <ShowError error={loggingQry.error} />;
  }

  if (loggingQry.isLoading || !logging) {
    return <CircularProgress />;
  }

  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Latest Logging</p>
        <p className="text-sm font-semibold text-gray-400">In reverse order</p>
      </AdminPage.Title>
      <div className="space-y-2 rounded-md border-2 border-solid">
        <LoggingTable logging={logging} />
      </div>
    </AdminPage>
  );
}
