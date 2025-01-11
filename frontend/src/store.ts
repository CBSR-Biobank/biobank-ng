import { SortingState } from '@tanstack/react-table';
import { create, StateCreator } from 'zustand';
import { CollectionEvent } from './models/collection-event';
import { Patient } from './models/patient';
import { User } from './models/user';

const DRAWER_OPEN_KEY = 'biobank-drawer-open';

const DRAWER_MENU_OPEN_KEY = 'biobank-drawer-menus-open';

const LOGGED_IN_TOKEN_KEY = 'biobank-logged-in-token';

interface PaginationSlice {
  page: number;
  pageSize?: number;
  searchTerm?: string;
  sorting: SortingState;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
  setSearchTerm: (searchTerm: string) => void;
  setSorting: (sorting: SortingState) => void;
}

function getDrawerOpen(): boolean {
  return localStorage.getItem(DRAWER_OPEN_KEY) === 'true';
}

function getDrawerMenuOpen(title: string): boolean {
  return (localStorage.getItem(DRAWER_MENU_OPEN_KEY) ?? '').includes(title);
}

const createPaginationSlice: StateCreator<PaginationSlice, [], [], PaginationSlice> = (set) => ({
  page: 1,
  pageSize: undefined,
  searchTerm: undefined,
  sorting: [],
  setPage: (page: number) => set((_state) => ({ page })),
  setPageSize: (pageSize: number) => set((_state) => ({ pageSize })),
  setSearchTerm: (searchTerm: string) => set((_state) => ({ searchTerm })),
  setSorting: (sorting: SortingState) => set((_state) => ({ sorting })),
});

interface UserState {
  checkingAuth: boolean;
  userToken: string | null;
  user?: User;
  loggedIn: boolean;
  isGlobalAdmin: boolean;
  isDrawerOpen: boolean;
  isDrawerMenuOpen: (title: string) => boolean;
  setCheckingAuth: (checking: boolean) => void;
  setLoggedIn: (loggedIn: boolean) => void;
  setUserToken: (token: string | null) => void;
  setUser: (user: User) => void;
  setDrawerOpen: (open: boolean) => void;
  setDrawerMenuOpen: (title: string, open: boolean) => void;
}

function getUserToken(): string | null {
  return localStorage.getItem(LOGGED_IN_TOKEN_KEY);
}

export const useUserStore = create<UserState>((set) => ({
  checkingAuth: true,
  userToken: getUserToken(),
  user: undefined,
  loggedIn: getUserToken() !== null,
  isGlobalAdmin: false,
  isDrawerOpen: getDrawerOpen(),
  isDrawerMenuOpen: (title: string) => getDrawerMenuOpen(title),
  setCheckingAuth: (checking) => set((state) => ({ ...state, checkingAuth: checking })),
  setLoggedIn: (loggedIn) => set((state) => ({ ...state, loggedIn, username: undefined })),
  setUserToken: (token) => {
    if (token) {
      localStorage.setItem(LOGGED_IN_TOKEN_KEY, token);
    } else {
      localStorage.removeItem(LOGGED_IN_TOKEN_KEY);
    }
    set((state) => ({ ...state, userToken: token, username: undefined, loggedIn: token !== null }));
  },
  setUser: (user) => {
    set((state) => ({ ...state, user, isGlobalAdmin: user.isGlobalAdmin }));
  },
  setDrawerOpen: (open) => {
    if (open) {
      localStorage.setItem(DRAWER_OPEN_KEY, `${open}`);
    } else {
      localStorage.removeItem(DRAWER_OPEN_KEY);
    }
    set((state) => ({ ...state, isDrawerOpen: open }));
  },
  setDrawerMenuOpen: (title, open) => {
    const item = `:${title}`;
    let openMenus = localStorage.getItem(DRAWER_MENU_OPEN_KEY) ?? '';
    if (open && !openMenus.includes(item)) {
      openMenus += item;
    } else if (openMenus.includes(item)) {
      openMenus = openMenus.replace(item, '');
    }
    localStorage.setItem(DRAWER_MENU_OPEN_KEY, openMenus);
  },
}));

interface PatientState {
  patient?: Patient;
  collectionEvent?: CollectionEvent;
  setPatient: (patient?: Patient) => void;
  setCollectionEvent: (collectionEvent?: CollectionEvent) => void;
}

export const usePatientStore = create<PatientState>((set) => ({
  patient: undefined,
  collectionEvent: undefined,
  setPatient: (patient) => set((state) => ({ ...state, patient, collectionEvent: undefined })),
  setCollectionEvent: (collectionEvent) => set((state) => ({ ...state, collectionEvent })),
}));

interface StudyState {
  catalogueUrl: string | null;
  setCatalogueUrl: (url: string | null) => void;
}

export const useStudyStore = create<StudyState>((set) => ({
  catalogueUrl: null,
  setCatalogueUrl: (catalogueUrl) => set((state) => ({ ...state, catalogueUrl })),
}));

interface AppLoggingsState extends PaginationSlice {}

export const useAppLoggingsStore = create<AppLoggingsState>((...a) => ({
  ...createPaginationSlice(...a),
  pageSize: 20,
  sorting: [{ id: 'createdAt', desc: true }],
}));
