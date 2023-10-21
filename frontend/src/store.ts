import { create } from 'zustand';
import { User, userHasRole } from './models/user';
import { UserRole } from './models/user-role';

const LOGGED_IN_TOKEN_KEY = "biobank-logged-in-token";

interface UserState {
  checkingAuth: boolean;
  userToken: string | null;
  user?: User;
  loggedIn: boolean,
  isSuperuser: boolean;
  setCheckingAuth: (checking: boolean) => void;
  setUserToken: (token: string | null) => void;
  setUser: (user: User) => void;
}

function getUserToken(): string | null {
  return localStorage.getItem(LOGGED_IN_TOKEN_KEY);
};

export const useUserStore = create<UserState>((set) => ({
  checkingAuth: true,
  userToken: getUserToken(),
  user: undefined,
  loggedIn: getUserToken() !== null,
  isSuperuser: false,
  setCheckingAuth: (checking) => set((state) => ({ ...state, checkingAuth: checking })),
  setUserToken: (token) => {
    if (token) {
      localStorage.setItem(LOGGED_IN_TOKEN_KEY, token);
    } else {
      localStorage.removeItem(LOGGED_IN_TOKEN_KEY);
    }
    set((state) => ({ ...state, userToken: token, username: undefined, loggedIn: token !== null }));
  },
  setUser: (user) => set((state) => ({ ...state, user, isSuperuser: userHasRole(user, UserRole.SUPERUSER) }))
}));
