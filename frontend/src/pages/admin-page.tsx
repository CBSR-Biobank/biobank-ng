import { cn } from '@app/utils';

import { VariantProps, cva } from 'class-variance-authority';

const variants = cva('mb-8 grid grid-cols-1 gap-4 rounded-md bg-gray-100 p-4 drop-shadow-md', {
  variants: {
    variant: {
      default: 'border-0',
      borders: 'border-2 border-slate-300/75',
    },
  },
  defaultVariants: {
    variant: 'default',
  },
});

export interface AdminPageProps extends React.HTMLAttributes<HTMLDivElement>, VariantProps<typeof variants> {}

export function AdminPage({ variant, className, children }: AdminPageProps) {
  return <div className={cn(variants({ variant, className }))}>{children}</div>;
}

function Title({
  className,
  hasBorder = false,
  children,
}: React.PropsWithChildren<{ className?: string; hasBorder?: boolean }>) {
  return (
    <div className={cn('py-6 text-4xl', className, { 'border-b-2 border-slate-300/75': hasBorder })}>{children}</div>
  );
}

Title.displayName = 'Title';
AdminPage.displayName = 'AdminPage';
AdminPage.Title = Title;
