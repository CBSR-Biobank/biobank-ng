import { Login } from '@app/components/login';
import { HomePage } from '@app/pages/homepage';

import { Link, RouterProvider, createBrowserRouter, useRouteError } from 'react-router-dom';

import { ShowError } from './components/show-error';
import { BasicPage } from './pages/basic-page';

const router = createBrowserRouter([
  {
    path: '/',
    element: <BasicPage />,
    errorElement: <ErrorBoundary />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: 'logging',
        children: [
          {
            index: true,
            async lazy() {
              let { LoggingPage } = await import('@app/pages/logging/logging-page');
              return { Component: LoggingPage };
            },
          },
        ],
      },
      {
        path: 'patients',
        children: [
          {
            index: true,
            async lazy() {
              let { PatientSelect } = await import('@app/pages/patients/patient-select');
              return { Component: PatientSelect };
            },
          },
          {
            path: 'add',
            async lazy() {
              let { PatientAddPage } = await import('@app/pages/patients/patient-add');
              return { Component: PatientAddPage };
            },
          },
          {
            path: ':pnumber',
            async lazy() {
              let { PatientPage } = await import('@app/pages/patients/patient-page');
              return { Component: PatientPage };
            },
            children: [
              {
                index: true,
                async lazy() {
                  let { PatientDetails } = await import('@app/components/patients/patient-details');
                  return { Component: PatientDetails };
                },
              },
              {
                path: 'not-exist',
                async lazy() {
                  let { PatientNotExist } = await import('@app/components/patients/patient-not-exist');
                  return { Component: PatientNotExist };
                },
              },
              {
                path: 'no-privileges',
                async lazy() {
                  let { NoPrivileges } = await import('@app/components/patients/no-privileges');
                  return { Component: NoPrivileges };
                },
              },
              {
                path: ':vnumber',
                children: [
                  {
                    index: true,
                    async lazy() {
                      let { CollectionEventView } = await import('@app/pages/collection-events/collection-event-view');
                      return { Component: CollectionEventView };
                    },
                  },
                  {
                    path: 'not-exist',
                    async lazy() {
                      let { VisitNotExist } = await import('@app/components/patients/visit-not-exist');
                      return { Component: VisitNotExist };
                    },
                  },
                  {
                    path: 'delete-no-privileges',
                    async lazy() {
                      let { VisitDeleteNoPrivileges } = await import(
                        '@app/components/patients/visit-delete-no-privileges'
                      );
                      return { Component: VisitDeleteNoPrivileges };
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
      {
        path: 'profile',
        async lazy() {
          let { UserProfile } = await import('@app/pages/user/user-profile');
          return { Component: UserProfile };
        },
        children: [
          {
            path: '',
            async lazy() {
              let { UserDetails } = await import('@app/components/users/user-details');
              return { Component: UserDetails };
            },
          },
          {
            path: 'studies',
            async lazy() {
              let { UserStudies } = await import('@app/components/users/user-studies');
              return { Component: UserStudies };
            },
          },
        ],
      },
      {
        path: '*',
        element: <NoMatch />,
      },
    ],
  },
  {
    path: 'login',
    element: <Login />,
  },
  {
    path: '*',
    element: <NoMatch />,
    errorElement: <ErrorBoundary />,
  },
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

function ErrorBoundary() {
  let error = useRouteError();
  console.error(error);
  return (
    <div className="container pt-8">
      <ShowError error={error}></ShowError>
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
