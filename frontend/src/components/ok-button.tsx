import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import { BiobankButton } from './biobank-button';

export const OkButton = React.forwardRef<HTMLButtonElement, React.ButtonHTMLAttributes<HTMLButtonElement>>(
  ({ className, ...props }, ref) => (
    <BiobankButton ref={ref} variant="default" {...props}>
      <FontAwesomeIcon icon={faCheck} />
      OK
    </BiobankButton>
  )
);
