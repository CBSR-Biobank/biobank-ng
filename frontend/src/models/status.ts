export enum Status {
  NONE = 'None',
  ACTIVE = 'Active',
  CLOSED = 'Closed',
  FLAGGED = 'Flagged',
}

export const StatusLabels: Record<Status, string> = {
  None: 'None',
  Active: 'Active',
  Closed: 'Closed',
  Flagged: 'Flagged',
};
