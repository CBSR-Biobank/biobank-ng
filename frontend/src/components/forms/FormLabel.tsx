import { cn } from '@app/utils';
import React from 'react';
import { Label } from '../ui/label';

const labelClasses = 'text-sm font-semibold text-gray-500';

export const FormLabel: React.FC<React.PropsWithChildren<{ className?: string }>> = ({ className, children }) => {
  return <Label className={cn(labelClasses, className)}>{children}</Label>;
};
