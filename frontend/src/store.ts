import { create } from 'zustand';
import { User, userHasRole } from './models/user';
import { UserRole } from './models/user-role';

const LOCAL_SESSION_KEY = "biobank-logged-in";

interface UserState {
  checkingAuth: boolean;
  loggedIn: boolean;
  user?: User;
  isSuperuser: boolean;
  setCheckingAuth: (checking: boolean) => void;
  setLoggedIn: (loggedIn: boolean) => void;
  setUser: (user: User) => void;
}

function getInitialLoggedIn() {
  return localStorage.getItem(LOCAL_SESSION_KEY) == "true" || false;
};

export const useUserStore = create<UserState>((set) => ({
  checkingAuth: true,
  loggedIn: getInitialLoggedIn(),
  username: '',
  isSuperuser: false,
  setCheckingAuth: (checking) => set((state) => ({ ...state, checkingAuth: checking })),
  setLoggedIn: (loggedIn) => {
    localStorage.setItem(LOCAL_SESSION_KEY, loggedIn ? "true" : "false"),
    set((state) => ({ ...state, loggedIn, username: undefined }));
  },
  setUser: (user) => set((state) => ({ ...state, user, isSuperuser: userHasRole(user, UserRole.SUPERUSER) }))
}));
