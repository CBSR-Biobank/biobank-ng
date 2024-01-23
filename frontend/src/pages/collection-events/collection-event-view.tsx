import { CircularProgress } from '@app/components/circular-progress';
import { CommentsTable } from '@app/components/comments-table';
import { EntityProperty } from '@app/components/entity-property';
import { ShowError } from '@app/components/show-error';
import { StatusChip } from '@app/components/status-chip';
import { Button } from '@app/components/ui/button';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@app/components/ui/collapsible';
import { useCollectionEvent } from '@app/hooks/useCollectionEvent';
import { usePatientStore } from '@app/store';
import { cn } from '@app/utils';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { AdminPage } from '../admin-page';
import { SourceSpecimenTable } from '../specimens/source-specimens-table';

export function CollectionEventView() {
  const { patient, setCollectionEvent } = usePatientStore();
  const params = useParams();
  const vnumber = params.vnumber ? parseInt(params?.vnumber) : undefined;
  const collectionEventQry = useCollectionEvent(patient?.pnumber, vnumber);
  const { data: collectionEvent } = collectionEventQry;
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    if (collectionEvent) {
      setCollectionEvent(collectionEvent);
    }
  }, [collectionEvent]);

  if (!params.vnumber) {
    return <ShowError message="visit number is invalid" />;
  }

  if (collectionEventQry.isError) {
    return <ShowError error={collectionEventQry.error} />;
  }

  if (!patient || collectionEventQry.isLoading || !collectionEvent) {
    return <CircularProgress />;
  }

  const handlePropChange = (_propertyName: string) => {
    // setPropertyToUpdate(propertyName);
    // setOpen(true);
  };

  return (
    <>
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-sm font-semibold text-gray-400">
            {patient.studyNameShort} Patient: {patient.pnumber}
          </p>
          <p className="text-4xl font-semibold text-sky-600">Visit {params.vnumber}</p>
        </AdminPage.Title>

        <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
          <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
            <EntityProperty propName="patientNumber" label="Patient Number" allowChanges={false}>
              {collectionEvent.patientNumber}
            </EntityProperty>

            <EntityProperty propName="visitNumber" label="Visit" allowChanges={true} handleChange={handlePropChange}>
              {collectionEvent.visitNumber}
            </EntityProperty>

            <EntityProperty propName="status" label="Status" allowChanges={true} handleChange={handlePropChange}>
              <StatusChip status={collectionEvent.status} size="xs" />
            </EntityProperty>
          </div>

          <EntityProperty propName="studyNameShort" label="Study" allowChanges={false}>
            {collectionEvent.studyNameShort}
          </EntityProperty>

          {collectionEvent.attributes.map((attribute, index) => (
            <EntityProperty
              key={index}
              propName={attribute.name}
              label={attribute.name}
              allowChanges={true}
              handleChange={handlePropChange}
            >
              {attribute.value}
            </EntityProperty>
          ))}

          {collectionEvent.comments.length > 0 && (
            <Collapsible open={isOpen} onOpenChange={setIsOpen} className="rounded-md border-2 border-solid">
              <div className="flex items-center justify-between bg-gray-300/50 px-4 py-2">
                <h4 className="text-sm text-slate-700">Comments</h4>
                <CollapsibleTrigger asChild>
                  <Button variant="ghost" size="sm" className="w-9 p-0">
                    <FontAwesomeIcon
                      icon={faChevronRight}
                      className={cn('duration-300 ease-in-out', { 'rotate-90': isOpen })}
                    />
                    <span className="sr-only">Toggle</span>
                  </Button>
                </CollapsibleTrigger>
              </div>
              <CollapsibleContent className="space-y-2">
                <CommentsTable comments={collectionEvent.comments} />
              </CollapsibleContent>
            </Collapsible>
          )}
          {collectionEvent.sourceSpecimens.length > 0 && (
            <div className="space-y-2 rounded-md border-2 border-solid">
              <h4 className="bg-gray-300/50 px-4 py-2 text-sm font-normal text-slate-700">Specimens</h4>
              <SourceSpecimenTable specimens={collectionEvent.sourceSpecimens} />
            </div>
          )}
        </div>
      </AdminPage>
    </>
  );
}
