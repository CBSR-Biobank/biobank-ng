import { clsx, type ClassValue } from 'clsx';
import { format } from 'date-fns';
import { useEffect, useState } from 'react';
import { twMerge } from 'tailwind-merge';
import { z } from 'zod';

export const isoDateTimeOrNullSchema = z.union([
  z
    .string()
    .refine((val) => !isNaN(Date.parse(val)), {
      message: 'Invalid ISO date-time format',
    })
    .transform((val) => new Date(val)),
  z.null(),
  z.null(),
]);

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

// see https://usehooks-ts.com/react-hook/use-debounce
export function useDebounce<T>(value: T, delay?: number): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedValue(value), delay || 500);
    return () => {
      clearTimeout(timer);
    };
  }, [value, delay]);

  return debouncedValue;
}

export function capitalizeWord(word: string) {
  return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
}

export function getAsString(value: string | string[]): string {
  if (Array.isArray(value)) {
    return value[0];
  }

  return value;
}

export function stringToColor(string: string): string {
  let hash = 0;
  let i: number;

  /* eslint-disable no-bitwise */
  for (i = 0; i < string.length; i += 1) {
    hash = string.charCodeAt(i) + ((hash << 5) - hash);
  }

  let color = '#';

  for (i = 0; i < 3; i += 1) {
    const value = (hash >> (i * 8)) & 0xff;
    color += `00${value.toString(16)}`.slice(-2);
  }
  /* eslint-enable no-bitwise */

  return color;
}

export function stringAvatar(name: string, fontSize: number = 12, width: number = 60, height: number = 60) {
  const upper = name.toUpperCase();
  return {
    sx: {
      bgcolor: stringToColor(name),
      fontSize,
      width,
      height,
    },
    children: `${upper.split(' ')[0][0]}${upper.split(' ')[1][0]}`,
  };
}

export function dateToString(date?: Date | string | null): string | null {
  if (!date) {
    return null;
  }
  if (date instanceof Date) {
    return format(date, 'yyy-MM-dd');
  }
  return date;
}

export function datesRangeToString(startDate: Date, endDate: Date | null): string {
  const dayStart = startDate.getDate();
  const monthStart = startDate.toLocaleString('default', { month: 'long' });
  const yearStart = startDate.getFullYear();

  if (!endDate) {
    return `${dayStart} ${monthStart}, ${yearStart} - Present`;
  }

  const dayEnd = endDate.getDate();
  const monthEnd = endDate.toLocaleString('default', { month: 'long' });
  const yearEnd = endDate.getFullYear();

  let datesFormat = '';

  if (yearStart === yearEnd) {
    if (monthStart === monthEnd) {
      datesFormat = 'within-same-month';
      if (dayStart === dayEnd) {
        datesFormat = 'same-day';
      }
    } else {
      datesFormat = 'within-same-year';
    }
  } else {
    datesFormat = 'spans-years';
  }

  let dates = '';

  switch (datesFormat) {
    case 'same-day':
      dates = `${dayEnd} ${monthEnd} ${yearEnd}`;
      break;

    case 'within-same-month':
      dates = `${dayStart} - ${dayEnd} ${monthEnd}, ${yearEnd}`;
      break;

    case 'within-same-year':
      dates = `${dayStart} ${monthStart}  - ${dayEnd} ${monthEnd}, ${yearEnd}`;
      break;

    case 'spans-years':
      dates = format(startDate, 'yyyy-MM-dd') + ' to ' + format(endDate, 'yyyy-MM-dd');
      break;
  }
  return dates;
}

export const PG_ELLIPSIS = -1;

// this code inspired by https://www.freecodecamp.org/news/build-a-custom-pagination-component-in-react/
export function paginationRange(currentPage: number, totalPageCount: number): number[] {
  const siblingCount = 1;

  const range = (start: number, end: number) => {
    let length = end - start + 1;

    // Create an array of certain length and set the elements within it from
    // start value to end value.
    return Array.from({ length }, (_, idx) => idx + start);
  };

  // Pages count is determined as siblingCount + firstPage + lastPage + currentPage + 2*DOTS
  const totalPageNumbers = siblingCount + 5;

  // Case 1:
  // If the number of pages is less than the page numbers we want to show in our
  // paginationComponent, we return the range [1..totalPageCount]
  if (totalPageNumbers >= totalPageCount) {
    return range(1, totalPageCount);
  }

  // Calculate left and right sibling index and make sure they are within range 1 and totalPageCount
  const leftSiblingIndex = Math.max(currentPage - siblingCount, 1);
  const rightSiblingIndex = Math.min(currentPage + siblingCount, totalPageCount);

  // We do not show dots just when there is just one page number to be inserted between the extremes of sibling and
  // the page limits i.e 1 and totalPageCount. Hence we are using leftSiblingIndex > 2 and rightSiblingIndex <
  // totalPageCount - 2
  const shouldShowLeftDots = leftSiblingIndex > 2;
  const shouldShowRightDots = rightSiblingIndex < totalPageCount - 2;

  const firstPageIndex = 1;
  const lastPageIndex = totalPageCount;

  // Case 2: No left dots to show, but rights dots to be shown
  if (!shouldShowLeftDots && shouldShowRightDots) {
    let leftItemCount = 3 + 2 * siblingCount;
    let leftRange = range(1, leftItemCount);

    return [...leftRange, PG_ELLIPSIS, totalPageCount];
  }

  // Case 3: No right dots to show, but left dots to be shown
  if (shouldShowLeftDots && !shouldShowRightDots) {
    let rightItemCount = 3 + 2 * siblingCount;
    let rightRange = range(totalPageCount - rightItemCount + 1, totalPageCount);
    return [firstPageIndex, PG_ELLIPSIS, ...rightRange];
  }

  // Case 4: Both left and right dots to be shown
  if (shouldShowLeftDots && shouldShowRightDots) {
    let middleRange = range(leftSiblingIndex, rightSiblingIndex);
    return [firstPageIndex, PG_ELLIPSIS, ...middleRange, PG_ELLIPSIS, lastPageIndex];
  }

  return [];
}
