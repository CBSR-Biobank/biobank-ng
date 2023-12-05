import { userSchema } from '@app/models/user';
import { useUserStore } from '@app/store';

type Route = Record<string, string>;

export const API_ROUTES: Readonly<Record<string, Route>> = {
  auth: {
    index: '/api/auth',
    login: '/api/login', // FIXME: still needed?
    token: '/api/token'
  },
  patients: {
    index: '/api/patients',
    pnumber: '/api/patients/:pnumber',
    'collection-events': '/api/patients/:pnumber/collection-events',
    'collection-event': '/api/patients/:pnumber/collection-events/:vnumber'
  }
} as const;

export type ApiError = {
  status: number;
  error: any;
};

export async function fetchAuthenticated() {
  const response = await fetch(API_ROUTES.auth.index, {
    headers: {
      Authorization: 'Bearer ' + useUserStore.getState().userToken,
      credentials: 'include',
      'Content-Type': 'application/json'
    }
  });

  if (!response.ok) {
    const err: ApiError = { status: response.status, error: 'Unable to authenticate' };
    throw err;
  }

  const json = await response.json();
  return json;
}

export async function login(username: string, password: string) {
  const response = await fetch(API_ROUTES.auth.token, {
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

export async function fetchApi(route: string, init?: RequestInit) {
  const response = await fetch(route, {
    headers: {
      Authorization: 'Bearer ' + useUserStore.getState().userToken,
      credentials: 'include',
      'Content-Type': 'application/json'
    },
    ...init
  });

  return handleServerResponse(response);
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

export function paginationToQueryParams(page?: number, searchTerm?: string): string {
  const params = new URLSearchParams();

  if (page && page > 1) {
    params.append('page', `${page}`);
  }

  if (searchTerm && searchTerm.length > 0) {
    params.append('search', searchTerm);
  }

  let paramsAsString = params.toString();
  if (paramsAsString.length <= 0) {
    return '';
  }
  return '?' + paramsAsString;
}

export function routeReplace(baseRoute: string, replacements: Record<string, string>): string {
  const result = baseRoute.replace(/\/(:\w+)\//gi, (_match, p1) => {
    const subst = replacements[p1] || p1;
    return `/${subst}/`;
  });
  return result;
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
