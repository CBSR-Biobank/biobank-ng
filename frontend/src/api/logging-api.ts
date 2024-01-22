import { loggingSchema } from '@app/models/logging';
import { z } from 'zod';
import { httpClient } from './api';

export class LoggingApi {
  static async getLatest() {
    const response = await await httpClient({
      method: 'GET',
      path: ['logging', 'latest'],
      body: undefined,
      query: undefined
    });
    const result = await response.json();
    const logs = z.array(loggingSchema).parse(result);
    return logs;
  }
}
