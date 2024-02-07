import { Annotation, annotationValueAsDate } from '@app/models/annotation';
import { Chip } from '../chip';
import { EntityProperty } from '../entity-property';
import { MutatorDate } from '../mutators/mutator-date';
import { MutatorDialog } from '../mutators/mutator-dialog';
import { MutatorNumber } from '../mutators/mutator-number';
import { MutatorText } from '../mutators/mutator-text';

const AnnotationMutator: React.FC<{
  annotation: Annotation;
  onClick: (propertyName: string, value?: string) => void;
}> = ({ annotation, onClick }) => {
  switch (annotation.type) {
    case 'number':
      return (
        <MutatorNumber
          label={annotation.name}
          value={annotation.value ? parseFloat(annotation.value) : undefined}
          required
          onClose={(value?: number) => onClick(annotation.name, value ? `{$value}` : undefined)}
        />
      );

    case 'text':
      return (
        <MutatorText
          label={annotation.name}
          value={annotation.value ?? undefined}
          required
          onClose={(value?: string) => onClick(annotation.name, value)}
        />
      );

    case 'date_time':
      return (
        <MutatorDate
          label={annotation.name}
          value={annotationValueAsDate(annotation)}
          required
          onClose={(value?: Date) => onClick(annotation.name, value ? `{$value}` : undefined)}
        />
      );
  }
};

const AnnotationValue: React.FC<{ annotation: Annotation }> = ({ annotation }) => {
  if (annotation.value) {
    if (annotation.type === 'date_time') {
      return <>{annotationValueAsDate(annotation)}</>;
    }

    if (annotation.type === 'select_single') {
      return (
        <Chip variant="primary" size="sm">
          {annotation.value}
        </Chip>
      );
    }

    if (annotation.type === 'select_multiple') {
      return annotation.value.split(';').map((value) => (
        <Chip key={value} variant="primary" size="sm">
          {value}
        </Chip>
      ));
    }
  }

  return <>{annotation.value}</>;
};

export const Annotations: React.FC<{
  annotations: Annotation[];
  onClick: (annotationName: string, value?: string) => void;
}> = ({ annotations, onClick }) => {
  return (
    <>
      {annotations.map((annotation) => (
        <EntityProperty
          key={annotation.name}
          propName={annotation.name}
          label={annotation.name}
          mutator={
            <MutatorDialog title="Update Patient">
              <AnnotationMutator annotation={annotation} onClick={onClick}></AnnotationMutator>
            </MutatorDialog>
          }
        >
          <AnnotationValue annotation={annotation} />
        </EntityProperty>
      ))}
    </>
  );
};
