import { PatientApi } from '@app/api/patient-api';
import { Patient, PatientUpdate } from '@app/models/patient';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { PropertyChangerResult } from '../property-changer/property-changer';
import { PropertyChangerStudy } from '../property-changer/property-changer-study';
import { PropertyChangerText } from '../property-changer/property-changer-text';
import { ShowError } from '../show-error';

export const PatientPropertyChanger: React.FC<{
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

  const handleClose = (result: PropertyChangerResult, propertyName: string, newValue?: unknown) => {
    if (result === 'ok') {
      const prop = propertyName as keyof PatientUpdate;
      const newValues = {
        pnumber: patient.pnumber,
        studyNameShort: patient.studyNameShort,
        [prop]: newValue
      };
      console.log(prop, newValues);
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
        <PropertyChangerText
          {...commonProps}
          label="Patient Number"
          value={patient.pnumber}
          required={true}
          multiline={false}
          maxlen={80}
        />
      );

    case 'studyNameShort':
      return <PropertyChangerStudy {...commonProps} label="Study" value={patient.studyNameShort} required={true} />;

    default:
      throw new Error('property is invalid: ' + propertyToUpdate);
  }
};
