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
  const classes = cn({
    'bg-background text-foreground': variant === 'default',
    'border-destructive/50 text-destructive dark:border-destructive [&>svg]:text-destructive':
      variant === 'destructive',
    'border-sky-600 text-sky-600 dark:border-sky-600 [&>svg]:text-sky-600': variant === 'info',
    'border-orange-500/50 text-orange-500 dark:border-orange-500 [&>svg]:text-orange-500': variant === 'warning',
  });

  return (
    <div ref={ref} className={cn('grid grid-cols-1 gap-8', className)}>
      <Alert className={classes}>
        <FontAwesomeIcon icon={icon ?? faCircleInfo} />
        <AlertTitle>{title}</AlertTitle>
        <AlertDescription className="text-sm">{message}</AlertDescription>
      </Alert>
    </div>
  );
});
