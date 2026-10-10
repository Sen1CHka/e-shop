import { HttpErrorResponse, HttpParams } from '@angular/common/http';

export const API_URL = 'http://localhost:8090/api';

/** Turns any failed request into a sentence that can be shown to the user */
export function errorMessage(error: unknown, fallback = 'Something went wrong, please try again'): string {
  if (error instanceof HttpErrorResponse) {
    if (error.status === 0) {
      return 'Cannot reach the server. Check your connection and try again.';
    }
    if (typeof error.error?.message === 'string' && error.error.message) {
      return error.error.message;
    }
  }
  return fallback;
}

/** Field name -> message map sent by the server for invalid forms */
export function fieldErrors(error: unknown): Record<string, string> {
  if (error instanceof HttpErrorResponse && error.error?.fieldErrors) {
    return error.error.fieldErrors;
  }
  return {};
}

/** Builds query parameters, leaving out empty values */
export function toParams(query: object): HttpParams {
  let params = new HttpParams();
  for (const [key, value] of Object.entries(query)) {
    if (value !== null && value !== undefined && value !== '' && value !== false) {
      params = params.set(key, String(value));
    }
  }
  return params;
}
