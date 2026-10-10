import { Route } from '@angular/router';
import { authGuard, guestGuard } from '@selling-frontend/domain';
import { ShopLayoutComponent } from './shop-layout/shop-layout.component';
import { ShopHomeComponent } from './shop-home/shop-home.component';
import { ShopCatalogComponent } from './shop-catalog/shop-catalog.component';
import { ShopProductDetailComponent } from './shop-product-detail/shop-product-detail.component';
import { ShopCartComponent } from './shop-cart/shop-cart.component';
import { ShopCheckoutComponent } from './shop-checkout/shop-checkout.component';
import { ShopLoginComponent } from './shop-auth/shop-login.component';
import { ShopRegisterComponent } from './shop-auth/shop-register.component';
import { ShopAccountComponent } from './shop-account/shop-account.component';
import { ShopOrdersComponent } from './shop-account/shop-orders.component';
import { ShopOrderDetailComponent } from './shop-account/shop-order-detail.component';
import { ShopProfileComponent } from './shop-account/shop-profile.component';
import { ShopAddressesComponent } from './shop-account/shop-addresses.component';
import { ShopFavoritesComponent } from './shop-account/shop-favorites.component';
import { ShopNotFoundComponent } from './shop-not-found/shop-not-found.component';

export const shopRoutes: Route[] = [
  {
    path: '',
    component: ShopLayoutComponent,
    children: [
      { path: '', component: ShopHomeComponent, title: 'E-shop' },
      { path: 'catalog', component: ShopCatalogComponent, title: 'Products - E-shop' },
      { path: 'product/:id', component: ShopProductDetailComponent },
      { path: 'cart', component: ShopCartComponent, title: 'Cart - E-shop' },
      { path: 'checkout', component: ShopCheckoutComponent, canActivate: [authGuard], title: 'Checkout - E-shop' },
      { path: 'login', component: ShopLoginComponent, canActivate: [guestGuard], title: 'Sign in - E-shop' },
      { path: 'register', component: ShopRegisterComponent, canActivate: [guestGuard], title: 'Create account - E-shop' },
      {
        path: 'account',
        component: ShopAccountComponent,
        canActivate: [authGuard],
        children: [
          { path: '', pathMatch: 'full', redirectTo: 'orders' },
          { path: 'orders', component: ShopOrdersComponent, title: 'My orders - E-shop' },
          { path: 'orders/:id', component: ShopOrderDetailComponent, title: 'Order detail - E-shop' },
          { path: 'profile', component: ShopProfileComponent, title: 'My profile - E-shop' },
          { path: 'addresses', component: ShopAddressesComponent, title: 'My addresses - E-shop' },
          { path: 'favorites', component: ShopFavoritesComponent, title: 'Favourites - E-shop' },
        ],
      },
      { path: '**', component: ShopNotFoundComponent, title: 'Page not found - E-shop' },
    ],
  },
];
