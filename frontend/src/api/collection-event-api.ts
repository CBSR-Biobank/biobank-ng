import { patientSchema } from '@app/models/patient';
import { API_ROUTES, fetchApi } from './api';

export class CollectionEventApi {
  static async getByPnumber(pnumber: string) {
    const route = API_ROUTES.patients.pnumber.replace(':pnumber', pnumber);
    const response = await fetchApi(route);
    const result = await response.json();
    const person = patientSchema.parse(result);
    return person;
  }
}
