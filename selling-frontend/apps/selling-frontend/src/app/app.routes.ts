import { Route } from '@angular/router';
import { adminGuard } from '@selling-frontend/domain';

export const appRoutes: Route[] = [
  {
    path: 'admin',
    canActivate: [adminGuard],
    loadChildren: () => import('@selling-frontend/selling').then((c) => c.sellingRoutingShellRoutes),
  },
  {
    path: '',
    loadChildren: () => import('@selling-frontend/shop').then((c) => c.shopRoutes),
  },
];
