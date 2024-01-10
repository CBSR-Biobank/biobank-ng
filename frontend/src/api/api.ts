import { userSchema } from '@app/models/user';
import { useUserStore } from '@app/store';

// For "Endpoint" type, see
// https://medium.com/flock-community/contract-first-strictly-typed-endpoints-in-typescript-with-runtime-validation-c409b2603287

type Auth = {
  method: 'GET';
  path: ['auth'];
  query: undefined;
};

type Logging = {
  method: 'GET';
  path: ['logging'];
  query: undefined;
};

type LoggingLatest = {
  method: 'GET';
  path: ['logging', 'latest'];
  query: undefined;
};

type PatientGet = {
  method: 'GET';
  path: ['patients', string];
  query: undefined;
};

type CollectionEventsGet = {
  method: 'GET';
  path: ['patients', string, 'collection-events'];
  query: undefined;
};

type CollectionEventGet = {
  method: 'GET';
  path: ['patients', string, 'collection-events', number];
  query: undefined;
};

type AliquotsGet = {
  method: 'GET';
  path: ['specimens', 'aliquots', string];
  query: undefined;
};

export type Endpoint =
  | Auth
  | Logging
  | LoggingLatest
  | PatientGet
  | CollectionEventsGet
  | CollectionEventGet
  | AliquotsGet;

export type ApiError = {
  status: number;
  error: any;
};

export async function httpClient(endpoint: Endpoint) {
  const method = endpoint.method;
  const path = endpoint.path.join('/');
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

  const response = await fetch(url, { method, headers });
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
