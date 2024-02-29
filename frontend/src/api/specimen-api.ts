import { SourceSpecimenAdd, aliquotSchema, sourceSpecimenSchema } from '@app/models/specimen';

import { z } from 'zod';

import { httpClient } from './api';

export class SpecimenApi {
  static async getByParentInventoryId(inventoryId: string) {
    const response = await httpClient({
      method: 'GET',
      path: ['specimens', inventoryId, 'aliquots'],
      body: undefined,
      query: undefined,
    });
    const result = await response.json();
    const specimens = z.array(aliquotSchema).parse(result);
    return specimens;
  }

  static async add(specimen: SourceSpecimenAdd) {
    const response = await httpClient({
      method: 'POST',
      path: ['specimens'],
      body: JSON.stringify(specimen),
      query: undefined,
    });
    const result = await response.json();
    return sourceSpecimenSchema.parse(result);
  }
}
