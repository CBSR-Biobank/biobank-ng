import { EntityProperty } from '@app/components/entity-property';
import { useRouterUser } from '@app/pages/user/user-profile';

export function UserDetails() {
  const { user } = useRouterUser();

  return (
    <div className="bg-basic-100 border-top flex flex-col gap-8 rounded-md drop-shadow-md">
      <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
        <EntityProperty propName="username" label="Username">
          {user.username}
        </EntityProperty>
        <EntityProperty propName="fullname" label="Full Name">
          {user.fullname}
        </EntityProperty>
        <EntityProperty propName="isGlobalAdmin" label="Global Admin">
          {user.isGlobalAdmin ? 'Yes' : 'No'}
        </EntityProperty>
        <EntityProperty propName="apiKey" label="API Key">
          {user.apiKey}
        </EntityProperty>
      </div>
    </div>
  );
}
