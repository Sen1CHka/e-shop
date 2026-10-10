import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CategoryService, Product, ProductService } from '@selling-frontend/domain';
import { SellingIconComponent, SellingProductCardComponent } from '@selling-frontend/shared';

@Component({
  selector: 'shop-home',
  standalone: true,
  imports: [CommonModule, RouterModule, SellingIconComponent, SellingProductCardComponent],
  templateUrl: './shop-home.component.html',
  styleUrl: './shop-home.component.scss',
})
export class ShopHomeComponent implements OnInit {
  private readonly productService = inject(ProductService);

  readonly categories = inject(CategoryService).categories;
  readonly newest = signal<Product[] | null>(null);
  readonly budget = signal<Product[] | null>(null);
  readonly failed = signal(false);
  readonly placeholders = [1, 2, 3, 4];

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.failed.set(false);
    this.productService.search({ sort: 'newest', inStock: true, size: 8 }).subscribe({
      next: (page) => this.newest.set(page.content),
      error: () => this.failed.set(true),
    });
    this.productService.search({ maxPrice: 1000, inStock: true, sort: 'price_asc', size: 4 }).subscribe({
      next: (page) => this.budget.set(page.content),
      error: () => undefined,
    });
  }
}
