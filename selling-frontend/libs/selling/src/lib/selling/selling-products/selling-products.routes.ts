import { Route } from '@angular/router';
import { SellingProductsComponent } from './selling-products.component';

export const sellingProductsRoutes: Route[] = [
  {
    path: '',
    component: SellingProductsComponent
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
