import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { MutatorDate } from './mutator-date';
import { MutatorDateRange } from './mutator-date-range';
import { MutatorEmail } from './mutator-email';
import { MutatorNumber } from './mutator-number';
import { MutatorRadio } from './mutator-radio';
import { MutatorSelect } from './mutator-select';
import { MutatorText } from './mutator-text';
import { MutatorUrl } from './mutator-url';

type MutatorType =
  | 'autocomplete,'
  | 'combo-box'
  | 'date'
  | 'date-range'
  | 'email'
  | 'number'
  | 'radio'
  | 'text'
  | 'url';

export type MutatorSchema<T> = {
  propertyName: string;
  propertyType?: MutatorType;
  label: string;
  value?: T | null;
  displayValue?: React.ReactElement | string | null;
  icon?: IconProp;
  tooltip?: string;
  required: boolean;
  handleChange?: (propertyName: string) => void;

  propertyOptions?: PropertyOption<T>[];
  mutatorOverride?: (props: MutatorProps<T>) => JSX.Element;
};

//export type PropertiesSchema = Record<string, MutatorSchema<unknown>>;

export interface PropertyOption<T> {
  id: T;
  label: string;
}

export type MutatorResult = 'ok' | 'cancel';

export type MutatorProps<T> = {
  propertyName: string;
  propertyType?: MutatorType;
  title: string;
  label: string;
  open: boolean;
  required: boolean;
  value?: T;
  onClose: (result: MutatorResult, propertyName: string, value: T | null) => void;
  propertyOptions?: PropertyOption<T>[];
};

export type DateRange = {
  startDate: Date;
  endDate: Date;
};

export type MutatorDateRangeProps = MutatorProps<DateRange>;

export type MutatorDateProps = MutatorProps<Date> & {
  minDate?: Date;
  maxDate?: Date;
};

export type MutatorNumberProps = MutatorProps<number> & {
  min?: number;
  max?: number;
};

export type MutatorTextareaProps = MutatorProps<string> & {
  multiline: boolean;
  maxlen?: number;
};

export const Mutator: React.FC<MutatorProps<unknown>> = ({ propertyType, ...props }) => {
  switch (propertyType) {
    case 'date-range': {
      const dateRangeProps = props as MutatorDateRangeProps;
      return <MutatorDateRange {...dateRangeProps} />;
    }

    case 'date': {
      const dateProps = props as MutatorDateProps;
      return <MutatorDate {...dateProps} />;
    }

    case 'email': {
      const emailProps = props as unknown as MutatorProps<string>;
      return <MutatorEmail {...emailProps} />;
    }

    case 'number': {
      const numberProps = props as unknown as MutatorNumberProps;
      return <MutatorNumber {...numberProps} />;
    }

    case 'text': {
      const textProps = props as unknown as MutatorTextareaProps;
      return <MutatorText {...textProps} />;
    }

    case 'radio':
      return <MutatorRadio {...props} />;

    case 'combo-box':
      return <MutatorSelect {...props} />;

    case 'url': {
      const urlProps = props as unknown as MutatorProps<string>;
      return <MutatorUrl {...urlProps} />;
    }
  }

  throw new Error('invalid property type: ' + propertyType);
};
