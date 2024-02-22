import { faBan } from '@fortawesome/free-solid-svg-icons';
import React from 'react';

import { Button } from './ui/button';

export const CancelButton = React.forwardRef<HTMLButtonElement, React.ButtonHTMLAttributes<HTMLButtonElement>>(
  ({ className, ...props }, ref) => (
    <Button ref={ref} variant="secondary" icon={faBan} {...props}>
      Cancel
    </Button>
  )
);
