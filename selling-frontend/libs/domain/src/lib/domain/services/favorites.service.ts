import { HttpClient } from '@angular/common/http';
import { Injectable, effect, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Product } from '../models';
import { API_URL, errorMessage } from './api';
import { AuthService } from './auth.service';
import { ToastService } from './toast.service';

@Injectable({ providedIn: 'root' })
export class FavoritesService {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthService);
  private readonly toasts = inject(ToastService);
  private readonly router = inject(Router);
  private readonly basePath = API_URL + '/favorites';

  /** Ids of favourite products, used to draw the hearts all over the shop */
  readonly ids = signal<ReadonlySet<number>>(new Set());

  constructor() {
    effect(
      () => {
        if (this.auth.isLoggedIn()) {
          this.getAll().subscribe({
            next: (products) => this.ids.set(new Set(products.map((product) => product.id))),
            error: () => undefined,
          });
        } else {
          this.ids.set(new Set());
        }
      },
      { allowSignalWrites: true }
    );
  }

  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.basePath);
  }

  isFavorite(productId: number): boolean {
    return this.ids().has(productId);
  }

  toggle(product: Pick<Product, 'id' | 'name'>) {
    if (!this.auth.isLoggedIn()) {
      this.toasts.info('Sign in to save your favourite products');
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }
    const wasFavorite = this.isFavorite(product.id);
    // The heart reacts immediately and is reverted if the server refuses
    this.setLocally(product.id, !wasFavorite);
    const request = wasFavorite
      ? this.http.delete<void>(`${this.basePath}/${product.id}`)
      : this.http.put<void>(`${this.basePath}/${product.id}`, null);
    request.subscribe({
      next: () =>
        this.toasts.success(
          wasFavorite ? `${product.name} removed from favourites` : `${product.name} saved to favourites`
        ),
      error: (error) => {
        this.setLocally(product.id, wasFavorite);
        this.toasts.error(errorMessage(error));
      },
    });
  }

  private setLocally(productId: number, favorite: boolean) {
    const ids = new Set(this.ids());
    if (favorite) {
      ids.add(productId);
    } else {
      ids.delete(productId);
    }
    this.ids.set(ids);
  }
}
