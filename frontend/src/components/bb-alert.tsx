import { cn } from '@app/utils';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Alert, AlertDescription, AlertTitle } from './ui/alert';

export const BbAlert: React.FC<
  React.HTMLAttributes<HTMLDivElement> & {
    variant: 'default' | 'info' | 'destructive' | 'warning';
    message?: string;
    title?: string;
    icon?: IconProp;
  }
> = ({ className, variant = 'info', message, title, icon, children }) => {
  const classes = cn(
    {
      'bg-background text-foreground': variant === 'default',
      'border-destructive/50 text-destructive dark:border-destructive [&>svg]:text-destructive':
        variant === 'destructive',
      'border-sky-600 text-sky-600 dark:border-sky-600 [&>svg]:text-sky-600': variant === 'info',
      'border-orange-500/50 text-orange-500 dark:border-orange-500 [&>svg]:text-orange-500': variant === 'warning',
    },
    className
  );

  return (
    <Alert className={classes}>
      {icon && <FontAwesomeIcon icon={icon} />}
      <AlertTitle>{title}</AlertTitle>
      {message && <AlertDescription>{message}</AlertDescription>}
      {children}
    </Alert>
  );
};
