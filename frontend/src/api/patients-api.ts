import { collectionEventSchema } from '@app/models/collection-event';
import { patientSchema } from '@app/models/patient';
import { httpClient } from './api';

export class PatientApi {
  static async getByPnumber(pnumber?: string) {
    if (!pnumber) {
      return null;
    }

    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber],
      query: undefined
    });
    const result = await response.json();
    const patient = patientSchema.parse(result);
    return patient;
  }

  static async getPatientCollectionEvent(pnumber?: string, vnumber?: number) {
    if (!pnumber || !vnumber) {
      return null;
    }

    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber, 'collection-events', vnumber],
      query: undefined
    });
    const result = await response.json();
    const cevent = collectionEventSchema.parse(result);
    return cevent;
  }
}
