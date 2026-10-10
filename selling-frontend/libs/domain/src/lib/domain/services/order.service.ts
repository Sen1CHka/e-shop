import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { CheckoutOptions, Order, OrderCreate, OrderFilter, OrderState } from '../models';
import { API_URL, toParams } from './api';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly http = inject(HttpClient);
  private readonly basePath = API_URL + '/orders';

  getCheckoutOptions(): Observable<CheckoutOptions> {
    return this.http.get<CheckoutOptions>(API_URL + '/checkout/options');
  }

  create(order: OrderCreate): Observable<Order> {
    return this.http.post<Order>(this.basePath, order);
  }

  getMine(): Observable<Order[]> {
    return this.http.get<Order[]>(this.basePath);
  }

  getById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.basePath}/${id}`);
  }

  cancel(id: number): Observable<Order> {
    return this.http.post<Order>(`${this.basePath}/${id}/cancel`, null);
  }

  filterAll(filter: OrderFilter = {}): Observable<Order[]> {
    return this.http.get<Order[]>(API_URL + '/admin/orders', { params: toParams(filter) });
  }

  updateState(id: number, state: OrderState): Observable<Order> {
    return this.http.patch<Order>(`${API_URL}/admin/orders/${id}/state`, { state });
  }
}
