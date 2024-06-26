import { ApiError, login } from '@app/api/api';
import { AdminPage } from '@app/pages/admin-page';
import { useUserStore } from '@app/store';

import { zodResolver } from '@hookform/resolvers/zod';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Navigate, useNavigate } from 'react-router-dom';
import { z } from 'zod';

import { LabelledInput } from './forms/labelled-input';
import { Alert } from './ui/alert';
import { Button } from './ui/button';

const schema = z.object({
  username: z.string().min(1, { message: 'A username is required' }),
  password: z.string().min(1, { message: 'A password is required' }),
});

type FormInputs = z.infer<typeof schema>;

export function Login() {
  const navigate = useNavigate();
  const { loggedIn, user } = useUserStore();
  const [loginError, setLoginError] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { isValid, errors },
  } = useForm<FormInputs>({
    mode: 'all',
    resolver: zodResolver(schema),
    defaultValues: {
      username: '',
      password: '',
    },
  });

  const onSubmit = async (values: FormInputs) => {
    try {
      const user = await login(values.username, values.password);
      if (user) {
        navigate('/');
      }
    } catch (e) {
      const apiError = e as ApiError;
      if (apiError.status === 401) {
        setLoginError(true);
      }
    }
  };

  if (loggedIn && user !== undefined) {
    return <Navigate to="/" />;
  }

  return (
    <AdminPage className="grid h-screen content-center justify-items-center gap-8">
      <AdminPage.Title className="flex items-center gap-2 font-bold tracking-tight">CBSR Biobank Login</AdminPage.Title>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="grid grid-cols-1 gap-6 sm:w-[450px]">
          <LabelledInput label="Username" errorMessage={errors?.username?.message} {...register('username')} />
          <LabelledInput
            type="password"
            label="Password"
            errorMessage={errors?.password?.message}
            {...register('password')}
          />
          {loginError && (
            <div className="flex items-center justify-center">
              <Alert variant="destructive" className="border-2 border-red-600 bg-red-200 p-2 text-red-600">
                {'invalid username or password'}
              </Alert>
            </div>
          )}
          <div className="w-full pt-6">
            <Button type="submit" disabled={!isValid} size="full">
              OK
            </Button>
          </div>
        </div>
      </form>
    </AdminPage>
  );
}
