import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService, CartChange, CartItem, CartService, PricePipe, ToastService } from '@selling-frontend/domain';
import {
  SellingEmptyStateComponent,
  SellingIconComponent,
  SellingProductImageComponent,
  SellingQuantityComponent,
} from '@selling-frontend/shared';

@Component({
  selector: 'shop-cart',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    PricePipe,
    SellingIconComponent,
    SellingProductImageComponent,
    SellingQuantityComponent,
    SellingEmptyStateComponent,
  ],
  templateUrl: './shop-cart.component.html',
  styleUrl: './shop-cart.component.scss',
})
export class ShopCartComponent implements OnInit {
  readonly cart = inject(CartService);
  readonly auth = inject(AuthService);
  private readonly toasts = inject(ToastService);

  readonly changes = signal<CartChange[]>([]);

  ngOnInit(): void {
    // Prices and stock may have changed since the products were added
    this.cart.refresh((changes) => this.changes.set(changes));
  }

  remove(item: CartItem, index: number) {
    this.cart.remove(item.productId);
    // Removing is easy to do by mistake, so it can be taken back instead of being confirmed
    this.toasts.info(`${item.name} removed from the cart`, {
      label: 'Undo',
      run: () => this.cart.restore(item, index),
    });
  }

  trackById(index: number, item: CartItem) {
    return item.productId;
  }
}
