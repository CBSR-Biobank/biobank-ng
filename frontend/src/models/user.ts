import { z } from 'zod';

import { UserGroup } from './user-group';

export const groupSchema = z.object({
  groupId: z.number(),
  name: z.string(),
});

export const userSchema = z.object({
  userId: z.number(),
  fullname: z.string(),
  username: z.string(),
  isGlobalAdmin: z.boolean(),
  apiKey: z.string().nullable(),
  groups: z.array(groupSchema),
});

export type Group = z.infer<typeof groupSchema>;
export type User = z.infer<typeof userSchema>;

export function userHasGroup(user: User, group: string): boolean {
  return (user?.groups ?? []).find((g) => g.name.includes(group)) !== undefined;
}

export function userHasGroups(user: User, groups: UserGroup[]): boolean {
  const groupNames = groups.map((g) => g.toString());
  const userGroups = (user?.groups ?? []).map((g) => g.name);
  const intersect = (arr1: string[], arr2: string[]) => arr1.filter((a1) => arr2.includes(a1));
  return groups && intersect(groupNames, userGroups).length > 0;
}
