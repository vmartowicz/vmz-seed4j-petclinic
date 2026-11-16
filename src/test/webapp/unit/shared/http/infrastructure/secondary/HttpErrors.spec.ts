import { toError } from '@/shared/http/infrastructure/secondary/HttpErrors';
import type { AxiosError } from 'axios';
import { describe, expect, it } from 'vitest';

const axiosError = (error: Partial<AxiosError<{ detail?: string }>>): AxiosError<{ detail?: string }> =>
  ({ message: 'Request failed', ...error }) as AxiosError<{ detail?: string }>;

describe('Http errors', () => {
  it('should use problem detail', () => {
    expect(toError(axiosError({ response: { data: { detail: 'Owner is unknown' } } as never })).message).toBe('Owner is unknown');
  });

  it('should use error message without problem detail', () => {
    expect(toError(axiosError({ response: { data: {} } as never })).message).toBe('Request failed');
  });

  it('should use error message without response data', () => {
    expect(toError(axiosError({ response: {} as never })).message).toBe('Request failed');
  });

  it('should use error message without response', () => {
    expect(toError(axiosError({})).message).toBe('Request failed');
  });
});
