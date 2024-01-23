import { PatientApi } from '@app/api/patient-api';
import { Patient, PatientUpdate } from '@app/models/patient';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { PropertyChangerResult } from '../property-changer/property-changer';
import { PropertyChangerDate } from '../property-changer/property-changer-date';
import { PropertyChangerStudy } from '../property-changer/property-changer-study';
import { PropertyChangerText } from '../property-changer/property-changer-text';
import { ShowError } from '../show-error';

export const PatientPropertyChanger: React.FC<{
  patient: Patient;
  propertyToUpdate: string;
  onClose: () => void;
}> = ({ patient, propertyToUpdate, onClose }) => {
  const queryClient = useQueryClient();

  const updatePatient = useMutation((updatedPatient: PatientUpdate) => PatientApi.update(updatedPatient), {
    onSuccess: (patient) => {
      queryClient.setQueryData(['patients', patient.id], patient);
      queryClient.invalidateQueries(['publication', 'types']);
    }
  });

  const handleClose = (result: PropertyChangerResult, propertyName: string, newValue?: unknown) => {
    if (result === 'ok') {
      const prop = propertyName as keyof PatientUpdate;
      const newValues = { ...patient, [prop]: newValue };
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

    case 'createdAt':
      return <PropertyChangerDate {...commonProps} label="Created Date" value={patient.createdAt} required={true} />;

    case 'study':
      return <PropertyChangerStudy {...commonProps} label="Study" value={patient.studyNameShort} required={true} />;

    default:
      throw new Error('property is invalid: ' + propertyToUpdate);
  }
};
