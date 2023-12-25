import { Route } from '@angular/router';

export const sellingRoutingShellRoutes: Route[] = [
  {
    path: 'home',
    loadChildren: () =>
      import('@selling-frontend/selling').then((c) => c.sellingHomeRoutes),
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
