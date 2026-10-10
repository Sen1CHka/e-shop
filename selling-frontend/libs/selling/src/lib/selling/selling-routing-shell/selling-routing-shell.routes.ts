import { Route } from '@angular/router';
import { SellingLayoutComponent } from '../selling-layout/selling-layout.component';
import { SellingDashboardComponent } from '../selling-dashboard/selling-dashboard.component';
import { SellingOrdersComponent } from '../selling-orders/selling-orders.component';
import { SellingProductsComponent } from '../selling-products/selling-products.component';
import { SellingCategoriesComponent } from '../selling-categories/selling-categories.component';
import { SellingUsersComponent } from '../selling-users/selling-users.component';

export const sellingRoutingShellRoutes: Route[] = [
  {
    path: '',
    component: SellingLayoutComponent,
    children: [
      { path: '', component: SellingDashboardComponent, title: 'Dashboard - E-shop admin' },
      { path: 'orders', component: SellingOrdersComponent, title: 'Orders - E-shop admin' },
      { path: 'products', component: SellingProductsComponent, title: 'Products - E-shop admin' },
      { path: 'categories', component: SellingCategoriesComponent, title: 'Categories - E-shop admin' },
      { path: 'users', component: SellingUsersComponent, title: 'Users - E-shop admin' },
      { path: '**', redirectTo: '' },
    ],
  },
];
