import { BbButton } from '@app/components/bb-button';
import { CircularProgress } from '@app/components/circular-progress';
import { StudySelect } from '@app/components/forms/study-select';
import { ShowError } from '@app/components/show-error';
import { useCatalogueRequest, useStudyNames } from '@app/hooks/use-study';
import { useStudyStore } from '@app/store';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { z } from 'zod';

const schema = z.object({
  studyNameShort: z.string(),
});

export const CatalogueRequest: React.FC = () => {
  const navigate = useNavigate();
  const { isLoading, isError, error, studyNames } = useStudyNames();
  const { setCatalogueUrl } = useStudyStore();
  const catalogueMutation = useCatalogueRequest();

  const {
    control,
    handleSubmit,
    reset,
    formState: { isValid },
  } = useForm<z.infer<typeof schema>>({
    mode: 'all',
    reValidateMode: 'onChange',
    resolver: zodResolver(schema),
  });

  if (isError && error) {
    return <ShowError error={error} />;
  }

  if (isLoading || !studyNames) {
    return <CircularProgress />;
  }

  const onSubmit: SubmitHandler<z.infer<typeof schema>> = (values) => {
    const selectedStudy = studyNames.find((sn) => sn.nameShort === values.studyNameShort);
    if (!selectedStudy) {
      throw new Error('invalid study selected');
    }
    catalogueMutation.mutate(selectedStudy.nameShort, {
      onSuccess: (url: string) => {
        setCatalogueUrl(url);
        const path = url.split('/');
        const catId = path[path?.length - 1];
        navigate(`/study-catalogue/${selectedStudy.nameShort}/${catId}`);
        reset();
      },
      onError: () => {
        reset();
      },
    });
  };

  return (
    <>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="grid grid-cols-1 gap-6">
          <StudySelect
            label="Select the study you want a specimen catalogue for:"
            control={control}
            name="studyNameShort"
            studies={studyNames}
          />

          <div className="flex gap-4">
            <BbButton disabled={!isValid} size="xl" type="submit" leadingIcon={faPaperPlane}>
              Submit
            </BbButton>
          </div>
        </div>
      </form>
    </>
  );
};
