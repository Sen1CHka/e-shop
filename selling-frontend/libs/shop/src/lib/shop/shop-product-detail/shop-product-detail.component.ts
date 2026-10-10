import { Component, ElementRef, Input, ViewChild, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router, RouterModule } from '@angular/router';
import {
  AuthService,
  CartService,
  ConfirmService,
  FavoritesService,
  LOW_STOCK_LIMIT,
  PricePipe,
  Product,
  ProductService,
  Review,
  ToastService,
  errorMessage,
} from '@selling-frontend/domain';
import {
  SellingEmptyStateComponent,
  SellingIconComponent,
  SellingProductCardComponent,
  SellingProductImageComponent,
  SellingQuantityComponent,
  SellingRatingComponent,
  StockInfo,
  addToCartWithFeedback,
  stockInfo,
} from '@selling-frontend/shared';

@Component({
  selector: 'shop-product-detail',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    PricePipe,
    SellingIconComponent,
    SellingProductImageComponent,
    SellingProductCardComponent,
    SellingRatingComponent,
    SellingQuantityComponent,
    SellingEmptyStateComponent,
  ],
  templateUrl: './shop-product-detail.component.html',
  styleUrl: './shop-product-detail.component.scss',
})
export class ShopProductDetailComponent {
  readonly auth = inject(AuthService);
  readonly favorites = inject(FavoritesService);
  private readonly productService = inject(ProductService);
  private readonly cart = inject(CartService);
  private readonly toasts = inject(ToastService);
  private readonly confirm = inject(ConfirmService);
  private readonly router = inject(Router);
  private readonly title = inject(Title);

  @ViewChild('reviewsSection')
  reviewsSection?: ElementRef<HTMLElement>;

  readonly lowStockLimit = LOW_STOCK_LIMIT;
  readonly product = signal<Product | null>(null);
  readonly reviews = signal<Review[] | null>(null);
  readonly related = signal<Product[]>([]);
  readonly notFound = signal(false);

  readonly myReview = computed(
    () => this.reviews()?.find((review) => review.username === this.auth.user()?.username) ?? null
  );

  quantity = 1;
  rating = 0;
  comment = '';
  ratingMissing = false;
  isSavingReview = false;

  // Filled by the router from the ":id" part of the address
  @Input()
  set id(id: string) {
    this.load(Number(id));
  }

  stock(product: Product): StockInfo {
    return stockInfo(product.availableAmount);
  }

  inCart(product: Product): number {
    return this.cart.quantityOf(product.id);
  }

  isFavorite(product: Product): boolean {
    return this.favorites.isFavorite(product.id);
  }

  addToCart(product: Product) {
    addToCartWithFeedback(this.cart, this.toasts, this.router, product, this.quantity);
    this.quantity = 1;
  }

  scrollToReviews() {
    const section = this.reviewsSection?.nativeElement;
    section?.scrollIntoView({ behavior: 'smooth' });
    section?.focus({ preventScroll: true });
  }

  saveReview(product: Product) {
    if (this.rating === 0) {
      this.ratingMissing = true;
      return;
    }
    this.isSavingReview = true;
    this.productService.saveReview(product.id, { rating: this.rating, comment: this.comment.trim() || null }).subscribe({
      next: () => {
        this.isSavingReview = false;
        this.toasts.success('Thank you, your review has been published');
        this.reload(product.id);
      },
      error: (error) => {
        this.isSavingReview = false;
        this.toasts.error(errorMessage(error));
      },
    });
  }

  async deleteReview(product: Product, review: Review) {
    const confirmed = await this.confirm.ask({
      title: 'Delete this review?',
      text: 'The rating and the text will be removed permanently.',
      confirmLabel: 'Delete review',
      danger: true,
    });
    if (!confirmed) {
      return;
    }
    this.productService.deleteReview(review.id).subscribe({
      next: () => {
        this.toasts.success('The review has been deleted');
        this.reload(product.id);
      },
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }

  private load(id: number) {
    this.product.set(null);
    this.reviews.set(null);
    this.related.set([]);
    this.notFound.set(false);
    this.quantity = 1;
    this.productService.getById(id).subscribe({
      next: (product) => {
        this.product.set(product);
        this.title.setTitle(`${product.name} - E-shop`);
        this.loadRelated(product);
      },
      error: () => this.notFound.set(true),
    });
    this.loadReviews(id);
  }

  /** After a review changes, both the list and the average on the product are outdated */
  private reload(id: number) {
    this.productService.getById(id).subscribe((product) => this.product.set(product));
    this.loadReviews(id);
  }

  private loadReviews(id: number) {
    this.productService.getReviews(id).subscribe({
      next: (reviews) => {
        this.reviews.set(reviews);
        const mine = this.myReview();
        this.rating = mine?.rating ?? 0;
        this.comment = mine?.comment ?? '';
        this.ratingMissing = false;
      },
      error: () => this.reviews.set([]),
    });
  }

  private loadRelated(product: Product) {
    if (product.categoryId === null) {
      return;
    }
    this.productService.search({ categoryId: product.categoryId, inStock: true, size: 5 }).subscribe({
      next: (page) => this.related.set(page.content.filter((other) => other.id !== product.id).slice(0, 4)),
      error: () => undefined,
    });
  }
}
