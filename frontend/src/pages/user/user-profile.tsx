import { useUserStore } from '@app/store';
import { AdminPage } from '../admin-page';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserDoctor, faGear, faClipboardUser, faFileMedical, faEnvelope} from '@fortawesome/free-solid-svg-icons';
import { Card, CardContent, CardHeader, CardTitle } from '@app/components/ui/card';
import { EntityProperty } from '@app/components/entity-property';
import { Button } from '@app/components/ui/button';



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
                        <div className='flex items-center justify-between'>
                          <div>
                            <p className='pl-4 text-3xl font-semibold text-sky-600'>{user?.fullname}</p>  {/* Test Name*/}
                            <p className='pl-4 text-sm font-semibold text-gray-400'>{user?.isGlobalAdmin ? ' Admin': ''}</p>
                          </div>

                          <div>
                            <FontAwesomeIcon icon={faEnvelope}/>
                          </div>
                        </div>
                    </CardTitle>
                </div>
              </div>
            </CardHeader>

              <CardContent>
                <div className='grid grid-cols-1 gap-6 md:grid-cols-2'>
                  
                  {/* My Info Tab */}
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
                         label={'User Type'}
                         >
                          {user?.groups.map((group) => (
                            <p key={group.groupId}>{group.name}</p>
                             ))}
                        </EntityProperty>

                    </CardContent>
                  </Card>

                  {/* My Patient Studies Tab */}
                  <Card>
                    <CardHeader>
                      <CardTitle>
                        <p className='pl-4 text-2xl font-semibold text-sky-400'>My Patient Studies</p>
                      </CardTitle>
                    </CardHeader>
                      <CardContent className='space-y-2'>
                          <Card>
                            <CardContent className=' flex pl-2 pb-0 pr-2 justify-between items-center'>
                             
                              <div className='flex justify-between items-center'>
                                <div className='flex items-center pr-0'>
                                
                                  <FontAwesomeIcon icon={faClipboardUser} className='size-6 justify-center text-gray-400'/>
                                  <div>
                                    <p className='pl-2 text-xl font-semibold text-sky-300'> Study 1</p>
                                    <p className='pl-2 text-sm font-semibold text-gray-400'>Last updated: </p>
                                  </div>
                                </div>
                              </div>

                                <Button>
                                  <p className='pr-2 text-sm font-semibold'>View</p>
                                  <FontAwesomeIcon icon={faFileMedical} size='lg'/>
                                </Button>

                            </CardContent>  
                          </Card>

                          <Card>
                            <CardContent className=' flex pl-2 pb-0 pr-2 justify-between items-center'>
                             
                              <div className='flex justify-between items-center'>
                                <div className='flex items-center '>
                                
                                  <FontAwesomeIcon icon={faClipboardUser} className='size-6 justify-center text-gray-400'/>
                                  <div>
                                    <p className='pl-2 text-xl font-semibold text-sky-300'> Study 2</p>
                                    <p className='pl-2 text-sm font-semibold text-gray-400'>Last updated: </p>
                                  </div>
                                </div>
                              </div>

                              <Button size={'icon'}>
                                <FontAwesomeIcon icon={faFileMedical} size='lg'/>
                              </Button>

                            </CardContent>  
                          </Card>

                          <Card>
                            <CardContent className=' flex pl-2 pb-0 pr-2 justify-between items-center'>
                             
                              <div className='flex justify-between items-center'>
                                <div className='flex items-center '>
                                
                                  <FontAwesomeIcon icon={faClipboardUser} className='size-6 justify-center text-gray-400'/>
                                  <div>
                                    <p className='pl-2 text-xl font-semibold text-sky-300'> Study 3</p>
                                    <p className='pl-2 text-sm font-semibold text-gray-400'>Last updated: </p>
                                  </div>
                                </div>
                              </div>

                                <Button size={'icon'}>
                                  <FontAwesomeIcon icon={faFileMedical} size='lg'/>
                                </Button>

                            </CardContent>  
                          </Card>

                          <Card>
                            <CardContent className=' flex pl-2 pb-0 pr-2 justify-between items-center'>
                             
                              <div className='flex justify-between items-center'>
                                <div className='flex items-center '>
                                
                                  <FontAwesomeIcon icon={faClipboardUser} className='size-6 justify-center text-gray-400'/>
                                  <div>
                                    <p className='pl-2 text-xl font-semibold text-sky-300'> Study 4</p>
                                    <p className='pl-2 text-sm font-semibold text-gray-400'>Last updated: </p>
                                  </div>
                                </div>
                              </div>

                                <Button size={'icon'}>
                                  <FontAwesomeIcon icon={faFileMedical} size='lg'/>
                                </Button>

                            </CardContent>  
                          </Card>
                          
                          <Card>
                            <CardContent className=' flex pl-2 pr-2 pb-0 justify-between items-center'>
                             
                              <div className='flex justify-between items-center'>
                                <div className='flex items-center '>
                                
                                  <FontAwesomeIcon icon={faClipboardUser} className='size-6 justify-center text-gray-400'/>
                                  <div>
                                    <p className='pl-2 text-xl font-semibold text-sky-300'> Study 5</p>
                                    <p className='pl-2 text-sm font-semibold text-gray-400'>Last updated: </p>
                                  </div>
                                </div>
                              </div>

                                <Button size={'icon'}>
                                  <FontAwesomeIcon icon={faFileMedical} size='lg'/>
                                </Button>

                            </CardContent>  
                          </Card>

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
