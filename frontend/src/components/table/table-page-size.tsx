import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@app/components/ui/select';

export const TablePageSize: React.FC<{ size: number; onValueChange: (size: number) => void }> = ({
  size,
  onValueChange,
}) => {
  const handleChange = (size: string) => {
    onValueChange(parseInt(size));
  };

  return (
    <Select defaultValue={`${size}`} onValueChange={handleChange}>
      <SelectTrigger className="w-[15ch]">
        <SelectValue placeholder="Rows" />
      </SelectTrigger>
      <SelectContent>
        <SelectGroup>
          <SelectLabel>Rows per Page</SelectLabel>
          <SelectItem value="20">20 Rows</SelectItem>
          <SelectItem value="30">30 Rows</SelectItem>
          <SelectItem value="50">50 Rows</SelectItem>
        </SelectGroup>
      </SelectContent>
    </Select>
  );
};
