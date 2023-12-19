import { Route } from '@angular/router';

export const sellingRoutingShellRoutes: Route[] = [
  {
    path: 'home',
    loadChildren: () =>
      import('@selling-frontend/home').then((c) => c.homeRoutes),
  },
  {
    path: '*',
    redirectTo: 'home'
  },
  {
    path: '**',
    redirectTo: 'home'
  },
];
