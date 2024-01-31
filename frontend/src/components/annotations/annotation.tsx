import { Annotation } from '@app/models/annotation';
import { ReactNode } from 'react';
import { z } from 'zod';
import { Chip } from '../chip';
import { EntityProperty } from '../entity-property';

const AnnotationComp: React.FC<{ annotation: Annotation; onClick: (propertyName: string) => void }> = ({
  annotation,
  onClick
}) => {
  let value: ReactNode = <></>;

  if (annotation.value) {
    if (annotation.type === 'date_time') {
      const schema = z.union([z.null(), z.string().pipe(z.coerce.date())]).optional();
      value = <>{schema.parse(annotation.value)}</>;
    }

    if (annotation.type === 'select_single') {
      value = <Chip>{annotation.value}</Chip>;
    }

    if (annotation.type === 'select_multiple') {
      value = <Chip>{annotation.value}</Chip>;
    }
  }

  switch (annotation.type) {
    case 'number':
    case 'text':
    case 'date_time': {
      return (
        <EntityProperty
          propName={annotation.name}
          label={annotation.name}
          allowChanges={true}
          handleChange={() => onClick(annotation.name)}
        >
          <>{value}</>
        </EntityProperty>
      );
    }
  }
};

export const Annotations: React.FC<{ annotations: Annotation[]; onClick: (propertyName: string) => void }> = ({
  annotations,
  onClick
}) => {
  return (
    <>
      {annotations.map((annotation) => (
        <AnnotationComp key={annotation.name} annotation={annotation} onClick={onClick} />
      ))}
    </>
  );
};
