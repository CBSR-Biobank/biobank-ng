import { CircularProgress } from '@app/components/circular-progress';
import { InfoCard } from '@app/components/info-card';
import { ShowError } from '@app/components/show-error';
import { usePatientComments } from '@app/hooks/use-patient';

import { forwardRef } from 'react';

import { CommentCard } from '../comment-card';

export const PatientComments = forwardRef<
  HTMLTableElement,
  {
    pnumber: string;
  }
>(({ pnumber }, forwardedRef) => {
  const commentsQry = usePatientComments(pnumber);
  const { data: comments } = commentsQry;

  if (commentsQry.isError) {
    return <ShowError error={commentsQry.error} />;
  }

  if (commentsQry.isLoading || !comments) {
    return <CircularProgress />;
  }

  if (comments.length <= 0) {
    return (
      <InfoCard
        variant="default"
        ref={forwardedRef}
        className="m-4"
        title="No Comments"
        message="This patient has no comments on record"
      />
    );
  }

  return (
    <div className="m-4 grid grid-cols-1">
      {comments.map((comment, index) => (
        <CommentCard key={index} comment={comment} />
      ))}
    </div>
  );
});
