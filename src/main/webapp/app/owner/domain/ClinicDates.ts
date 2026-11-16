const pad = (value: number): string => value.toString().padStart(2, '0');

export const isoDate = (date: Date): string => `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;

export const nextDay = (date: Date): string => isoDate(new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1));
