import { CircularProgress } from '@app/components/circular-progress';
import { LoggingTable } from '@app/components/logging/logging-table';
import { useAppLoggingsStore } from '@app/store';
import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { AdminPage } from '../admin-page';

export function LoggingPage() {
  const { page, pageSize, setPage, setPageSize } = useAppLoggingsStore();
  const [searchParams, setSearchParams] = useSearchParams();

  // this is used to prevent a query to the backend for default values in the store
  const [paramsParsed, setParamsParsed] = useState(false);

  // update state based on URL
  useEffect(() => {
    const pg = searchParams.get('page');
    if (pg) {
      setPage(parseInt(pg));
    }

    const sz = searchParams.get('size');
    if (sz) {
      setPageSize(parseInt(sz));
    }
    setParamsParsed(true);
  }, []);

  // update state based on user actions
  useEffect(() => {
    const params: any = { size: pageSize };

    if (page > 1) {
      params['page'] = `${page}`;
    }

    setSearchParams(params);
  }, [page, pageSize]);

  if (!paramsParsed) {
    return <CircularProgress />;
  }

  return (
    <AdminPage>
      <AdminPage.Title hasBorder>
        <p className="text-4xl font-semibold text-sky-600">Biobank Log</p>
        <p className="text-sm font-semibold text-gray-400">In reverse chronological order</p>
      </AdminPage.Title>
      <LoggingTable />
    </AdminPage>
  );
}
