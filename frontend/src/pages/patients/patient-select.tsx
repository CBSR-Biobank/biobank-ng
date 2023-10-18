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
      handleSubmit();
    }
  };

  const handleSubmit = () => {
    if (input.trim() !== '') {
      navigate(`/patients/${input}`);
    }
  };

  return (
    <div className="flex w-full max-w-sm items-center space-x-2">
      <Input type="email" placeholder="Patient Number" onChange={handleInputChange} onKeyUp={handleInputKeyUp} />
      <Button type="submit" onClick={handleSubmit}>
        Submit
      </Button>
    </div>
  );
}
