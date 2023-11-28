import { Alert, AlertDescription, AlertTitle } from '@app/components/alert';
import { faCircleInfo } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const InfoCard: React.FC<{ title: String; message: String }> = ({ title, message }) => {
  return (
    <div className="grid grid-cols-1 gap-8">
      <Alert className="border-sky-500 bg-sky-200 text-slate-500">
        <FontAwesomeIcon icon={faCircleInfo} />
        <AlertTitle>{title}</AlertTitle>
        <AlertDescription>{message}</AlertDescription>
      </Alert>
    </div>
  );
};
