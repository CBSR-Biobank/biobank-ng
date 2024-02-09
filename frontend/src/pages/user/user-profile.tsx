import { useUserStore } from '@app/store';
import { AdminPage } from '../admin-page';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserDoctor, faGear, faG } from '@fortawesome/free-solid-svg-icons';
import { Card, CardContent, CardHeader, CardTitle } from '@app/components/ui/card';


export function UserProfile() {
  const { user } = useUserStore();
  
  return (
    <>
      {/* <UserBreadcrumbs /> */}
      <AdminPage>
        <AdminPage.Title hasBorder>
          <p className="text-4xl font-semibold text-sky-600">User Profile</p>
        </AdminPage.Title>
        {/*****************************************************************/}
        <div className='shadow-xl rounded-lg '>
          <Card>
            <CardHeader >
              <div className='flex items-center justify-between'>
                <div className='flex items-center'>
                  <FontAwesomeIcon icon={faUserDoctor} size="3x" style={{color: "#74C0FC", position: "sticky" }  } />
                    <CardTitle>
                  
                        {/* <p className='text-3xl font-semibold text-sky-600'>{user?.fullname}</p> */}
                        <p className='pl-4 text-3xl font-semibold text-sky-600'>Bryan Kostelyk</p>  {/* Test Name*/}
                        <p className='pl-4 text-sm font-semibold text-gray-400'>{user?.isGlobalAdmin ? ' Admin': ''}</p>
                  
                    </CardTitle>
                </div>
                  <div className='flex items-center rounded-xl shadow-md p-1 hover:bg-slate-200'>
                  <FontAwesomeIcon icon={faGear} size='2x' color='gray'/> {/* Wrap in button for settings window */}
                  </div>
              </div>
              {/* {JSON.stringify(user, null, 2)}  */}
            </CardHeader>

            
            <CardContent>
              <div className='flex items-center space-x-8'>
                <Card>
                  <CardContent>
                    {/* {user?.username} */}
                    <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    </p>
                  </CardContent>
                </Card>
                <Card>
                  <CardContent>
                    {/* {user?.username} */}
                    <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    </p>
                  </CardContent>
                </Card>
                <Card>
                  <CardContent>
                    {/* {user?.username} */}
                    <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi tenetur quam perspiciatis quisquam? Maxime doloremque, corporis ex error blanditiis placeat sed voluptate at, reiciendis aliquid eaque iste, cupiditate dolorum quaerat.
                    </p>
                  </CardContent>
                </Card>
              </div>
            </CardContent>
          </Card>
        </div>
       
        {/*****************************************************************/}
      </AdminPage>
    </>
  );
}
