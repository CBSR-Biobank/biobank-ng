import { Annotations } from '@app/components/annotations/annotation';
import { CircularProgress } from '@app/components/circular-progress';
import { ShowError } from '@app/components/show-error';
import { useStudyAnnotationTypes } from '@app/hooks/use-study';
import { Annotation } from '@app/models/annotation';
import { CollectionEvent } from '@app/models/collection-event';

export const CollectionEventAnnotations: React.FC<{
  collectionEvent: CollectionEvent;
  onClick: (annotation: Annotation, value?: string | null) => void;
}> = ({ collectionEvent, onClick }) => {
  const { annotationTypes, isLoading, isError, error } = useStudyAnnotationTypes(collectionEvent.studyNameShort);

  if (isError) {
    return <ShowError error={error} />;
  }

  if (isLoading || !annotationTypes) {
    return <CircularProgress />;
  }

  return (
    <Annotations
      mutatorTitle="Update Visit"
      annotations={collectionEvent.annotations}
      annotationTypes={annotationTypes}
      onUpdate={onClick}
    />
  );
};
