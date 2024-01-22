import { PatientApi } from '@app/api/patients-api';
import { StudyApi } from '@app/api/study-api';
import { PatientBreadcrumbs } from '@app/components/breadcrumbs/patients-breadcrubms';
import { CircularProgress } from '@app/components/circular-progress';
import { LabelledInput } from '@app/components/forms/labelled-input';
import { StudySelect } from '@app/components/forms/study-select';
import { Button } from '@app/components/ui/button';
import { Patient, PatientAdd } from '@app/models/patient';
import { Status } from '@app/models/status';
import { usePatientStore } from '@app/store';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { AdminPage } from '../admin-page';

const schema = z.object({
  pnumber: z.string().min(1, { message: 'a patient number is required' }),
  createdAt: z.string().or(z.literal('')), // allows empty string
  studyNameShort: z.string()
});

export function PatientAddPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { setPatient } = usePatientStore();
  const now = new Date();
  now.setUTCHours(0, 0, 0, 0);

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
      pnumber: '',
      createdAt: now.toISOString().substring(0, 16),
      studyNameShort: ''
    }
  });

  const studyNamesQry = useQuery(['studies', 'names'], () => StudyApi.names(Status.ACTIVE), {
    keepPreviousData: true
  });

  const addPatient = useMutation((patient: PatientAdd) => PatientApi.add(patient), {
    onSuccess: (newPatient: Patient) => {
      queryClient.setQueryData(['patients', newPatient.id], newPatient);
      queryClient.invalidateQueries(['patients']);
      navigate(`/patients/${newPatient.pnumber}`);
      reset();
    },
    onError: () => {
      reset();
    }
  });

  useEffect(() => {
    setPatient(undefined);
  }, []);

  const onSubmit: SubmitHandler<z.infer<typeof schema>> = (values) => {
    console.log(values);
    const createdAt = new Date(Date.parse(values.createdAt));
    addPatient.mutate({ ...values, createdAt });
  };

  if (studyNamesQry.isLoading || !studyNamesQry.data) {
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

            <LabelledInput
              id="createdAt"
              type="datetime-local"
              label="Created Date"
              errorMessage={errors?.createdAt?.message}
              {...register('createdAt')}
              defaultValue={new Date().toISOString().substring(0, 16)}
              required
            />
            <StudySelect control={control} name="studyNameShort" studies={studyNamesQry.data} />
            <Button className="flex w-1/3 gap-3 bg-sky-500 hover:bg-sky-400" disabled={!isValid} type="submit">
              Submit
              <FontAwesomeIcon icon={faPaperPlane} />
            </Button>
          </div>
        </form>
      </AdminPage>
    </>
  );
}
