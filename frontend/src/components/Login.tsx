import { ApiError, login } from '@app/api/api';
import { AdminPage } from '@app/pages/AdminPage';
import { useUserStore } from '@app/store';
import { zodResolver } from '@hookform/resolvers/zod';
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { Alert } from './Alert';
import { OkButton } from './OkButton';
import { LabelledInput } from './forms/LabelledInput';

const schema = z.object({
  username: z.string().min(1, { message: 'A username is required' }),
  password: z.string().min(1, { message: 'A password is required' })
});

type FormInputs = z.infer<typeof schema>;

export function Login() {
  const navigate = useNavigate();
  const { loggedIn, setLoggedIn } = useUserStore();
  const [loginError, setLoginError] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { isValid, errors }
  } = useForm<FormInputs>({
    mode: 'all',
    resolver: zodResolver(schema),
    defaultValues: {
      username: '',
      password: ''
    }
  });

  const onSubmit = async (values: FormInputs) => {
    try {
      const result = await login(values.username, values.password);
      setLoggedIn(true);
      console.log(result);
      navigate('/');
    } catch (e) {
      const apiError = e as ApiError;
      console.log(apiError);
      if (apiError.status === 401) {
        setLoginError(true);
      }
    }
  };

  useEffect(() => {
    if (loggedIn) {
      navigate('/');
    }
  }, [loggedIn]);

  return (
    <AdminPage className="grid justify-items-center gap-8">
      <AdminPage.Title>Login</AdminPage.Title>
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
          <OkButton type="submit" disabled={!isValid} />
        </div>
      </form>
    </AdminPage>
  );
}
