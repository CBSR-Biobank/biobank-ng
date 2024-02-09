import { DialogClose, DialogFooter } from '@app/components/ui/dialog';
import { Annotation, annotationValueAsDate } from '@app/models/annotation';
import { AnnotationType } from '@app/models/annotation-type';
import { faCirclePlus, faTriangleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Chip } from '../chip';
import { EntityProperty } from '../entity-property';
import { MutatorDate } from '../mutators/mutator-date';
import { MutatorDialog } from '../mutators/mutator-dialog';
import { MutatorNumber } from '../mutators/mutator-number';
import { MutatorSelect } from '../mutators/mutator-select';
import { MutatorText } from '../mutators/mutator-text';
import { OkButton } from '../ok-button';
import { Badge } from '../ui/badge';
import { AnnotationValueDeleteDialog } from './annotation-value-delete-dialog';

const AnnotationMutator: React.FC<{
  annotation: Annotation;
  annotationType: AnnotationType;
  onUpdate: (annotation: Annotation, value?: string) => void;
}> = ({ annotation, annotationType, onUpdate }) => {
  if (!annotationType) {
    throw new Error('invalid annotation type');
  }

  switch (annotation.type) {
    case 'number':
      return (
        <MutatorNumber
          label={annotation.name}
          value={annotation.value ? parseFloat(annotation.value) : undefined}
          required
          onClose={(value?: number) => onUpdate(annotation, value ? `${value}` : undefined)}
        />
      );

    case 'text':
      return (
        <MutatorText
          label={annotation.name}
          value={annotation.value ?? undefined}
          required
          onClose={(value?: string) => onUpdate(annotation, value)}
        />
      );

    case 'date_time':
      return (
        <MutatorDate
          label={annotation.name}
          value={annotationValueAsDate(annotation)}
          required
          onClose={(value?: Date) => onUpdate(annotation, value ? `${value}` : undefined)}
        />
      );

    case 'select_single':
    case 'select_multiple': {
      if (!annotationType.validValues) {
        throw new Error('invalid select_single annotation type');
      }

      let value = annotation.type === 'select_single' ? annotation.value ?? undefined : undefined;
      const options = annotationType.validValues
        .map((v) => ({ id: v, label: v }))
        .filter((v) => !annotation.value?.includes(v.id));

      const handleClose = (value?: string) => {
        if (annotation.type === 'select_multiple') {
          if (!value) {
            return;
          }
          const newValues = annotation.value ? annotation.value.split(';') : [];
          newValues.push(value);
          onUpdate(annotation, newValues.join(';'));
        } else {
          onUpdate(annotation, value ? `${value}` : undefined);
        }
      };

      if (options.length <= 0) {
        return (
          <>
            <div className="flex items-center gap-4">
              <FontAwesomeIcon icon={faTriangleExclamation} size="3x" className="text-yellow-500" />
              <div className="flex items-baseline gap-1">
                All
                <Chip variant="basic" size="sm">
                  {annotation.name}
                </Chip>
                values for have been selected
              </div>
            </div>
            <DialogFooter className="grid-cols-1 gap-3 lg:grid-cols-2">
              <DialogClose asChild>
                <OkButton />
              </DialogClose>
            </DialogFooter>
          </>
        );
      }

      return (
        <MutatorSelect label={annotation.name} value={value} required propertyOptions={options} onClose={handleClose} />
      );
    }
  }
};

const SelectedValueChip: React.FC<{ value: string; onUnselected: (value: string) => void }> = ({
  value,
  onUnselected
}) => {
  const handleUnselect = () => {
    onUnselected(value);
  };

  return (
    <Badge variant="secondary" className="mb-1 mr-1">
      {value}

      <AnnotationValueDeleteDialog value={value} onClose={handleUnselect} />
    </Badge>
  );
};

const AnnotationValue: React.FC<{
  annotation: Annotation;
  onUpdate: (annotation: Annotation, value?: string) => void;
}> = ({ annotation, onUpdate }) => {
  const handleMultiSelectUnselected = (valueToRemove: string) => {
    const newValue = annotation.value
      ?.split(';')
      .filter((v) => v !== valueToRemove)
      .join(';');
    onUpdate(annotation, newValue);
  };

  if (annotation.value) {
    switch (annotation.type) {
      case 'date_time':
        return <>{annotationValueAsDate(annotation)}</>;

      case 'select_single':
        return (
          <Chip variant="primary" size="sm">
            {annotation.value}
          </Chip>
        );

      case 'select_multiple':
        return annotation.value
          .split(';')
          .map((value) => <SelectedValueChip key={value} value={value} onUnselected={handleMultiSelectUnselected} />);
    }
  }

  return <>{annotation.value}</>;
};

export const Annotations: React.FC<{
  mutatorTitle: string;
  annotations: Annotation[];
  annotationTypes: AnnotationType[];
  onUpdate: (annotation: Annotation, value?: string) => void;
}> = ({ mutatorTitle, annotations, annotationTypes, onUpdate }) => {
  const typeLookup = Object.fromEntries(annotationTypes.map((at) => [at.label, at]));

  return (
    <>
      {annotations.map((annotation) => {
        let extraProps = {};

        if (annotation.type === 'select_multiple') {
          extraProps = {
            icon: faCirclePlus,
            tooltip: 'Add'
          };
        }

        return (
          <EntityProperty
            key={annotation.name}
            propName={annotation.name}
            label={annotation.name}
            mutator={
              <MutatorDialog title={mutatorTitle} {...extraProps}>
                <AnnotationMutator
                  annotation={annotation}
                  annotationType={typeLookup[annotation.name]}
                  onUpdate={onUpdate}
                ></AnnotationMutator>
              </MutatorDialog>
            }
          >
            <AnnotationValue annotation={annotation} onUpdate={onUpdate} />
          </EntityProperty>
        );
      })}
    </>
  );
};
