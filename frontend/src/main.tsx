import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import ReactDOM from 'react-dom/client';

import { App } from './App.tsx';
import './index.css';

const onError = (error: Error) => {
  if ('status' in error && error.status === 401) {
    window.location.replace('/login');
  }
  return false;
};

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      throwOnError: onError,
    },
    mutations: {
      throwOnError: onError,
    },
  },
});

ReactDOM.createRoot(document.getElementById('root')!).render(
  <QueryClientProvider client={queryClient}>
    <React.StrictMode>
      <App />
    </React.StrictMode>
  </QueryClientProvider>
);
