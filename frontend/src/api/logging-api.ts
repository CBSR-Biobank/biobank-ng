import { appLoggingSchema } from '@app/models/app-logging';
import { pagedQueryBuild } from '@app/models/paged-query';
import { paginatedResponseSchema } from '@app/models/paginated-response';
import { SortingState } from '@tanstack/react-table';
import { httpClient } from './api';

export class LoggingApi {
  static async paginate({
    page,
    size,
    searchTerm,
    sorting,
  }: {
    page?: number;
    size?: number;
    searchTerm?: string;
    sorting?: SortingState;
  }) {
    let query = pagedQueryBuild({ page: page ?? 0, size: size, search: searchTerm, sorting });

    const response = await await httpClient({
      method: 'GET',
      path: ['logging'],
      body: undefined,
      query,
    });
    const result = await response.json();
    return paginatedResponseSchema(appLoggingSchema).parse(result);
  }
}
