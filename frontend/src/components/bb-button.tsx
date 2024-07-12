import { RequiredVariantProps } from '@app/types';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { cva } from 'class-variance-authority';
import React from 'react';

// idea from here:
// https://github.com/brookslybrand/cva-component-demo

const buttonVariants = cva('font-semibold shadow-sm cursor-pointer', {
  variants: {
    intent: {
      primary:
        'bg-sky-500 text-white hover:bg-sky-600 active:bg-sky-700 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-sky-400 disabled:bg-blue-300',
      secondary:
        'bg-slate-200 text-gray-900 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 active:bg-gray-100 disabled:bg-gray-200 disabled:text-white',
      soft: 'bg-gray-100 text-slate-700 hover:bg-gray-300 active:bg-gray-300 border-2 border-gray-400 disabled:bg-gray-300 disabled:text-white',
      mutator: 'bg-gray-300/50 text-sky-500 border-r hover:bg-gray-300 p-0',
    },
    size: {
      xs: 'text-xs',
      sm: 'text-sm',
      md: 'text-sm',
      lg: 'text-sm',
      xl: 'text-sm',
    },
    rounded: {
      normal: '',
      full: 'rounded-full',
    },
    _content: {
      text: '',
      textAndIcon: 'inline-flex items-center',
      icon: '',
    },
  },
  compoundVariants: [
    { intent: 'mutator', className: 'rounded-none' },
    { size: ['xs', 'sm'], rounded: 'normal', className: 'rounded' },
    { size: ['md', 'lg', 'xl'], rounded: 'normal', className: 'rounded-md' },
    {
      size: ['xs', 'sm'],
      _content: ['text', 'textAndIcon'],
      className: 'gap-x-1.5 px-2 py-1',
    },
    {
      size: ['md', 'lg', 'xl'],
      _content: ['text', 'textAndIcon'],
      className: 'gap-x-2',
    },
    {
      size: 'md',
      _content: ['text', 'textAndIcon'],
      className: 'px-2.5 py-1.5',
    },
    {
      size: 'lg',
      _content: ['text', 'textAndIcon'],
      className: 'px-3 py-2',
    },
    {
      size: 'xl',
      _content: ['text', 'textAndIcon'],
      className: 'px-3.5 py-2.5',
    },
    { size: 'xs', _content: 'icon', className: 'p-0.5' },
    { size: 'sm', _content: 'icon', className: 'p-1' },
    { size: 'md', _content: 'icon', className: 'p-1.5' },
    { size: 'lg', _content: 'icon', className: 'p-2' },
    { size: 'xl', _content: 'icon', className: 'p-2.5' },
  ],
  defaultVariants: {
    intent: 'primary',
    size: 'md',
    rounded: 'normal',
    _content: 'text',
  },
});

export type ButtonVariants = Omit<RequiredVariantProps<typeof buttonVariants>, '_content'>;

type ButtonProps = Partial<ButtonVariants> &
  React.ButtonHTMLAttributes<HTMLButtonElement> &
  ({ leadingIcon?: IconDefinition; trailingIcon?: never } | { leadingIcon?: never; trailingIcon?: IconDefinition });

type IconButtonProps = Omit<ButtonProps, 'rounded' | 'leadingIcon' | 'trailingIcon' | 'children'> & {
  hiddenLabel: string;
  icon: IconDefinition;
};

export function BbButton({
  intent,
  size,
  rounded,
  className,
  children,
  leadingIcon,
  trailingIcon,
  ...props
}: ButtonProps) {
  return (
    <button className={buttonVariants({ className, intent, size, rounded })} {...props}>
      {leadingIcon ? <FontAwesomeIcon icon={leadingIcon} className="mr-1 w-5" aria-hidden="true" /> : null}
      {children}
      {trailingIcon ? <FontAwesomeIcon icon={trailingIcon} className="ml-1 w-5" aria-hidden="true" /> : null}
    </button>
  );
}

export function IconButton({ icon, intent, size, hiddenLabel, className, ...props }: IconButtonProps) {
  return (
    <button
      className={buttonVariants({
        className,
        intent,
        rounded: 'full',
        size,
        _content: 'icon',
      })}
      {...props}
    >
      <p className="sr-only">{hiddenLabel}</p>
      <FontAwesomeIcon icon={icon} size="xl" className="w-5" aria-hidden="true" />
    </button>
  );
}
