import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService, ToastService } from '@selling-frontend/domain';
import { SellingIconComponent } from '@selling-frontend/shared';

@Component({
  selector: 'selling-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, SellingIconComponent],
  template: `
    <header class="admin-header">
      <a routerLink="/admin" class="admin-logo">e<span>·</span>shop <small>admin</small></a>
      <div class="admin-header-actions">
        <a routerLink="/" class="admin-header-link"><selling-icon name="arrow-left" [size]="16"></selling-icon> Back to shop</a>
        <span class="admin-user">{{ auth.user()?.realName }}</span>
        <button type="button" class="admin-header-link" (click)="logout()">
          <selling-icon name="logout" [size]="16"></selling-icon> Sign out
        </button>
      </div>
    </header>

    <div class="admin-body">
      <nav class="admin-nav" aria-label="Administration">
        <a routerLink="/admin" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">
          <selling-icon name="chart"></selling-icon><span>Dashboard</span>
        </a>
        <a routerLink="/admin/orders" routerLinkActive="active"><selling-icon name="package"></selling-icon><span>Orders</span></a>
        <a routerLink="/admin/products" routerLinkActive="active"><selling-icon name="grid"></selling-icon><span>Products</span></a>
        <a routerLink="/admin/categories" routerLinkActive="active"><selling-icon name="tag"></selling-icon><span>Categories</span></a>
        <a routerLink="/admin/users" routerLinkActive="active"><selling-icon name="users"></selling-icon><span>Users</span></a>
      </nav>
      <main class="admin-main">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styleUrl: './selling-layout.component.scss',
})
export class SellingLayoutComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toasts = inject(ToastService);

  logout() {
    this.auth.logout();
    this.toasts.success('You have been signed out');
    this.router.navigate(['/']);
  }
}
