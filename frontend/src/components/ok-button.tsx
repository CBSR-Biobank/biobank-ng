import { faCheck } from '@fortawesome/free-solid-svg-icons';
import React from 'react';

import { Button } from './ui/button';

export const OkButton = React.forwardRef<HTMLButtonElement, React.ButtonHTMLAttributes<HTMLButtonElement>>(
  ({ className, ...props }, ref) => (
    <Button ref={ref} variant="default" icon={faCheck} {...props}>
      OK
    </Button>
  )
);
