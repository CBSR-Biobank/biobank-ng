import { IconButton } from '@app/components/bb-button';
import { Card, CardDescription, CardFooter, CardHeader, CardTitle } from '@app/components/ui/card';
import { useUserStore } from '@app/store';
import { faPlus } from '@fortawesome/free-solid-svg-icons';

import { Link } from 'react-router-dom';

export function HomePage() {
  const { loggedIn } = useUserStore();

  return (
    <>
      <div className="grid grid-cols-1 place-content-stretch gap-4">
        <p className="pt-8 text-center text-4xl font-semibold text-sky-600">CBSR Biobank</p>
      </div>
      {loggedIn && (
        <div className="container mx-auto grid grid-cols-1 place-content-stretch gap-4 pt-10 md:grid-cols-2">
          <Link to={'/patients'}>
            <Card>
              <CardHeader>
                <CardTitle className="text-sky-600">Patient Visits</CardTitle>
                <CardDescription>View patient visits, or Record a visit for a patient.</CardDescription>
              </CardHeader>
              <CardFooter>
                <div className="w-full">
                  <IconButton variant="soft" icon={faPlus} hiddenLabel="add / view patient visit" />
                </div>
              </CardFooter>
            </Card>
          </Link>
          <Link to={'/patients/add'}>
            <Card>
              <CardHeader>
                <CardTitle className="text-sky-600">Add Patient</CardTitle>
                <CardDescription>Add a new patient to a study.</CardDescription>
              </CardHeader>
              <CardFooter>
                <div className="w-full">
                  <IconButton variant="soft" icon={faPlus} hiddenLabel="add patient" />
                </div>
              </CardFooter>
            </Card>
          </Link>
        </div>
      )}
    </>
  );
}
