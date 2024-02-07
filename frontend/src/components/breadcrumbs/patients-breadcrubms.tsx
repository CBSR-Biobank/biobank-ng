import { usePatientStore } from '@app/store';
import { capitalizeWord } from '@app/utils';
import { matchPath, useLocation } from 'react-router-dom';
import { Breadcrumbs } from './breadcrumbs';

export const PatientBreadcrumbs: React.FC = () => {
  const { pathname } = useLocation();
  const { patient, collectionEvent } = usePatientStore();

  const pathnames = pathname.split('/').filter(Boolean);
  const breadcrumbs = pathnames.map((name, index) => {
    const route = `/${pathnames.slice(0, index + 1).join('/')}`;
    const isLast = index === pathnames.length - 1;
    let label = capitalizeWord(name);

    const patientMatch = matchPath({ path: '/patients/:pnumber', end: true }, route);
    if (patientMatch && patient && route === patientMatch.pathname) {
      label = `Patient ${patient.pnumber}`;
    }

    const ceventMatch = matchPath({ path: '/patients/:pnumber/:vnumber', end: true }, route);
    if (ceventMatch && collectionEvent && route === ceventMatch.pathname) {
      label = `Visit ${collectionEvent.vnumber}`;
    }

    return { label, route, isLast };
  });

  return <Breadcrumbs crumbs={breadcrumbs} />;
};
