import { Status } from '@app/models/status';
import { studyNameSchema } from '@app/models/study';
import { z } from 'zod';
import { httpClient } from './api';

export class StudyApi {
  static async names(status?: Status) {
    const response = await httpClient({
      method: 'GET',
      path: ['studies', 'names'],
      body: undefined,
      query: { status }
    });
    const result = await response.json();
    const names = z.array(studyNameSchema).parse(result);
    return names;
  }
}
