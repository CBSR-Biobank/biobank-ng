import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { Input } from '@app/components/ui/input';
import { usePatientStore } from '@app/store';

import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { ChangeEvent, KeyboardEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { BbButton } from '@app/components/bb-button';
import { AdminPage } from '../admin-page';

export function PatientSelect() {
  const navigate = useNavigate();
  const { setPatient } = usePatientStore();
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
    navigate(`/patients/${input}`);
  };

  return (
    <>
      <PatientBreadcrumbs />
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-4xl font-semibold text-sky-600">Select a Patient</p>
        </AdminPage.Title>
        <form className="grid grid-cols-1 gap-4 pt-8 md:grid-cols-2" onSubmit={handleSubmit} autoComplete="on">
          <Input
            id="patientNumber"
            placeholder="Patient Number"
            onChange={handleInputChange}
            onKeyUp={handleInputKeyUp}
          />
          <BbButton
            type="submit"
            trailingIcon={faPaperPlane}
            disabled={input.trim() === ''}
            className="min-w-max md:w-min"
          >
            Submit
          </BbButton>
        </form>
      </AdminPage>
    </>
  );
}
