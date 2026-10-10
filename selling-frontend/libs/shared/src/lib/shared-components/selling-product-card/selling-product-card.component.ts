import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { CartService, FavoritesService, LOW_STOCK_LIMIT, PricePipe, Product, ToastService } from '@selling-frontend/domain';
import { SellingIconComponent } from '../selling-icon';
import { SellingProductImageComponent } from '../selling-product-image';
import { SellingRatingComponent } from '../selling-rating';

export interface StockInfo {
  label: string;
  cssClass: string;
}

export function stockInfo(availableAmount: number): StockInfo {
  if (availableAmount === 0) {
    return { label: 'Sold out', cssClass: 'stock-out' };
  }
  if (availableAmount <= LOW_STOCK_LIMIT) {
    return { label: availableAmount === 1 ? 'Last piece in stock' : `Last ${availableAmount} pcs in stock`, cssClass: 'stock-low' };
  }
  return { label: 'In stock', cssClass: 'stock-ok' };
}

/** Adds to the cart and tells the user what happened; shared by the card and the product page */
export function addToCartWithFeedback(cart: CartService, toasts: ToastService, router: Router, product: Product, quantity = 1) {
  const added = cart.add(product, quantity);
  const showCart = { label: 'Show cart', run: () => router.navigate(['/cart']) };
  if (added === 0) {
    toasts.info(`You already have all available pieces of ${product.name} in your cart`, showCart);
  } else if (added < quantity) {
    toasts.info(`Only ${added} more pcs of ${product.name} were available and added to your cart`, showCart);
  } else {
    toasts.success(`${product.name} added to your cart`, showCart);
  }
}

@Component({
  selector: 'selling-product-card',
  standalone: true,
  imports: [CommonModule, RouterModule, PricePipe, SellingIconComponent, SellingProductImageComponent, SellingRatingComponent],
  templateUrl: './selling-product-card.component.html',
  styleUrl: './selling-product-card.component.scss',
})
export class SellingProductCardComponent {
  @Input({ required: true })
  product!: Product;

  readonly favorites = inject(FavoritesService);
  private readonly cart = inject(CartService);
  private readonly toasts = inject(ToastService);
  private readonly router = inject(Router);

  get isFavorite(): boolean {
    return this.favorites.isFavorite(this.product.id);
  }

  get stock(): StockInfo {
    return stockInfo(this.product.availableAmount);
  }

  addToCart() {
    addToCartWithFeedback(this.cart, this.toasts, this.router, this.product);
  }
}
