import React from 'react';

import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { BbButton } from './bb-button';

export const OkButton: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>> = (props) => (
  <BbButton variant="primary" leadingIcon={faCheck} {...props}>
    OK
  </BbButton>
);
