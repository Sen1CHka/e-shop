import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Role, Stats, User } from '../models';
import { API_URL } from './api';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);
  private readonly basePath = API_URL + '/admin';

  getStats(): Observable<Stats> {
    return this.http.get<Stats>(this.basePath + '/stats');
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.basePath + '/users');
  }

  changeRole(userId: number, role: Role): Observable<User> {
    return this.http.patch<User>(`${this.basePath}/users/${userId}/role`, { role });
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.basePath}/users/${userId}`);
  }
}
