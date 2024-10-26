import { SourceSpecimenAdd, aliquotSchema, sourceSpecimenSchema } from '@app/models/specimen';

import { z } from 'zod';

import { specimenPullSchema } from '@app/models/specimen-pull';
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

  static async specimenRequestUpload(file: File) {
    const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const data = new FormData();
    data.append('file', file);
    data.append('timezone', timeZone);

    const response = await httpClient(
      {
        method: 'POST',
        path: ['specimens', 'request'],
        body: data,
        query: undefined,
      },
      null
    );
    const result = await response.json();
    return z.array(specimenPullSchema).parse(result);
  }
}
