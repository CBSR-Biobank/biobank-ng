import { cn } from '@app/utils';

const headingClasses =
  'mb-8 grid grid-cols-1 justify-between gap-4 rounded-md bg-gray-100 p-4 pb-20 drop-shadow-md sm:flex-row';

function Title({
  className,
  hasBorder = false,
  children
}: React.PropsWithChildren<{ className?: string; hasBorder?: boolean }>) {
  return (
    <div
      className={cn('py-6 text-4xl', className, {
        'border-b-2 border-slate-300/75': hasBorder
      })}
    >
      {children}
    </div>
  );
}

Title.displayName = 'Title';

/**
 * The page shown to the user when "Funders' is selected from the dashboard menu.
 */
export function AdminPage({ className, children }: React.PropsWithChildren<{ className?: string }>) {
  return <div className={cn(headingClasses, className)}>{children}</div>;
}

AdminPage.displayName = 'AdminPage';
AdminPage.Title = Title;
