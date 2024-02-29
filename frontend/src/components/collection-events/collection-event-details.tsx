import { CircularProgress } from '@app/components/circular-progress';
import { EntityProperty } from '@app/components/entity-property';
import { MutatorDialog } from '@app/components/mutators/mutator-dialog';
import { MutatorStatus } from '@app/components/mutators/mutator-status';
import { MutatorVisitNumber } from '@app/components/patients/mutator-visit-number';
import { ShowError } from '@app/components/show-error';
import { StatusChip } from '@app/components/status-chip';
import { useCollectionEvent, useCollectionEventUpdate } from '@app/hooks/use-collection-event';
import { Annotation } from '@app/models/annotation';
import { Patient, takenVisitNumbers } from '@app/models/patient';
import { Status } from '@app/models/status';
import { AdminPage } from '@app/pages/admin-page';

import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import { CollectionEventAnnotations } from './collection-event-annotations';
import { CollectionEventButtons } from './collection-event-buttons';
import { SpecimensCollapsible } from './specimens-collapsible';
import { VisitCommentsCollapsible } from './visit-comments-collapsible';

export const CollectionEventDetails: React.FC<{ patient: Patient; vnumber: number }> = ({ patient, vnumber }) => {
  const navigate = useNavigate();
  const { collectionEvent, isLoading, isError, error } = useCollectionEvent(patient.pnumber, vnumber);
  const updateMutation = useCollectionEventUpdate();

  useEffect(() => {
    if (isError && error) {
      if (error?.status === 404) {
        navigate('./not-exist');
      }
    }
  }, [isError, error]);

  if (isError && error) {
    if (error?.status !== 404) {
      return <ShowError error={error} />;
    }
  }

  if (isLoading) {
    return <CircularProgress />;
  }

  if (!collectionEvent) {
    return <Outlet />;
  }

  const handleVnumberUpdated = (value?: number) => {
    if (!value) {
      return; // do nothing
    }

    updateMutation.mutate(
      {
        pnumber: collectionEvent.pnumber,
        vnumber: collectionEvent.vnumber,
        cevent: { ...collectionEvent, vnumber: value },
      },
      {
        onSuccess: () => {
          navigate(`../${value}`);
        },
        onError: (error: any) => {
          // query client config will handle the 401 error
          if ('status' in error && error.status !== 401) {
            // FIXME: display an error here
            return;
          }
        },
      }
    );
  };

  const handleStatusUpdated = (value?: Status) => {
    if (!value) {
      return; // do nothing
    }

    updateMutation.mutate({
      pnumber: collectionEvent.pnumber,
      vnumber: collectionEvent.vnumber,
      cevent: { ...collectionEvent, status: value },
    });
  };

  const handleAnnotationChange = (annotation: Annotation, value?: string) => {
    const annotations = collectionEvent.annotations.filter((a) => a.name !== annotation.name);
    annotations.push({ ...annotation, value: value ?? '' });
    updateMutation.mutate({
      pnumber: collectionEvent.pnumber,
      vnumber: collectionEvent.vnumber,
      cevent: { ...collectionEvent, annotations },
    });
  };

  return (
    <>
      <AdminPage.Title>
        <p className="text-sm font-semibold text-gray-400">
          {collectionEvent.studyNameShort} Patient: {patient.pnumber}
        </p>
        <p className="text-4xl font-semibold text-sky-600">Visit {vnumber}</p>
      </AdminPage.Title>

      <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
          <EntityProperty propName="patientNumber" label="Patient Number">
            {collectionEvent.pnumber}
          </EntityProperty>

          <EntityProperty propName="studyNameShort" label="Study">
            {collectionEvent.studyNameShort}
          </EntityProperty>

          <EntityProperty
            propName="visitNumber"
            label="Visit"
            mutator={
              <MutatorDialog title="Update Patient">
                <MutatorVisitNumber
                  vnumber={collectionEvent.vnumber}
                  disallow={takenVisitNumbers(patient)}
                  onClose={handleVnumberUpdated}
                />
              </MutatorDialog>
            }
          >
            {collectionEvent.vnumber}
          </EntityProperty>

          <EntityProperty
            propName="status"
            label="Status"
            mutator={
              <MutatorDialog title="Update Visit">
                <MutatorStatus label="Status" value={collectionEvent.status} onClose={handleStatusUpdated} required />
              </MutatorDialog>
            }
          >
            <StatusChip status={collectionEvent.status} size="xs" />
          </EntityProperty>

          <CollectionEventAnnotations collectionEvent={collectionEvent} onClick={handleAnnotationChange} />
        </div>

        <VisitCommentsCollapsible
          pnumber={patient.pnumber}
          vnumber={collectionEvent.vnumber}
          count={collectionEvent.commentCount}
        />

        <SpecimensCollapsible sourceSpecimens={collectionEvent.sourceSpecimens} />
      </div>

      <CollectionEventButtons collectionEvent={collectionEvent} />
    </>
  );
};
