import { Login } from '@app/components/login';
import { HomePage } from '@app/pages/homepage';
import { Link, RouterProvider, createBrowserRouter } from 'react-router-dom';
import { BasicPage } from './pages/basic-page';

const router = createBrowserRouter([
  {
    path: '/',
    element: <BasicPage />,
    children: [
      {
        index: true,
        element: <HomePage />
      },
      {
        path: 'logging',
        children: [
          {
            index: true,
            async lazy() {
              let { LoggingPage } = await import('@app/pages/logging/logging-page');
              return { Component: LoggingPage };
            }
          }
        ]
      },
      {
        path: 'patients',
        children: [
          {
            index: true,
            async lazy() {
              let { PatientSelect } = await import('@app/pages/patients/patient-select');
              return { Component: PatientSelect };
            }
          },
          {
            path: 'add',
            async lazy() {
              let { PatientAdd } = await import('@app/pages/patients/patient-add');
              return { Component: PatientAdd };
            }
          },
          {
            path: ':pnumber',
            async lazy() {
              let { PatientPage: PatientPage } = await import('@app/pages/patients/patient-page');
              return { Component: PatientPage };
            },
            children: [
              {
                index: true,
                async lazy() {
                  let { PatientDetails } = await import('@app/components/patients/patient-details');
                  return { Component: PatientDetails };
                }
              },
              {
                path: ':vnumber',
                async lazy() {
                  let { CollectionEventView } = await import('@app/pages/collection-events/collection-event-view');
                  return { Component: CollectionEventView };
                }
              }
            ]
          }
        ]
      },
      {
        path: '*',
        element: <NoMatch />
      }
    ]
  },
  {
    path: 'login',
    element: <Login />
  },
  {
    path: '*',
    element: <NoMatch />
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
