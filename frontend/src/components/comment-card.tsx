import { Comment } from '@app/models/comment';
import { format } from 'date-fns';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';

export const CommentCard: React.FC<{ comment: Comment }> = ({ comment }) => {
  const user = comment.user ?? 'Anonymous';

  return (
    <Card className="my-2">
      <CardHeader>
        <CardTitle className="text-sm">{user}</CardTitle>
        <CardDescription className="text-xs">{format(comment.createdAt, 'yyyy-MM-dd hh:mm')}</CardDescription>
      </CardHeader>
      <CardContent className="text-sm">{comment.message}</CardContent>
    </Card>
  );
};
