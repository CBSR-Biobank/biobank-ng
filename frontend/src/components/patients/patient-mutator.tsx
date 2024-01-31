import { PatientApi } from '@app/api/patient-api';
import { Patient, PatientUpdate } from '@app/models/patient';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { MutatorResult } from '../mutators/mutator';
import { MutatorText } from '../mutators/mutator-text';
import { ShowError } from '../show-error';
import { MutatorStudy } from '../studies/mutator-study';

export const PatientMutator: React.FC<{
  patient: Patient;
  propertyToUpdate: string;
  onClose: () => void;
}> = ({ patient, propertyToUpdate, onClose }) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const updatePatient = useMutation(
    (updatedPatient: PatientUpdate) => PatientApi.update(patient.pnumber, updatedPatient),
    {
      onSuccess: (updated) => {
        queryClient.setQueryData(['patients', updated.pnumber], updated);
        queryClient.invalidateQueries(['patients', updated.pnumber]);

        if (updated.pnumber != patient.pnumber) {
          navigate(`../${updated.pnumber}`);
        }
      }
    }
  );

  const handleClose = (result: MutatorResult, propertyName: string, newValue?: unknown) => {
    if (result === 'ok') {
      const prop = propertyName as keyof PatientUpdate;
      const newValues = {
        pnumber: patient.pnumber,
        studyNameShort: patient.studyNameShort,
        [prop]: newValue
      };
      updatePatient.mutate(newValues);
    }
    onClose();
  };

  if (propertyToUpdate === null) {
    throw new Error('property to update is invalid');
  }

  if (updatePatient.isError) {
    return <ShowError error={updatePatient.error} />;
  }

  const commonProps = {
    propertyName: propertyToUpdate,
    title: 'Update Patient',
    open: true,
    onClose: handleClose
  };

  switch (propertyToUpdate) {
    case 'pnumber':
      return (
        <MutatorText
          {...commonProps}
          label="Patient Number"
          value={patient.pnumber}
          required={true}
          multiline={false}
          maxlen={80}
        />
      );

    case 'studyNameShort':
      return <MutatorStudy {...commonProps} label="Study" value={patient.studyNameShort} required={true} />;

    default:
      throw new Error('property is invalid: ' + propertyToUpdate);
  }
};
