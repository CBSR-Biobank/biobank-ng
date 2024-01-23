import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import { Button } from './ui/button';

export const BackButton = React.forwardRef<HTMLButtonElement, React.ButtonHTMLAttributes<HTMLButtonElement>>(
  ({ className, ...props }, ref) => (
    <Button ref={ref} variant="default" {...props} icon={faChevronLeft}>
      Back
    </Button>
  )
);
