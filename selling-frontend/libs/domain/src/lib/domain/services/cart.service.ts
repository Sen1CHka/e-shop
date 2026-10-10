import { Injectable, computed, effect, inject, signal } from '@angular/core';
import { CartItem, Product } from '../models';
import { ProductService } from './product.service';

const CART_KEY = 'eshop.cart';

export interface CartChange {
  name: string;
  reason: 'removed' | 'reduced';
}

/**
 * Shopping cart kept in the browser, so that it works without signing in
 * and survives closing the page.
 */
@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly productService = inject(ProductService);

  readonly items = signal<CartItem[]>(this.readStoredItems());

  readonly count = computed(() => this.items().reduce((sum, item) => sum + item.quantity, 0));
  readonly total = computed(() => this.items().reduce((sum, item) => sum + item.price * item.quantity, 0));
  readonly isEmpty = computed(() => this.items().length === 0);

  constructor() {
    effect(() => localStorage.setItem(CART_KEY, JSON.stringify(this.items())));
  }

  quantityOf(productId: number): number {
    return this.items().find((item) => item.productId === productId)?.quantity ?? 0;
  }

  /**
   * Adds the product and returns how many pieces were really added;
   * the cart never holds more than what is in stock.
   */
  add(product: Product, quantity = 1): number {
    const inCart = this.quantityOf(product.id);
    const added = Math.max(0, Math.min(quantity, product.availableAmount - inCart));
    if (added === 0) {
      return 0;
    }
    if (inCart > 0) {
      this.setQuantity(product.id, inCart + added);
    } else {
      this.items.update((items) => [...items, { ...this.toItem(product), quantity: added }]);
    }
    return added;
  }

  setQuantity(productId: number, quantity: number) {
    this.items.update((items) =>
      items.map((item) =>
        item.productId === productId
          ? { ...item, quantity: Math.max(1, Math.min(quantity, Math.max(item.availableAmount, 1))) }
          : item
      )
    );
  }

  remove(productId: number) {
    this.items.update((items) => items.filter((item) => item.productId !== productId));
  }

  restore(item: CartItem, index: number) {
    this.items.update((items) => [...items.slice(0, index), item, ...items.slice(index)]);
  }

  clear() {
    this.items.set([]);
  }

  /**
   * Loads current prices and stock. Products that are no longer sold are removed
   * and quantities above the stock are lowered; the changes are returned so they can be announced.
   */
  refresh(onDone?: (changes: CartChange[]) => void) {
    const ids = this.items().map((item) => item.productId);
    if (ids.length === 0) {
      onDone?.([]);
      return;
    }
    this.productService.getByIds(ids).subscribe({
      next: (products) => {
        const changes: CartChange[] = [];
        const refreshed: CartItem[] = [];
        for (const item of this.items()) {
          const product = products.find((p) => p.id === item.productId);
          if (!product || !product.active || product.availableAmount === 0) {
            changes.push({ name: item.name, reason: 'removed' });
            continue;
          }
          const quantity = Math.min(item.quantity, product.availableAmount);
          if (quantity < item.quantity) {
            changes.push({ name: item.name, reason: 'reduced' });
          }
          refreshed.push({ ...this.toItem(product), quantity });
        }
        this.items.set(refreshed);
        onDone?.(changes);
      },
      error: () => onDone?.([]),
    });
  }

  private toItem(product: Product): Omit<CartItem, 'quantity'> {
    return {
      productId: product.id,
      name: product.name,
      price: product.price,
      imageUrl: product.imageUrl,
      categoryIcon: product.categoryIcon,
      availableAmount: product.availableAmount,
    };
  }

  private readStoredItems(): CartItem[] {
    try {
      const stored = JSON.parse(localStorage.getItem(CART_KEY) ?? '[]');
      return Array.isArray(stored) ? stored : [];
    } catch {
      return [];
    }
  }
}
