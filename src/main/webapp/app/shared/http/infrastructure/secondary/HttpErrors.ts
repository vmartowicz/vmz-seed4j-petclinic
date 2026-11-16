import type { AxiosError } from 'axios';

interface ProblemDetail {
  detail?: string;
}

export const toError = (error: AxiosError<ProblemDetail>): Error => new Error(error.response?.data?.detail ?? error.message);
