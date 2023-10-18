import { Login } from '@app/components/Login';
import { HomePage } from '@app/pages/HomePage';
import { Link, RouterProvider, createBrowserRouter } from 'react-router-dom';
import { DashboardLayout } from './components/DashboardLayout';

const router = createBrowserRouter([
  {
    path: '/',
    element: <DashboardLayout />,
    children: [
      {
        index: true,
        element: <HomePage />
      },
      {
        path: 'login',
        element: <Login />
      },
      {
        path: '*',
        element: <NoMatch />
      }
    ]
  }
]);

function NoMatch() {
  return (
    <div>
      <h2>Nothing to see here!</h2>
      <p>
        <Link to="/">Go to the home page</Link>
      </p>
    </div>
  );
}

export function App() {
  return (
    <div className="grid grid-cols-1 gap-2">
      <RouterProvider router={router} fallbackElement={<p>Loading...</p>} />
    </div>
  );
}
