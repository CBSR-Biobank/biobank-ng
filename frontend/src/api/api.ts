import { Status } from '@app/models/status';
import { userSchema } from '@app/models/user';
import { useUserStore } from '@app/store';

// For "Endpoint" type, see
// https://medium.com/flock-community/contract-first-strictly-typed-endpoints-in-typescript-with-runtime-validation-c409b2603287

type Auth = {
  method: 'GET';
  path: ['auth'];
  query: undefined;
  body: undefined;
};

type Logging = {
  method: 'GET';
  path: ['logging'];
  query: undefined;
  body: undefined;
};

type LoggingLatest = {
  method: 'GET';
  path: ['logging', 'latest'];
  query: undefined;
  body: undefined;
};

type PatientCreate = {
  method: 'POST';
  path: ['patients'];
  query: undefined;
  body: string;
};

type PatientUpdate = {
  method: 'PUT';
  path: ['patients', string];
  query: undefined;
  body: string;
};

type PatientComments = {
  method: 'GET';
  path: ['patients', string, 'comments'];
  query: undefined;
  body: undefined;
};

type PatientGet = {
  method: 'GET';
  path: ['patients', string];
  query: undefined;
  body: undefined;
};

type PatientAddComment = {
  method: 'POST';
  path: ['patients', string, 'comments'];
  query: undefined;
  body: string;
};

type CollectionEventsGet = {
  method: 'GET';
  path: ['patients', string, 'collection-events'];
  query: undefined;
  body: undefined;
};

type CollectionEventGet = {
  method: 'GET';
  path: ['patients', string, 'collection-events', number];
  query: undefined;
  body: undefined;
};

type AliquotsGet = {
  method: 'GET';
  path: ['specimens', string, 'aliquots'];
  query: undefined;
  body: undefined;
};

type StudyNames = {
  method: 'GET';
  path: ['studies', 'names'];
  query: { status?: Status | undefined };
  body: undefined;
};

export type Endpoint =
  | Auth
  | Logging
  | LoggingLatest
  | PatientCreate
  | PatientUpdate
  | PatientComments
  | PatientGet
  | PatientAddComment
  | CollectionEventsGet
  | CollectionEventGet
  | AliquotsGet
  | StudyNames;

export type ApiError = {
  status: number;
  error: any;
};

export async function httpClient(endpoint: Endpoint) {
  const method = endpoint.method;
  const path = endpoint.path.join('/');
  const body = endpoint.body ?? null;
  const headers = {
    Authorization: 'Bearer ' + useUserStore.getState().userToken,
    credentials: 'include',
    'Content-Type': 'application/json'
  };

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
      authorization: 'Basic ' + window.btoa(username + ':' + password)
    },
    method: 'POST'
  });

  if (!response.ok) {
    if (response.status === 401) {
      useUserStore.getState().setUserToken(null);
    }
    const err: ApiError = {
      status: response.status,
      error: response.status === 401 ? 'unauthrorized' : 'unknown'
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
    query: undefined
  });

  if (!response.ok) {
    const err: ApiError = { status: response.status, error: 'Unable to authenticate' };
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
      credentials: 'include'
    }
  });

  return handleServerResponse(response);
}

async function handleServerResponse(response: Response) {
  if (!response.ok) {
    if (response.status === 401) {
      useUserStore.getState().setUserToken(null);
    }

    let error = await response.json();

    const err: ApiError = { status: response.status, error };
    console.error(err);
    throw err;
  }

  return response;
}
