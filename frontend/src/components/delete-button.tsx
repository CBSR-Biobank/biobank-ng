import React from 'react';

import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { BbButton } from './bb-button';

export const DeleteButton: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>> = (props) => (
  <BbButton variant="destructive" trailingIcon={faTrash} {...props}>
    Delete
  </BbButton>
);
