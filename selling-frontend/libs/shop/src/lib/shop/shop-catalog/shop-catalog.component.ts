import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Params, Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import {
  CategoryService,
  Page,
  Product,
  ProductService,
  ProductSort,
  SORT_OPTIONS,
  formatPrice,
} from '@selling-frontend/domain';
import {
  SellingEmptyStateComponent,
  SellingIconComponent,
  SellingPaginationComponent,
  SellingProductCardComponent,
} from '@selling-frontend/shared';

const PAGE_SIZE = 12;

/** Filters as they are written in the address bar, so every result can be shared or bookmarked */
interface CatalogFilter {
  q: string | null;
  category: number | null;
  min: number | null;
  max: number | null;
  stock: boolean;
  sort: ProductSort;
  page: number;
}

interface Chip {
  label: string;
  clear: Params;
}

@Component({
  selector: 'shop-catalog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SellingIconComponent,
    SellingProductCardComponent,
    SellingPaginationComponent,
    SellingEmptyStateComponent,
  ],
  templateUrl: './shop-catalog.component.html',
  styleUrl: './shop-catalog.component.scss',
})
export class ShopCatalogComponent {
  private readonly productService = inject(ProductService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private request?: Subscription;

  readonly categories = inject(CategoryService).categories;
  readonly sortOptions = SORT_OPTIONS;
  readonly placeholders = [1, 2, 3, 4, 5, 6];

  readonly filter = signal<CatalogFilter>({ q: null, category: null, min: null, max: null, stock: false, sort: 'newest', page: 0 });
  readonly page = signal<Page<Product> | null>(null);
  readonly isLoading = signal(false);
  readonly failed = signal(false);

  readonly category = computed(() => this.categories().find((category) => category.id === this.filter().category) ?? null);

  readonly chips = computed<Chip[]>(() => {
    const filter = this.filter();
    const chips: Chip[] = [];
    if (filter.q) {
      chips.push({ label: `Search: ${filter.q}`, clear: { q: null } });
    }
    const category = this.category();
    if (category) {
      chips.push({ label: category.name, clear: { category: null } });
    }
    if (filter.min !== null) {
      chips.push({ label: `From ${formatPrice(filter.min)}`, clear: { min: null } });
    }
    if (filter.max !== null) {
      chips.push({ label: `Up to ${formatPrice(filter.max)}`, clear: { max: null } });
    }
    if (filter.stock) {
      chips.push({ label: 'In stock only', clear: { stock: null } });
    }
    return chips;
  });

  minPrice: number | null = null;
  maxPrice: number | null = null;
  priceError = '';
  filtersOpen = false;

  constructor() {
    this.route.queryParamMap.pipe(takeUntilDestroyed()).subscribe((params) => {
      const number = (name: string) => {
        const value = Number(params.get(name));
        return params.get(name) !== null && Number.isFinite(value) && value >= 0 ? value : null;
      };
      const sort = params.get('sort') as ProductSort;
      this.filter.set({
        q: params.get('q')?.trim() || null,
        category: number('category'),
        min: number('min') || null,
        max: number('max'),
        stock: params.get('stock') === '1',
        sort: SORT_OPTIONS.some((option) => option.code === sort) ? sort : 'newest',
        page: number('page') ?? 0,
      });
      this.minPrice = this.filter().min;
      this.maxPrice = this.filter().max;
      this.priceError = '';
      this.load();
    });
  }

  load() {
    const filter = this.filter();
    this.isLoading.set(true);
    this.failed.set(false);
    // A newer filter always wins over a response that is still on its way
    this.request?.unsubscribe();
    this.request = this.productService
      .search({
        q: filter.q,
        categoryId: filter.category,
        minPrice: filter.min,
        maxPrice: filter.max,
        inStock: filter.stock,
        sort: filter.sort,
        page: filter.page,
        size: PAGE_SIZE,
      })
      .subscribe({
        next: (page) => {
          this.page.set(page);
          this.isLoading.set(false);
        },
        error: () => {
          this.page.set(null);
          this.isLoading.set(false);
          this.failed.set(true);
        },
      });
  }

  /** Changes the given query parameters; any change of a filter returns to the first page */
  update(params: Params, resetPage = true) {
    this.filtersOpen = false;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: resetPage ? { ...params, page: null } : params,
      queryParamsHandling: 'merge',
    });
  }

  applyPrice() {
    if (this.minPrice !== null && this.maxPrice !== null && this.minPrice > this.maxPrice) {
      this.priceError = 'The lower price cannot be higher than the upper one';
      return;
    }
    if ((this.minPrice ?? 0) < 0 || (this.maxPrice ?? 0) < 0) {
      this.priceError = 'The price cannot be negative';
      return;
    }
    this.update({ min: this.minPrice || null, max: this.maxPrice });
  }

  clearAll() {
    this.filtersOpen = false;
    this.router.navigate(['/catalog'], { queryParams: { sort: this.filter().sort === 'newest' ? null : this.filter().sort } });
  }

  trackById(index: number, product: Product) {
    return product.id;
  }
}
