import { faBan } from '@fortawesome/free-solid-svg-icons';
import React from 'react';

import { BbButton } from './bb-button';

export const CancelButton: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>> = (props) => (
  <BbButton variant="secondary" trailingIcon={faBan} {...props}>
    Cancel
  </BbButton>
);
