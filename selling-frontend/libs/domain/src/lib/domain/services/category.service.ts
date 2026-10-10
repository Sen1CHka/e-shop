import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Category, CategoryEdit } from '../models';
import { API_URL } from './api';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private readonly http = inject(HttpClient);
  private readonly basePath = API_URL + '/categories';

  /** Categories are needed on almost every page, so they are loaded once and shared */
  readonly categories = signal<Category[]>([]);

  constructor() {
    this.reload();
  }

  reload() {
    this.http.get<Category[]>(this.basePath).subscribe({
      next: (categories) => this.categories.set(categories),
      error: () => undefined,
    });
  }

  create(category: CategoryEdit): Observable<Category> {
    return this.http.post<Category>(this.basePath, category).pipe(tap(() => this.reload()));
  }

  update(id: number, category: CategoryEdit): Observable<Category> {
    return this.http.put<Category>(`${this.basePath}/${id}`, category).pipe(tap(() => this.reload()));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.basePath}/${id}`).pipe(tap(() => this.reload()));
  }
}
