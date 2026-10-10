import { Component, HostListener, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { filter } from 'rxjs';
import {
  AuthService,
  CartService,
  CategoryService,
  FavoritesService,
  PricePipe,
  ToastService,
} from '@selling-frontend/domain';
import { SellingIconComponent } from '@selling-frontend/shared';

@Component({
  selector: 'shop-layout',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, PricePipe, SellingIconComponent],
  templateUrl: './shop-layout.component.html',
  styleUrl: './shop-layout.component.scss',
})
export class ShopLayoutComponent {
  readonly auth = inject(AuthService);
  readonly cart = inject(CartService);
  readonly favorites = inject(FavoritesService);
  private readonly router = inject(Router);
  private readonly toasts = inject(ToastService);

  readonly categories = inject(CategoryService).categories;

  term = '';
  menuOpen = false;
  isCatalog = false;
  activeCategory: number | null = null;

  constructor() {
    this.readUrl();
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntilDestroyed()
      )
      .subscribe(() => {
        this.menuOpen = false;
        this.readUrl();
      });
  }

  @HostListener('document:click')
  @HostListener('document:keydown.escape')
  closeMenu() {
    this.menuOpen = false;
  }

  search() {
    this.router.navigate(['/catalog'], { queryParams: { q: this.term.trim() || null } });
  }

  logout() {
    this.auth.logout();
    this.toasts.success('You have been signed out');
    this.router.navigate(['/']);
  }

  firstName(realName: string): string {
    return realName.split(' ')[0];
  }

  // Keeps the search box and the highlighted category in sync with the address bar
  private readUrl() {
    const url = this.router.parseUrl(this.router.url);
    const path = url.root.children['primary']?.segments.map((segment) => segment.path).join('/') ?? '';
    this.isCatalog = path === 'catalog';
    this.term = this.isCatalog ? url.queryParams['q'] ?? '' : '';
    const category = Number(url.queryParams['category']);
    this.activeCategory = this.isCatalog && category ? category : null;
  }
}
