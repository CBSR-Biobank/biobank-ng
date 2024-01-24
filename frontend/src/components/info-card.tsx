import { Alert, AlertDescription, AlertTitle } from '@app/components/alert';
import { cn } from '@app/utils';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faCircleInfo } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { forwardRef } from 'react';

export const InfoCard = forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement> & {
    title: String;
    message: String;
    icon?: IconProp;
  }
>(({ className, title, message, icon }, ref) => {
  return (
    <div ref={ref} className={cn('grid grid-cols-1 gap-8', className)}>
      <Alert className="border-sky-500 bg-sky-200 text-slate-500">
        <FontAwesomeIcon icon={icon ?? faCircleInfo} />
        <AlertTitle>{title}</AlertTitle>
        <AlertDescription className="text-sm text-muted-foreground">{message}</AlertDescription>
      </Alert>
    </div>
  );
});
