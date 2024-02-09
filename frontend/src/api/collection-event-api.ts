import { CollectionEventAdd, collectionEventSchema } from '@app/models/collection-event';
import { CommentAdd, commentSchema } from '@app/models/comment';
import { Patient } from '@app/models/patient';
import { z } from 'zod';
import { httpClient } from './api';

export class CollectionEventApi {
  static async getCollectionEvent(pnumber: string, vnumber: number) {
    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber, 'collection-events', vnumber],
      body: undefined,
      query: undefined
    });
    const result = await response.json();
    const cevent = collectionEventSchema.parse(result);
    return cevent;
  }

  static async add(patient: Patient, cevent: CollectionEventAdd) {
    const response = await httpClient({
      method: 'POST',
      path: ['patients', patient.pnumber, 'collection-events'],
      body: JSON.stringify({
        vnumber: cevent.visitNumber
      }),
      query: undefined
    });
    const result = await response.json();
    return collectionEventSchema.parse(result);
  }

  static async delete(patient: Patient, visitNumber: number) {
    await httpClient({
      method: 'DELETE',
      path: ['patients', patient.pnumber, 'collection-events', visitNumber],
      body: undefined,
      query: undefined
    });
  }

  // parameters are required to be optional due to TypeScript and useQuery
  static async getComments(pnumber: string, vnumber: number) {
    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber, 'collection-events', vnumber, 'comments'],
      body: undefined,
      query: undefined
    });
    const result = await response.json();
    return z.array(commentSchema).parse(result);
  }

  // parameters are required to be optional due to TypeScript and useQuery
  static async addComment(pnumber: string, vnumber: number, comment: CommentAdd) {
    const response = await httpClient({
      method: 'POST',
      path: ['patients', pnumber, 'collection-events', vnumber, 'comments'],
      body: JSON.stringify(comment),
      query: undefined
    });
    const result = await response.json();
    return commentSchema.parse(result);
  }
}
