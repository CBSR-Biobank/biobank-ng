import { CollectionEventAdd, CollectionEventUpdate, collectionEventSchema } from '@app/models/collection-event';
import { CommentAdd, commentSchema } from '@app/models/comment';

import { z } from 'zod';

import { httpClient } from './api';

export class CollectionEventApi {
  static async getCollectionEvent(pnumber: string, vnumber: number) {
    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber, 'collection-events', vnumber],
      body: undefined,
      query: undefined,
    });
    const result = await response.json();
    const cevent = collectionEventSchema.parse(result);
    return cevent;
  }

  static async add(pnumber: string, cevent: CollectionEventAdd) {
    const response = await httpClient({
      method: 'POST',
      path: ['patients', pnumber, 'collection-events'],
      body: JSON.stringify({
        vnumber: cevent.vnumber,
      }),
      query: undefined,
    });
    const result = await response.json();
    return collectionEventSchema.parse(result);
  }

  static async update(pnumber: string, vnumber: number, cevent: CollectionEventUpdate) {
    const response = await httpClient({
      method: 'PUT',
      path: ['patients', pnumber, 'collection-events', vnumber],
      body: JSON.stringify(cevent),
      query: undefined,
    });
    const result = await response.json();
    return collectionEventSchema.parse(result);
  }

  static async delete(pnumber: string, visitNumber: number) {
    await httpClient({
      method: 'DELETE',
      path: ['patients', pnumber, 'collection-events', visitNumber],
      body: undefined,
      query: undefined,
    });
  }

  // parameters are required to be optional due to TypeScript and useQuery
  static async getComments(pnumber: string, vnumber: number) {
    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber, 'collection-events', vnumber, 'comments'],
      body: undefined,
      query: undefined,
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
      query: undefined,
    });
    const result = await response.json();
    return commentSchema.parse(result);
  }
}
