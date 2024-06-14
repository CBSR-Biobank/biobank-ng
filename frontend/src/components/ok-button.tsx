import React from 'react';

import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { BbButton } from './bb-button';

export const OkButton = React.forwardRef<HTMLButtonElement, React.ButtonHTMLAttributes<HTMLButtonElement>>(
  ({ className, ...props }, _ref) => (
    <BbButton intent="primary" leadingIcon={faCheck} {...props} className={className}>
      OK
    </BbButton>
  )
);
