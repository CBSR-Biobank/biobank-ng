import { useUserStore } from '@app/store';
import { AdminPage } from '../admin-page';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserDoctor, faGear, faG } from '@fortawesome/free-solid-svg-icons';
import { Card, CardContent, CardHeader, CardTitle } from '@app/components/ui/card';
import { EntityProperty } from '@app/components/entity-property';
import { MutatorDialog } from '@app/components/mutators/mutator-dialog';
import { MutatorText } from '@app/components/mutators/mutator-text';
import { cn } from '@app/utils';
import { Button } from '@app/components/ui/button';



export function UserProfile() {
  const { user } = useUserStore();
  const userGroups = user?.groups;
  
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
                        <p className='pl-4 text-3xl font-semibold text-sky-600'>{user?.fullname}</p>  {/* Test Name*/}
                        <p className='pl-4 text-sm font-semibold text-gray-400'>{user?.isGlobalAdmin ? ' Admin': ''}</p>
                  
                    </CardTitle>
                </div>
              </div>
            </CardHeader>

              <CardContent>
                
                <div className='grid grid-cols-1 gap-6 md:grid-cols-2'>
                  <Card>
                    <CardHeader>
                      <div className='flex items-center justify-between'>
                        <CardTitle>
                          <p className='pl-4 text-2xl font-semibold text-sky-400'>My Info</p>
                          <p className='pl-4 text-sm font-semibold text-gray-400'>Last log in: </p>
                          
                        </CardTitle>
                        <Button className='flex'>
                           <FontAwesomeIcon icon={faGear} className={'flex size-6 hover:animate-spin'}/>
                        </Button>
                      </div>
                    </CardHeader>

                    <CardContent>
                  
                        {/* {user?.username} */}
                        <EntityProperty
                          propName={'userName'}
                          label={'User Name'}
                          >
                          {user?.username}
                        </EntityProperty>
                        {/* {user?.username} */}
                        <EntityProperty
                          propName={'fullName'}
                          label={'Full Name'}
                          >
                          {user?.fullname}
                        </EntityProperty>
                        
                        <EntityProperty
                         propName={'userType'} 
                         label={'User Type'}>
                          {user?.groups.map((group) => (
                            <p key={group.groupId}>{group.name}</p>
                             ))}
                        </EntityProperty>

                    </CardContent>
                  </Card>
                  <Card>
                    <CardHeader>
                      <CardTitle>
                        <p className='pl-4 text-2xl font-semibold text-sky-400'>My Patient Studies</p>
                      </CardTitle>
                    </CardHeader>
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
