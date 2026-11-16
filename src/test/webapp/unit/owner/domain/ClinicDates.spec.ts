import { isoDate, nextDay } from '@/owner/domain/ClinicDates';
import { describe, expect, it } from 'vitest';

describe('Clinic dates', () => {
  it('should format date as ISO local date', () => {
    expect(isoDate(new Date(2026, 0, 5, 23, 30))).toBe('2026-01-05');
  });

  it('should get next day', () => {
    expect(nextDay(new Date(2026, 10, 15, 8))).toBe('2026-11-16');
  });

  it('should get next day over a year change', () => {
    expect(nextDay(new Date(2026, 11, 31, 23, 59))).toBe('2027-01-01');
  });
});
