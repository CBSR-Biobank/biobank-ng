import { CommentAdd, commentSchema } from '@app/models/comment';
import { Patient, PatientAdd, PatientUpdate, patientSchema } from '@app/models/patient';
import { z } from 'zod';
import { httpClient } from './api';

export class PatientApi {
  static async getByPnumber(pnumber: string) {
    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber],
      body: undefined,
      query: undefined
    });
    const result = await response.json();
    const patient = patientSchema.parse(result);
    return patient;
  }

  static async getPatientComments(pnumber: string) {
    const response = await httpClient({
      method: 'GET',
      path: ['patients', pnumber, 'comments'],
      body: undefined,
      query: undefined
    });
    const result = await response.json();
    const cevent = z.array(commentSchema).parse(result);
    return cevent;
  }

  static async add(patient: PatientAdd) {
    const response = await httpClient({
      method: 'POST',
      path: ['patients'],
      body: JSON.stringify(patient),
      query: undefined
    });
    const result = await response.json();
    return patientSchema.parse(result);
  }

  static async update(pnumber: string, newValues: PatientUpdate) {
    const data = {
      pnumber: newValues.pnumber,
      studyNameShort: newValues.studyNameShort
    };
    const response = await httpClient({
      method: 'PUT',
      path: ['patients', pnumber],
      body: JSON.stringify(data),
      query: undefined
    });
    const result = await response.json();
    return patientSchema.parse(result);
  }

  // parameters are required to be optional due to TypeScript and useQuery
  static async addComment(patient: Patient, comment: CommentAdd) {
    const response = await httpClient({
      method: 'POST',
      path: ['patients', patient.pnumber, 'comments'],
      body: JSON.stringify(comment),
      query: undefined
    });
    const result = await response.json();
    return commentSchema.parse(result);
  }
}
