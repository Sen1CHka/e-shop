import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Page, Product, ProductEdit, ProductQuery, Review, ReviewEdit } from '../models';
import { API_URL, toParams } from './api';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly basePath = API_URL + '/products';

  search(query: ProductQuery = {}): Observable<Page<Product>> {
    return this.http.get<Page<Product>>(this.basePath, { params: toParams(query) });
  }

  /** Includes products hidden from the shop */
  searchAll(query: ProductQuery = {}): Observable<Page<Product>> {
    return this.http.get<Page<Product>>(API_URL + '/admin/products', { params: toParams(query) });
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.basePath}/${id}`);
  }

  getByIds(ids: number[]): Observable<Product[]> {
    return this.http.get<Product[]>(this.basePath + '/batch', { params: { ids: ids.join(',') } });
  }

  create(product: ProductEdit): Observable<Product> {
    return this.http.post<Product>(this.basePath, product);
  }

  update(id: number, product: ProductEdit): Observable<Product> {
    return this.http.put<Product>(`${this.basePath}/${id}`, product);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.basePath}/${id}`);
  }

  getReviews(productId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.basePath}/${productId}/reviews`);
  }

  saveReview(productId: number, review: ReviewEdit): Observable<Review> {
    return this.http.post<Review>(`${this.basePath}/${productId}/reviews`, review);
  }

  deleteReview(reviewId: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/reviews/${reviewId}`);
  }
}
