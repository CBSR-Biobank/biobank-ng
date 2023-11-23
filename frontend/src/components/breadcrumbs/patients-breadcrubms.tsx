import { Patient } from '@app/models/patient';
import { capitalizeWord } from '@app/utils';
import { matchPath, useLocation } from 'react-router-dom';
import { Breadcrumbs } from './breadcrumbs';

export const PatientBreadcrumbs: React.FC<{ patient?: Patient }> = ({ patient }) => {
  const { pathname } = useLocation();

  const pathnames = pathname.split('/').filter(Boolean);
  const breadcrumbs = pathnames.map((name, index) => {
    const route = `/${pathnames.slice(0, index + 1).join('/')}`;
    const isLast = index === pathnames.length - 1;
    let label = capitalizeWord(name);

    const patientMatch = matchPath({ path: '/patients/:pnumber', end: true }, route);
    if (patientMatch && route === patientMatch.pathname) {
      if (!patient) {
        throw new Error('patient is invalid');
      }

      label = `${patient.studyNameShort}: Patient ${patient.pnumber}`;
    }

    return { label, route, isLast };
  });

  return <Breadcrumbs crumbs={breadcrumbs} />;
};
