import { cn } from '@app/utils';

import React from 'react';

import { Input } from '../ui/input';
import { FormLabel } from './form-label';

export const labelClasses = cn('text-sm font-semibold text-gray-500');

interface LabelledInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  isDirty?: boolean;
  isTouched?: boolean;
  errorMessage?: string;
}

export const LabelledInput = React.forwardRef<HTMLInputElement, LabelledInputProps>(
  ({ label, type = 'text', isDirty, isTouched, errorMessage, ...props }, ref) => {
    return (
      <div className="grid grid-cols-1 gap-3">
        <FormLabel className={labelClasses}>{label}</FormLabel>
        <Input
          ref={ref}
          type={type}
          className="border-primary-400 bg-basic-300 placeholder:text-basic-500 focus-visible:ring-primary-400"
          {...props}
        />
        {errorMessage && (
          <div className="text-xs font-medium text-red-600">
            <span role="alert">{errorMessage}</span>
          </div>
        )}
      </div>
    );
  }
);
