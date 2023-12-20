import { z } from 'zod';
import { API_ROUTES, fetchApi } from './api';
import { aliquotSchema } from '@app/models/specimen';

export class SpecimenApi {
  static async getByParentInventoryId(inventoryId?: string) {
    if (!inventoryId) {
      return null;
    }

    const route = API_ROUTES.specimens.aliquots.replace(':inventoryId', inventoryId);
    const response = await fetchApi(route);
    const result = await response.json();
    const specimens = z.array(aliquotSchema).parse(result);
    return specimens;
  }
}
