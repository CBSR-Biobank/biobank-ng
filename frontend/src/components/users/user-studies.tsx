import { faClipboardUser, faFileMedical } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button } from '../ui/button';
import { Card, CardContent } from '../ui/card';

export function UserStudies() {
  //const { user } = useRouterUser();

  return (
    <>
      <Card>
        <CardContent className=" flex items-center justify-between pb-0 pl-2 pr-2">
          <div className="flex items-center justify-between">
            <div className="flex items-center pr-0">
              <FontAwesomeIcon icon={faClipboardUser} className="size-6 justify-center text-gray-400" />
              <div>
                <p className="pl-2 text-xl font-semibold text-sky-300"> Study 1</p>
                <p className="pl-2 text-sm font-semibold text-gray-400">Last updated: </p>
              </div>
            </div>
          </div>

          <Button>
            <p className="pr-2 text-sm font-semibold">View</p>
            <FontAwesomeIcon icon={faFileMedical} size="lg" />
          </Button>
        </CardContent>
      </Card>

      <Card>
        <CardContent className=" flex items-center justify-between pb-0 pl-2 pr-2">
          <div className="flex items-center justify-between">
            <div className="flex items-center ">
              <FontAwesomeIcon icon={faClipboardUser} className="size-6 justify-center text-gray-400" />
              <div>
                <p className="pl-2 text-xl font-semibold text-sky-300"> Study 2</p>
                <p className="pl-2 text-sm font-semibold text-gray-400">Last updated: </p>
              </div>
            </div>
          </div>

          <Button size={'icon'}>
            <FontAwesomeIcon icon={faFileMedical} size="lg" />
          </Button>
        </CardContent>
      </Card>

      <Card>
        <CardContent className=" flex items-center justify-between pb-0 pl-2 pr-2">
          <div className="flex items-center justify-between">
            <div className="flex items-center ">
              <FontAwesomeIcon icon={faClipboardUser} className="size-6 justify-center text-gray-400" />
              <div>
                <p className="pl-2 text-xl font-semibold text-sky-300"> Study 3</p>
                <p className="pl-2 text-sm font-semibold text-gray-400">Last updated: </p>
              </div>
            </div>
          </div>

          <Button size={'icon'}>
            <FontAwesomeIcon icon={faFileMedical} size="lg" />
          </Button>
        </CardContent>
      </Card>

      <Card>
        <CardContent className=" flex items-center justify-between pb-0 pl-2 pr-2">
          <div className="flex items-center justify-between">
            <div className="flex items-center ">
              <FontAwesomeIcon icon={faClipboardUser} className="size-6 justify-center text-gray-400" />
              <div>
                <p className="pl-2 text-xl font-semibold text-sky-300"> Study 4</p>
                <p className="pl-2 text-sm font-semibold text-gray-400">Last updated: </p>
              </div>
            </div>
          </div>

          <Button size={'icon'}>
            <FontAwesomeIcon icon={faFileMedical} size="lg" />
          </Button>
        </CardContent>
      </Card>

      <Card>
        <CardContent className=" flex items-center justify-between pb-0 pl-2 pr-2">
          <div className="flex items-center justify-between">
            <div className="flex items-center ">
              <FontAwesomeIcon icon={faClipboardUser} className="size-6 justify-center text-gray-400" />
              <div>
                <p className="pl-2 text-xl font-semibold text-sky-300"> Study 5</p>
                <p className="pl-2 text-sm font-semibold text-gray-400">Last updated: </p>
              </div>
            </div>
          </div>

          <Button size={'icon'}>
            <FontAwesomeIcon icon={faFileMedical} size="lg" />
          </Button>
        </CardContent>
      </Card>
    </>
  );
}
