import { CollectionEventDetails } from '@app/components//collection-events/collection-event-details';
import { CircularProgress } from '@app/components/circular-progress';
import { usePatientStore } from '@app/store';

import { useParams } from 'react-router-dom';

export function CollectionEventView() {
  const { vnumber: vnumberStr } = useParams<{ vnumber: string }>();
  const vnumber = vnumberStr ? parseInt(vnumberStr) : undefined;
  const { patient } = usePatientStore();

  if (!patient || !vnumber) {
    return <CircularProgress />;
  }

  return <CollectionEventDetails patient={patient} vnumber={vnumber} />;
}
