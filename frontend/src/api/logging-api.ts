import { loggingSchema } from '@app/models/logging';
import { z } from 'zod';
import { API_ROUTES, fetchApi } from './api';

export class LoggingApi {
  static async getLatest() {
    const route = API_ROUTES.logging.latest;
    const response = await fetchApi(route);
    const result = await response.json();
    const logs = z.array(loggingSchema).parse(result);
    return logs;
  }
}
