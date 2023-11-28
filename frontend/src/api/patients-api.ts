import { collectionEventSchema } from '@app/models/collection-event';
import { patientSchema } from '@app/models/patient';
import { API_ROUTES, fetchApi } from './api';

export class PatientApi {
  static async getByPnumber(pnumber?: string) {
    if (!pnumber) {
      return null;
    }

    const route = API_ROUTES.patients.pnumber.replace(':pnumber', pnumber);
    const response = await fetchApi(route);
    const result = await response.json();
    const patient = patientSchema.parse(result);
    return patient;
  }

  static async getPatientCollectionEvent(pnumber?: string, vnumber?: number) {
    if (!pnumber || !vnumber) {
      return null;
    }

    const route = API_ROUTES.patients['collection-event']
      .replace(':pnumber', pnumber)
      .replace(':vnumber', vnumber.toString());
    const response = await fetchApi(route);
    const result = await response.json();
    const cevent = collectionEventSchema.parse(result);
    return cevent;
  }
}
