export interface PropertyOption<T> {
  id: T;
  label: string;
}

export type MutatorProps<T> = {
  label: string;
  required?: boolean;
  value?: T | null;
  onClose: (value?: T | null) => void;
  propertyOptions?: PropertyOption<T>[];
};
