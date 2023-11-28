import { cn } from '@app/utils';

export const ShowError: React.FC<{ message?: string; error?: any }> = ({ message, error }) => {
  return (
    <div role="alert">
      <div className={cn('rounded-t bg-red-500 px-4 py-2 font-bold text-white')}>Error</div>
      <div className={cn('rounded-b border border-t-0 border-red-400 bg-red-100 px-4 py-3 text-red-700')}>
        {message && <>{message}</>}
        {error && <pre>{JSON.stringify(error, null, 2)}</pre>}
      </div>
    </div>
  );
};
