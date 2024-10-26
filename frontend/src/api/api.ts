import { Status } from '@app/models/status';
import { userSchema } from '@app/models/user';
import { useUserStore } from '@app/store';

// For "Endpoint" type, see
// https://medium.com/flock-community/contract-first-strictly-typed-endpoints-in-typescript-with-runtime-validation-c409b2603287

type Auth = {
  method: 'GET';
  path: readonly ['auth'];
  query: undefined;
  body: undefined;
};

type ClinicNames = {
  method: 'GET';
  path: readonly ['clinics', 'names'];
  query: { status?: Status | undefined };
  body: undefined;
};

type Logging = {
  method: 'GET';
  path: readonly ['logging'];
  query: undefined;
  body: undefined;
};

type LoggingLatest = {
  method: 'GET';
  path: readonly ['logging', 'latest'];
  query: undefined;
  body: undefined;
};

type PatientCreate = {
  method: 'POST';
  path: readonly ['patients'];
  query: undefined;
  body: string;
};

type PatientUpdate = {
  method: 'PUT';
  path: readonly ['patients', string];
  query: undefined;
  body: string;
};

type PatientComments = {
  method: 'GET';
  path: readonly ['patients', string, 'comments'];
  query: undefined;
  body: undefined;
};

type PatientGet = {
  method: 'GET';
  path: readonly ['patients', string];
  query: undefined;
  body: undefined;
};

type PatientAddComment = {
  method: 'POST';
  path: readonly ['patients', string, 'comments'];
  query: undefined;
  body: string;
};

type CollectionEventsGet = {
  method: 'GET';
  path: readonly ['patients', string, 'collection-events'];
  query: undefined;
  body: undefined;
};

type CollectionEventGet = {
  method: 'GET';
  path: readonly ['patients', string, 'collection-events', number];
  query: undefined;
  body: undefined;
};

type CollectionEventCreate = {
  method: 'POST';
  path: readonly ['patients', string, 'collection-events'];
  query: undefined;
  body: string;
};

type CollectionEventUpdate = {
  method: 'PUT';
  path: readonly ['patients', string, 'collection-events', number];
  query: undefined;
  body: string;
};

type CollectionEventDelete = {
  method: 'DELETE';
  path: readonly ['patients', string, 'collection-events', number];
  query: undefined;
  body: undefined;
};

type CollectionEventListComments = {
  method: 'GET';
  path: readonly ['patients', string, 'collection-events', number, 'comments'];
  query: undefined;
  body: undefined;
};

type CollectionEventAddComment = {
  method: 'POST';
  path: readonly ['patients', string, 'collection-events', number, 'comments'];
  query: undefined;
  body: string;
};

type SourceSpecimenCreate = {
  method: 'POST';
  path: readonly ['specimens'];
  query: undefined;
  body: string;
};

type AliquotsGet = {
  method: 'GET';
  path: readonly ['specimens', string, 'aliquots'];
  query: undefined;
  body: undefined;
};

type SpecimenRequestPost = {
  method: 'POST';
  path: readonly ['specimens', 'request'];
  query: undefined;
  body: FormData;
};

type StudyCatalogue = {
  method: 'POST';
  path: readonly ['studies', 'catalogues', string];
  query: undefined;
  body: undefined;
};

type StudyCatalogueStatus = {
  method: 'GET';
  path: readonly ['studies', 'catalogues', string, string];
  query: undefined;
  body: undefined;
};

type StudyNames = {
  method: 'GET';
  path: readonly ['studies', 'names'];
  query: { status?: Status | undefined };
  body: undefined;
};

type StudyAnnotationTypes = {
  method: 'GET';
  path: readonly ['studies', string, 'annotation-types'];
  query: { status?: Status | undefined };
  body: undefined;
};

type SourceSpecimenTypes = {
  method: 'GET';
  path: readonly ['studies', string, 'source-specimen-types'];
  query: undefined;
  body: undefined;
};

export type Endpoint =
  | Auth
  | ClinicNames
  | Logging
  | LoggingLatest
  | PatientCreate
  | PatientUpdate
  | PatientComments
  | PatientGet
  | PatientAddComment
  | CollectionEventsGet
  | CollectionEventGet
  | CollectionEventCreate
  | CollectionEventUpdate
  | CollectionEventDelete
  | CollectionEventListComments
  | CollectionEventAddComment
  | SourceSpecimenCreate
  | AliquotsGet
  | SpecimenRequestPost
  | StudyCatalogue
  | StudyCatalogueStatus
  | StudyNames
  | StudyAnnotationTypes
  | SourceSpecimenTypes;

export type ApiError = {
  status: number;
  error: {
    details: string;
    timestamp?: string;
    message?: string;
    instance?: string;
    status?: number;
    title?: string;
    type?: string;
  };
};

export async function httpClient(endpoint: Endpoint, contentType: string | null = 'application/json') {
  const method = endpoint.method;
  const path = endpoint.path.join('/');
  let headers: HeadersInit = {
    Authorization: 'Bearer ' + useUserStore.getState().userToken,
    credentials: 'include',
  };

  if (contentType) {
    headers['Content-Type'] = contentType;
  }

  let body: any = undefined;
  if (endpoint.body) {
    if (contentType === 'application/json') {
      body = JSON.stringify(endpoint.body);
    } else {
      body = endpoint.body;
    }
  }

  let url = `/api/${path}`;
  if (endpoint.query !== undefined) {
    const queryString = new URLSearchParams(endpoint.query).toString();
    if (queryString.length > 0) {
      url += `?${queryString}`;
    }
  }

  const response = await fetch(url, { method, headers, body });
  return handleServerResponse(response);
}

export async function login(username: string, password: string) {
  const response = await fetch('/api/token', {
    headers: {
      authorization: 'Basic ' + window.btoa(username + ':' + password),
    },
    method: 'POST',
  });

  if (!response.ok) {
    if (response.status === 401) {
      useUserStore.getState().setUserToken(null);
    }
    const err: ApiError = {
      status: response.status,
      error: { details: response.status === 401 ? 'unauthorized' : 'unknown' },
    };
    console.error(err);
    throw err;
  }

  const token = response.headers.get('Authorization');
  useUserStore.getState().setUserToken(token);

  const user = userSchema.parse(await response.json());
  useUserStore.getState().setUser(user);
  return user;
}

export async function fetchAuthenticated() {
  const response = await httpClient({
    method: 'GET',
    path: ['auth'],
    body: undefined,
    query: undefined,
  });

  if (!response.ok) {
    const err: ApiError = { status: response.status, error: { details: 'Unable to authenticate' } };
    throw err;
  }

  const json = await response.json();
  return json;
}

export async function fetchApiFileUpload(route: string, file: File) {
  const data = new FormData();
  data.append('file', file);

  const response = await fetch(route, {
    method: 'POST',
    body: data,
    headers: {
      Authorization: 'Bearer ' + useUserStore.getState().userToken,
      credentials: 'include',
    },
  });

  return handleServerResponse(response);
}

export async function fetchApiFileDownload(url: string) {
  const response = await fetch(url, {
    headers: {
      Authorization: 'Bearer ' + useUserStore.getState().userToken,
      credentials: 'include',
    },
  });
  return response;
}

async function handleServerResponse(response: Response) {
  if (!response.ok) {
    if (response.status === 401) {
      useUserStore.getState().setUserToken(null);
    }

    let err: ApiError;
    let body = await response.text();
    if (body === '') {
      err = { status: response.status, error: { details: 'unknown error' } };
    } else {
      const json = body === '' ? {} : JSON.parse(body);
      err = { status: response.status, error: json };
    }

    console.error(err);
    throw err;
  }

  return response;
}
