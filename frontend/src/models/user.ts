import { z } from 'zod';
import { UserRole } from './user-role';

export const userSchema = z.object({
  username: z.string(),
  loggedIn: z.boolean(),
  roles: z.array(z.nativeEnum(UserRole)).optional().nullable()
});

export type User = z.infer<typeof userSchema>;

export function userHasRole(user: User, role: UserRole): boolean {
  return (user?.roles ?? []).find((r) => r.includes(role)) !== undefined;
}

export function userHasRoles(user: User, roles: UserRole[]): boolean {
  const userRoles = user?.roles ?? [];
  const intersect = (arr1: UserRole[], arr2: UserRole[]) => arr1.filter((a1) => arr2.includes(a1));
  return roles && intersect(roles, userRoles).length > 0;
}
