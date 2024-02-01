import { useUserStore } from '@app/store';
import { AdminPage } from '../admin-page';

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
        !!! TEMPORARY !!!
        <pre>{JSON.stringify(user, null, 2)}</pre>
        {/*****************************************************************/}
      </AdminPage>
    </>
  );
}
