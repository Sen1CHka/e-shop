import { Injectable } from '@angular/core';
import { Order, OrderEdit } from '../models';
import { BaseHttpService } from './baseHttp.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class OrderService extends BaseHttpService<Order> {
  constructor(override readonly http: HttpClient) {
    super(http);
  }

  override get entityName(): string {
    return 'order';
  }

  createOrder(entity: OrderEdit): Observable<number> {
    return this.http.post<number>(this.basePath, entity);
  }

  updateOrder(id: number, entity: OrderEdit): Observable<number> {
    return this.http.patch<number>(`${this.basePath}/${id}`, null, { params: { state: entity.state } });
  }

  filterAll(filter?: any): Observable<Order[]> {
    return this.http.get<Order[]>(this.basePath, { params: filter });
}
}
