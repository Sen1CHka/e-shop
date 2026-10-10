import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { API_URL } from '../services/api';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const toasts = inject(ToastService);

  const token = auth.token();
  const isApiCall = request.url.startsWith(API_URL);
  if (token && isApiCall) {
    request = request.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }

  return next(request).pipe(
    catchError((error) => {
      // A rejected token means the session expired; wrong credentials on the login form are not our case
      const isLogin = request.url.endsWith('/auth/login');
      if (error instanceof HttpErrorResponse && error.status === 401 && token && isApiCall && !isLogin) {
        auth.logout();
        toasts.info('Your session has expired, please sign in again');
        router.navigate(['/login'], { queryParams: { returnUrl: router.url } });
      }
      return throwError(() => error);
    })
  );
};
