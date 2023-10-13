import { create } from 'zustand';
import { User, userHasRole } from './models/user';
import { UserRole } from './models/user-role';

interface UserState {
  checkingAuth: boolean;
  loggedIn: boolean;
  user?: User;
  isSuperuser: boolean;
  setCheckingAuth: (checking: boolean) => void;
  setLoggedIn: (loggedIn: boolean) => void;
  setUser: (user: User) => void;
}

export const useUserStore = create<UserState>((set) => ({
  checkingAuth: true,
  loggedIn: false,
  username: '',
  isSuperuser: false,
  setCheckingAuth: (checking) => set((state) => ({ ...state, checkingAuth: checking })),
  setLoggedIn: (loggedIn) => set((state) => ({ ...state, loggedIn, username: undefined })),
  setUser: (user) => set((state) => ({ ...state, user, isSuperuser: userHasRole(user, UserRole.SUPERUSER) }))
}));
