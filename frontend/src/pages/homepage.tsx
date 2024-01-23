import { Button } from '@app/components/ui/button';
import { Card, CardDescription, CardFooter, CardHeader, CardTitle } from '@app/components/ui/card';
import { useUserStore } from '@app/store';
import { Check } from 'lucide-react';
import { Link } from 'react-router-dom';

export function HomePage() {
  const { loggedIn } = useUserStore();

  return (
    <>
      <div className="grid grid-cols-1 place-content-stretch gap-4">
        <p className="pt-8 text-center text-4xl font-semibold">Welcome to Biobank</p>
      </div>
      {loggedIn && (
        <div className="container mx-auto grid grid-cols-1 place-content-stretch gap-4 pt-10 md:grid-cols-2">
          <Card>
            <CardHeader>
              <CardTitle>Add or View Patient Visits</CardTitle>
              <CardDescription>View patient visits, or Record a visit for a patient.</CardDescription>
            </CardHeader>
            <CardFooter>
              <div className="w-full">
                <Link to={'/patients'}>
                  <Button className="w-full">
                    <Check className="mr-2 h-4 w-4" /> Patient visit
                  </Button>
                </Link>
              </div>
            </CardFooter>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>Add a Patient</CardTitle>
              <CardDescription>Add a new patient to a study.</CardDescription>
            </CardHeader>
            <CardFooter>
              <div className="w-full">
                <Link to={'/patients/add'}>
                  <Button className="w-full">
                    <Check className="mr-2 h-4 w-4" /> New patient
                  </Button>
                </Link>
              </div>
            </CardFooter>
          </Card>
        </div>
      )}
    </>
  );
}
