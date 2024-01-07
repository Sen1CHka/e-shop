import { Route } from '@angular/router';

export const sellingRoutingShellRoutes: Route[] = [
  {
    path: 'users',
    loadChildren: () =>
      import('@selling-frontend/selling').then((c) => c.sellingUsersRoutes),
  },
  {
    path: 'orders',
    loadChildren: () =>
      import('@selling-frontend/selling').then((c) => c.sellingOrdersRoutes),
  },
  {
    path: 'products',
    loadChildren: () =>
      import('@selling-frontend/selling').then((c) => c.sellingProductsRoutes),
  },
  {
    path: '*',
    redirectTo: 'orders'
  },
  {
    path: '**',
    redirectTo: 'orders'
  },
];
