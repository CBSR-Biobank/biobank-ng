import { cn } from '@app/utils';
import { VariantProps, cva } from 'class-variance-authority';
import React from 'react';
import { Button } from './ui/button';

const buttonVariants = cva('flex w-20 items-center gap-1 bg-warning-500 font-semibold', {
  variants: {
    variant: {
      default: 'text-slate-600 bg-gray-300 hover:bg-gray-400',
      primary: 'text-slate-600 bg-gray-300 hover:bg-slate-400',
      secondary: 'text-slate-600 bg-gray-600 hover:bg-slate-500',
      warning: 'text-basic-200 bg-warning-600 hover:bg-orange-500'
    },
    size: {
      default: 'h-10 px-4 py-2',
      sm: 'h-9 rounded-md px-3',
      lg: 'h-11 rounded-md px-8',
      icon: 'h-10 w-10'
    }
  },
  defaultVariants: {
    variant: 'default',
    size: 'default'
  }
});

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

export const BiobankButton = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, ...props }, ref) => (
    <Button ref={ref} className={cn(buttonVariants({ variant, size, className }))} {...props} />
  )
);
