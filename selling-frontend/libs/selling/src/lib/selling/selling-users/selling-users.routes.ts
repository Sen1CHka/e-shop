import { Route } from '@angular/router';
import { SellingUsersComponent } from './selling-users.component'

export const sellingUsersRoutes: Route[] = [
  {
    path: '',
    component: SellingUsersComponent
  },
  {
    path: '*',
    redirectTo: 'users'
  },
  {
    path: '**',
    redirectTo: 'users'
  },
];
