import { Route } from '@angular/router';
import { SellingOrdersComponent } from './selling-orders.component';

export const sellingOrdersRoutes: Route[] = [
  {
    path: '',
    component: SellingOrdersComponent
  },
  {
    path: '*',
    redirectTo: ''
  },
  {
    path: '**',
    redirectTo: ''
  },
];
