import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import { BbButton } from './bb-button';

export const BackButton: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>> = (props) => (
  <BbButton variant="secondary" {...props} size="xl" leadingIcon={faChevronLeft}>
    Back
  </BbButton>
);
