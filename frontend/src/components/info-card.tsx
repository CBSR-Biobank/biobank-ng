import { Alert, AlertDescription, AlertTitle } from '@app/components/ui/alert';
import { cn } from '@app/utils';

import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faCircleInfo } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { forwardRef } from 'react';

export const InfoCard = forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement> & {
    variant: 'default' | 'info' | 'destructive' | 'warning';
    title: string;
    message: string;
    icon?: IconProp;
  }
>(({ className, title, message, icon, variant = 'info' }, ref) => {
  return (
    <div ref={ref} className={cn('grid grid-cols-1 gap-8', className)}>
      <Alert variant={variant}>
        <FontAwesomeIcon icon={icon ?? faCircleInfo} />
        <AlertTitle>{title}</AlertTitle>
        <AlertDescription className="text-sm">{message}</AlertDescription>
      </Alert>
    </div>
  );
});
