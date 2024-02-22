import { Comment } from '@app/models/comment';

import { format } from 'date-fns';

import { Card } from './ui/card';

export const CommentCard: React.FC<{ comment: Comment }> = ({ comment }) => {
  const user = comment.user ?? 'Anonymous';

  return (
    <Card className="m-2 bg-gray-300/20 p-1">
      <div className="m-2">
        <div className="flex flex-col gap-4 pt-2 text-sm ">
          <div className="flex flex-row text-xs">
            <p className="text-sm font-semibold">{user}</p>
            <p className="ml-auto font-normal">{format(comment.createdAt, 'yyyy-MM-dd hh:mm')}</p>
          </div>
          <p>{comment.message}</p>
        </div>
      </div>
    </Card>
  );
};
