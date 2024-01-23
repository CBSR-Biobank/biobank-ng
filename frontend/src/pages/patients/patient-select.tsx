import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { Button } from '@app/components/ui/button';
import { Input } from '@app/components/ui/input';
import { usePatientStore } from '@app/store';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { ChangeEvent, KeyboardEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AdminPage } from '../admin-page';

export function PatientSelect() {
  const { setPatient } = usePatientStore();
  const navigate = useNavigate();
  const [input, setInput] = useState('');

  useEffect(() => {
    setPatient(undefined);
  }, []);

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    setInput(event.target.value);
  };
  const handleInputKeyUp = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      handleSubmit(undefined);
    }
  };

  const handleSubmit = (e?: React.SyntheticEvent) => {
    if (e) {
      e.preventDefault();
    }
    if (input.trim() !== '') {
      navigate(`/patients/${input}`);
    }
  };

  return (
    <>
      <PatientBreadcrumbs />
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-4xl font-semibold text-sky-600">Select a Patient</p>
        </AdminPage.Title>
        <form className="flex w-full max-w-sm items-center space-x-2 pt-8" onClick={handleSubmit} autoComplete="on">
          <Input
            id="patientNumber"
            placeholder="Patient Number"
            onChange={handleInputChange}
            onKeyUp={handleInputKeyUp}
          />
          <Button type="submit" icon={faPaperPlane}>
            Submit
          </Button>
        </form>
      </AdminPage>
    </>
  );
}
