import { CircularProgress } from '@app/components/circular-progress';
import { InfoCard } from '@app/components/info-card';
import { ShowError } from '@app/components/show-error';
import { useCollectionEventComments } from '@app/hooks/use-collection-event';

import { forwardRef } from 'react';

import { CommentCard } from '../comment-card';

export const CollectionEventComments = forwardRef<
  HTMLTableElement,
  {
    pnumber: string;
    vnumber: number;
  }
>(({ pnumber, vnumber }, forwardedRef) => {
  const commentsQry = useCollectionEventComments(pnumber, vnumber);
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
