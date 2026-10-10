import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FavoritesService, Product } from '@selling-frontend/domain';
import { SellingEmptyStateComponent, SellingProductCardComponent } from '@selling-frontend/shared';

@Component({
  selector: 'shop-favorites',
  standalone: true,
  imports: [CommonModule, RouterModule, SellingProductCardComponent, SellingEmptyStateComponent],
  template: `
    <h1 class="page-title">Favourites</h1>

    <ng-container *ngIf="loaded(); else loading">
      <div class="panel" *ngIf="visible().length === 0">
        <selling-empty-state
          icon="heart"
          heading="No favourite products yet"
          text="Tap the heart on any product to keep it here for later."
        >
          <a routerLink="/catalog" class="btn btn-primary">Browse products</a>
        </selling-empty-state>
      </div>
      <div class="product-grid">
        <selling-product-card *ngFor="let product of visible()" [product]="product"></selling-product-card>
      </div>
    </ng-container>
    <ng-template #loading><div class="skeleton" style="height: 22rem"></div></ng-template>
  `,
})
export class ShopFavoritesComponent implements OnInit {
  private readonly favorites = inject(FavoritesService);

  private readonly products = signal<Product[]>([]);
  readonly loaded = signal(false);

  // A product disappears from the page as soon as its heart is switched off
  readonly visible = computed(() => this.products().filter((product) => this.favorites.ids().has(product.id)));

  ngOnInit(): void {
    this.favorites.getAll().subscribe({
      next: (products) => {
        this.products.set(products);
        this.loaded.set(true);
      },
      error: () => this.loaded.set(true),
    });
  }
}
