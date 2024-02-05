export interface PropertyOption<T> {
  id: T;
  label: string;
}

export type MutatorProps<T> = {
  label: string;
  required: boolean;
  value?: T;
  onClose: (value?: T) => void;
  propertyOptions?: PropertyOption<T>[];
};
