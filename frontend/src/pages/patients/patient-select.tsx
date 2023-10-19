import { Button } from '@app/components/ui/button';
import { Input } from '@app/components/ui/input';
import { ChangeEvent, KeyboardEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export function PatientSelect() {
  const navigate = useNavigate();
  const [input, setInput] = useState('');

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    setInput(event.target.value);
  };
  const handleInputKeyUp = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      handleSubmit(undefined);
    }
  };

  const handleSubmit = (e?: React.SyntheticEvent) => {
    if (e) {
      e.preventDefault();
    }
    if (input.trim() !== '') {
      navigate(`/patients/${input}`);
    }
  };

  return (
    <form className="flex w-full max-w-sm items-center space-x-2" onClick={handleSubmit} autoComplete="on">
      <Input placeholder="Patient Number" onChange={handleInputChange} onKeyUp={handleInputKeyUp} />
      <Button type="submit" className="bg-sky-500 hover:bg-sky-400">
        Submit
      </Button>
    </form>
  );
}
