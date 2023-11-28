import { Comment } from '@app/models/comments';
import format from 'date-fns/format';

export const CommentsTable: React.FC<{ comments: Comment[] }> = ({ comments }) => {
  return (
    <div className="grid grid-cols-1 gap-8 overflow-x-auto">
      <table className="w-full min-w-max table-auto text-left">
        <thead>
          <tr>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Comment
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                User
              </p>
            </th>
            <th className="border-y border-gray-200 p-3">
              <p className="block font-sans text-sm font-normal leading-none text-slate-700 antialiased opacity-70">
                Date
              </p>
            </th>
          </tr>
        </thead>
        <tbody>
          {comments.map((comment) => (
            <tr key={comment.id}>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {comment.message}
                </p>
              </td>
              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {comment.user}
                </p>
              </td>

              <td className="border-b border-gray-200 p-3">
                <p className="block font-sans text-sm font-normal leading-normal text-slate-700 antialiased">
                  {format(comment.createdAt, 'yyyy-MM-dd')}
                </p>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
