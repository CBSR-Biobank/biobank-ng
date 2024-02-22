import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@app/components/ui/table';
import { Comment } from '@app/models/comment';

import { format } from 'date-fns';

export const CommentsTable: React.FC<{ comments: Comment[] }> = ({ comments }) => {
  return (
    <div className="grid grid-cols-1 gap-8 overflow-x-auto">
      <Table className="w-full min-w-max table-auto text-left">
        <TableHeader>
          <TableRow>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Date</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">User</TableHead>
            <TableHead className="text-sm font-normal text-slate-700 opacity-70">Comment</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {comments.map((comment) => (
            <TableRow key={comment.id}>
              <TableCell className="text-sm font-normal leading-normal text-slate-700">
                {format(comment.createdAt, 'yyyy-MM-dd')}
              </TableCell>
              <TableCell className="text-sm font-normal leading-normal text-slate-700">{comment.user}</TableCell>
              <TableCell className="text-sm font-normal leading-normal text-slate-700 md:w-[40rem]">
                {comment.message}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};
