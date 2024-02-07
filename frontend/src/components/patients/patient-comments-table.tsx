import { CircularProgress } from '@app/components/circular-progress';
import { InfoCard } from '@app/components/info-card';
import { ShowError } from '@app/components/show-error';
import { usePatientComments } from '@app/hooks/use-patient';
import { forwardRef } from 'react';
import { CommentsTable } from '../comments-table';

export type CommentsTableProps = {
  pnumber: string;
};

export const PatientCommentsTable = forwardRef<HTMLTableElement, CommentsTableProps>(({ pnumber }, forwardedRef) => {
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
        ref={forwardedRef}
        className="pl-8"
        title="No Comments"
        message="This patient has no comments on record."
      />
    );
  }

  return <CommentsTable comments={comments} />;
});
