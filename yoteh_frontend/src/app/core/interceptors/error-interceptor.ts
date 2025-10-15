import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Une erreur est survenue';

      if (error.error instanceof ErrorEvent) {
        // Erreur côté client
        errorMessage = `Erreur: ${error.error.message}`;
      } else {
        // Erreur côté serveur
        if (error.status === 401) {
          // Non autorisé - rediriger vers login
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          router.navigate(['/auth/login']);
          errorMessage = 'Session expirée. Veuillez vous reconnecter.';
        } else if (error.status === 403) {
          errorMessage = 'Accès refusé';
        } else if (error.status === 404) {
          errorMessage = 'Ressource non trouvée';
        } else if (error.error?.message) {
          errorMessage = error.error.message;
        }
      }

      console.error('Erreur HTTP:', errorMessage);
      return throwError(() => new Error(errorMessage));
    })
  );
};