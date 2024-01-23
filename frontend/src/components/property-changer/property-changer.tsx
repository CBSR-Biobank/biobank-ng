import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { PropertyChangerDate } from './property-changer-date';
import { PropertyChangerDateRange } from './property-changer-date-range';
import { PropertyChangerEmail } from './property-changer-email';
import { PropertyChangerNumber } from './property-changer-number';
import { PropertyChangerRadio } from './property-changer-radio';
import { PropertyChangerSelect } from './property-changer-select';
import { PropertyChangerText } from './property-changer-text';
import { PropertyChangerUrl } from './property-changer-url';

export enum PropertyTypes {
  AUTO_COMPLETE = 'autocomplete,',
  COMBO_BOX = 'combo-box',
  DATE = 'date',
  DATE_RANGE = 'date-range',
  EMAIL = 'email',
  NUMBER = 'number',
  RADIO = 'radio',
  TEXT = 'text',
  URL = 'website'
}

export interface PropertySchema<T> {
  propertyName: string;
  propertyType?: PropertyTypes;
  label: string;
  value?: T | null;
  displayValue?: React.ReactElement | string | null;
  icon?: IconProp;
  tooltip?: string;
  required: boolean;
  handleChange?: (propertyName: string) => void;

  propertyOptions?: PropertyOption<T>[];
  propertyChangerOverride?: (props: PropertyChangerProps<T>) => JSX.Element;
  multiline?: boolean;
  minDate?: Date;
  maxDate?: Date;
}

export type PropertiesSchema = Record<string, PropertySchema<unknown>>;

export interface DateRange {
  startDate: Date;
  endDate: Date;
}

export interface PersonNames {
  givenNames: string;
  familyNames: string;
}

export interface PropertyOption<T> {
  id: T;
  label: string;
}

export type PropertyChangerResult = 'ok' | 'cancel';

export interface PropertyChangerProps<T> {
  propertyName: string;
  propertyType?: PropertyTypes;
  title: string;
  label: string;
  open: boolean;
  required: boolean;
  value?: T;
  onClose: (result: PropertyChangerResult, propertyName: string, value: T | null) => void;
  propertyOptions?: PropertyOption<T>[];
}

export interface PropertyChangerDateRangeProps extends PropertyChangerProps<DateRange> {}

export interface PropertyChangerDateProps extends PropertyChangerProps<Date> {
  minDate?: Date;
  maxDate?: Date;
}

export interface PropertyChangerNumberProps extends PropertyChangerProps<number> {
  min?: number;
  max?: number;
}

export interface PropertyChangerTextareaProps extends PropertyChangerProps<string> {
  multiline: boolean;
  maxlen?: number;
}

export const PropertyChanger: React.FC<PropertyChangerProps<unknown>> = ({ propertyType, ...props }) => {
  switch (propertyType) {
    case PropertyTypes.DATE_RANGE: {
      const dateRangeProps = props as PropertyChangerDateRangeProps;
      return <PropertyChangerDateRange {...dateRangeProps} />;
    }

    case PropertyTypes.DATE: {
      const dateProps = props as PropertyChangerDateProps;
      return <PropertyChangerDate {...dateProps} />;
    }

    case PropertyTypes.EMAIL: {
      const emailProps = props as unknown as PropertyChangerProps<string>;
      return <PropertyChangerEmail {...emailProps} />;
    }

    case PropertyTypes.NUMBER: {
      const numberProps = props as unknown as PropertyChangerNumberProps;
      return <PropertyChangerNumber {...numberProps} />;
    }

    case PropertyTypes.TEXT: {
      const textProps = props as unknown as PropertyChangerTextareaProps;
      return <PropertyChangerText {...textProps} />;
    }

    case PropertyTypes.RADIO:
      return <PropertyChangerRadio {...props} />;

    case PropertyTypes.COMBO_BOX:
      return <PropertyChangerSelect {...props} />;

    case PropertyTypes.URL: {
      const urlProps = props as unknown as PropertyChangerProps<string>;
      return <PropertyChangerUrl {...urlProps} />;
    }
  }

  throw new Error('invalid property type: ' + propertyType);
};
