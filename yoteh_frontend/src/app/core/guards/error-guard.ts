import { CanActivateFn } from '@angular/router';

export const errorGuard: CanActivateFn = (route, state) => {
  return true;
};
