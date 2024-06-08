import { useUserStore } from '@app/store';

import { Tabs } from '@app/components/ui/tabs';
import { User } from '@app/models/user';
import { TabsList, TabsTrigger } from '@radix-ui/react-tabs';
import { useEffect, useState } from 'react';
import { Outlet, useNavigate, useOutletContext } from 'react-router-dom';
import { AdminPage } from '../admin-page';

const tabs = {
  details: {
    label: 'Details',
    route: '',
  },
  studies: {
    label: 'Studies',
    route: 'studies',
  },
};

type TabKeys = keyof typeof tabs;

export function useRouterUser() {
  return useOutletContext<{ user: User }>();
}

export function UserProfile() {
  const navigate = useNavigate();
  const { user } = useUserStore();
  const [currentTab, setCurrentTab] = useState('details');

  useEffect(() => {
    const newTab =
      Object.keys(tabs).find((tabkey) => {
        const tab = tabs[tabkey as TabKeys];
        return tab.route !== '' && location.pathname.includes(tab.route);
      }) || 'details';
    setCurrentTab(newTab);
  }, [location.pathname]);

  const handleTabChange = (value: string) => {
    setCurrentTab(value);
    navigate(tabs[value as TabKeys].route);
  };

  if (!user) {
    return <></>;
  }

  return (
    <AdminPage>
      <AdminPage.Title>
        <p className="text-sm font-semibold text-gray-400">User Profile</p>
        <p className="text-4xl font-semibold text-sky-600">{user?.fullname}</p>
      </AdminPage.Title>

      <Tabs value={currentTab} onValueChange={handleTabChange} className="w-full border-b-2">
        <TabsList className="bg-basic-100 text-primary-600 h-auto p-0">
          {Object.entries(tabs).map(([key, value]) => (
            <TabsTrigger
              key={key}
              value={key}
              className="px-3 px-4 py-1.5 text-sm  font-semibold text-slate-600 data-[state=active]:bg-slate-200"
            >
              {value.label}
            </TabsTrigger>
          ))}
        </TabsList>
      </Tabs>

      <div className="flex flex-col gap-8 p-4">
        <Outlet context={{ user }} />
      </div>
    </AdminPage>
  );
}
