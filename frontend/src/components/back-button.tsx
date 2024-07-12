import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import { BbButton } from './bb-button';

export const BackButton = React.forwardRef<HTMLButtonElement, React.ButtonHTMLAttributes<HTMLButtonElement>>(
  ({ className, ...props }, _ref) => (
    <BbButton intent="secondary" {...props} size="xl" leadingIcon={faChevronLeft}>
      Back
    </BbButton>
  )
);
