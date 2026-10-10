import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Product } from '../models';
import { CartChange, CartService } from './cart.service';

function product(overrides: Partial<Product> = {}): Product {
  return {
    id: 1,
    name: 'Laptop',
    description: null,
    price: 1000,
    availableAmount: 3,
    brand: null,
    imageUrl: null,
    categoryId: null,
    categoryName: null,
    categoryIcon: null,
    active: true,
    createdAt: '2026-01-01T00:00:00',
    averageRating: 0,
    reviewCount: 0,
    ...overrides,
  };
}

describe('CartService', () => {
  let cart: CartService;
  let http: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    cart = TestBed.inject(CartService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should count items and sum the price', () => {
    cart.add(product(), 2);
    cart.add(product({ id: 2, name: 'Cable', price: 249.5, availableAmount: 10 }));

    expect(cart.count()).toBe(3);
    expect(cart.total()).toBe(2249.5);
    expect(cart.isEmpty()).toBe(false);
  });

  it('should never hold more pieces than there are in stock', () => {
    expect(cart.add(product(), 2)).toBe(2);
    expect(cart.add(product(), 5)).toBe(1);
    expect(cart.add(product())).toBe(0);
    expect(cart.quantityOf(1)).toBe(3);

    cart.setQuantity(1, 99);
    expect(cart.quantityOf(1)).toBe(3);
    cart.setQuantity(1, 0);
    expect(cart.quantityOf(1)).toBe(1);
  });

  it('should not add a sold out product', () => {
    expect(cart.add(product({ availableAmount: 0 }))).toBe(0);
    expect(cart.isEmpty()).toBe(true);
  });

  it('should put a removed item back to its place', () => {
    cart.add(product());
    cart.add(product({ id: 2, name: 'Cable' }));
    const [first] = cart.items();

    cart.remove(1);
    expect(cart.items().map((item) => item.productId)).toEqual([2]);
    cart.restore(first, 0);
    expect(cart.items().map((item) => item.productId)).toEqual([1, 2]);
  });

  it('should keep the cart after the page is reloaded', () => {
    cart.add(product(), 2);
    TestBed.flushEffects();

    TestBed.resetTestingModule();
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });

    expect(TestBed.inject(CartService).quantityOf(1)).toBe(2);
  });

  it('should follow price and stock changes on refresh', () => {
    cart.add(product(), 3);
    cart.add(product({ id: 2, name: 'Cable' }));
    cart.add(product({ id: 3, name: 'Mouse' }));
    let changes: CartChange[] = [];

    cart.refresh((result) => (changes = result));
    http
      .expectOne((request) => request.url.endsWith('/products/batch'))
      .flush([
        product({ price: 900, availableAmount: 2 }),
        product({ id: 2, name: 'Cable', availableAmount: 0 }),
        // The third product is missing: it was removed from the shop
      ]);

    expect(cart.items()).toHaveLength(1);
    expect(cart.quantityOf(1)).toBe(2);
    expect(cart.total()).toBe(1800);
    expect(changes).toEqual([
      { name: 'Laptop', reason: 'reduced' },
      { name: 'Cable', reason: 'removed' },
      { name: 'Mouse', reason: 'removed' },
    ]);
  });
});
