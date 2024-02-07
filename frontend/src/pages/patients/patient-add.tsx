import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { CircularProgress } from '@app/components/circular-progress';
import { LabelledInput } from '@app/components/forms/labelled-input';
import { StudySelect } from '@app/components/forms/study-select';
import { Button } from '@app/components/ui/button';
import { usePatientAdd } from '@app/hooks/use-patient';
import { useStudyNames } from '@app/hooks/use-study';
import { Patient } from '@app/models/patient';
import { usePatientStore } from '@app/store';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { z } from 'zod';
import { AdminPage } from '../admin-page';

const schema = z.object({
  pnumber: z.string().min(1, { message: 'a patient number is required' }),
  studyNameShort: z.string().min(1, { message: 'a study name is required' })
});

export function PatientAddPage() {
  const navigate = useNavigate();
  const { setPatient } = usePatientStore();
  const [searchParams, _setSearchParams] = useSearchParams({ pnumber: '' });
  const studyNamesQry = useStudyNames();
  const patientAddMutation = usePatientAdd();

  const {
    control,
    register,
    handleSubmit,
    reset,
    formState: { isValid, errors }
  } = useForm<z.infer<typeof schema>>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
    defaultValues: {
      pnumber: searchParams.get('pnumber') ?? '',
      studyNameShort: ''
    }
  });

  useEffect(() => {
    setPatient(undefined);
  }, []);

  const onSubmit: SubmitHandler<z.infer<typeof schema>> = (values) => {
    patientAddMutation.mutate(values, {
      onSuccess: (newPatient: Patient) => {
        navigate(`/patients/${newPatient.pnumber}`);
        reset();
      },
      onError: () => {
        reset();
      }
    });
  };

  if (studyNamesQry.isLoading || !studyNamesQry.studyNames) {
    return <CircularProgress />;
  }

  return (
    <>
      <PatientBreadcrumbs />
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-4xl font-semibold text-sky-600">Add Patient</p>
        </AdminPage.Title>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="grid grid-cols-1 gap-6">
            <LabelledInput
              id="patientNumber"
              label="Patient Number"
              errorMessage={errors?.pnumber?.message}
              {...register('pnumber')}
            />

            <StudySelect control={control} name="studyNameShort" studies={studyNamesQry.studyNames} />

            <div className="flex gap-4">
              <Button disabled={!isValid} type="submit" icon={faPaperPlane}>
                Submit
              </Button>
            </div>
          </div>
        </form>
      </AdminPage>
    </>
  );
}
