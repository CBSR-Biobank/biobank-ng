import { clinicNameSchema } from '@app/models/clinic';
import { Status } from '@app/models/status';

import { z } from 'zod';

import { httpClient } from './api';

export class ClinicApi {
  static async names(status?: Status) {
    const response = await httpClient({
      method: 'GET',
      path: ['clinics', 'names'],
      body: undefined,
      query: { status },
    });
    const result = await response.json();
    const names = z.array(clinicNameSchema).parse(result);
    return names;
  }
}
