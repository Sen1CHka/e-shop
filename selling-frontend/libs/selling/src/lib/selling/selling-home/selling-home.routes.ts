import { Route } from '@angular/router';
import { SellingHomeComponent } from './selling-home.component';

export const sellingHomeRoutes: Route[] = [
  {
    path: '',
    component: SellingHomeComponent
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
