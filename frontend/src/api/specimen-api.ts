import { aliquotSchema } from '@app/models/specimen';
import { z } from 'zod';
import { httpClient } from './api';

export class SpecimenApi {
  static async getByParentInventoryId(inventoryId?: string) {
    if (!inventoryId) {
      return null;
    }

    const response = await httpClient({
      method: 'GET',
      path: ['specimens', inventoryId, 'aliquots'],
      query: undefined
    });
    const result = await response.json();
    const specimens = z.array(aliquotSchema).parse(result);
    return specimens;
  }
}
