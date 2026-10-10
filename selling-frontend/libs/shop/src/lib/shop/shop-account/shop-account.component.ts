import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '@selling-frontend/domain';
import { SellingIconComponent } from '@selling-frontend/shared';

@Component({
  selector: 'shop-account',
  standalone: true,
  imports: [CommonModule, RouterModule, SellingIconComponent],
  template: `
    <div class="account">
      <nav class="account-nav panel" aria-label="My account">
        <div class="account-user" *ngIf="auth.user() as user">
          <strong>{{ user.realName }}</strong>
          <span>{{ user.email }}</span>
        </div>
        <a routerLink="orders" routerLinkActive="active"><selling-icon name="package"></selling-icon>My orders</a>
        <a routerLink="favorites" routerLinkActive="active"><selling-icon name="heart"></selling-icon>Favourites</a>
        <a routerLink="addresses" routerLinkActive="active"><selling-icon name="map-pin"></selling-icon>My addresses</a>
        <a routerLink="profile" routerLinkActive="active"><selling-icon name="user"></selling-icon>My profile</a>
      </nav>
      <div class="account-content">
        <router-outlet></router-outlet>
      </div>
    </div>
  `,
  styleUrl: './shop-account.component.scss',
})
export class ShopAccountComponent {
  readonly auth = inject(AuthService);
}
