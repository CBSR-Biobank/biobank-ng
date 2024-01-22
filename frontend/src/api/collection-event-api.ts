import { patientSchema } from '@app/models/patient';
import { httpClient } from './api';

export class CollectionEventApi {
  static async getByPnumber(pnumber: string) {
    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber, 'collection-events'],
      body: undefined,
      query: undefined
    });

    const result = await response.json();
    const person = patientSchema.parse(result);
    return person;
  }
}
