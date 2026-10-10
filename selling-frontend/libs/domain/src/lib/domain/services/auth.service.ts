import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthResponse, ProfileEdit, Registration, User } from '../models';
import { API_URL } from './api';

const TOKEN_KEY = 'eshop.token';
const USER_KEY = 'eshop.user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly basePath = API_URL + '/auth';

  readonly token = signal<string | null>(localStorage.getItem(TOKEN_KEY));
  readonly user = signal<User | null>(this.readStoredUser());

  readonly isLoggedIn = computed(() => this.user() !== null);
  readonly isAdmin = computed(() => this.user()?.role === 'ADMIN');

  /**
   * The stored copy of the user may be outdated (role changed, account removed), so the app asks
   * the server once at start. Not done in the constructor: the HTTP interceptor needs this service.
   */
  restoreSession() {
    if (this.token()) {
      this.http.get<User>(this.basePath + '/me').subscribe({
        next: (user) => this.storeUser(user),
        error: () => undefined,
      });
    }
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(this.basePath + '/login', { username, password })
      .pipe(tap((response) => this.startSession(response)));
  }

  register(registration: Registration): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(this.basePath + '/register', registration)
      .pipe(tap((response) => this.startSession(response)));
  }

  updateProfile(profile: ProfileEdit): Observable<User> {
    return this.http.put<User>(this.basePath + '/me', profile).pipe(tap((user) => this.storeUser(user)));
  }

  changePassword(currentPassword: string, newPassword: string): Observable<void> {
    return this.http.put<void>(this.basePath + '/me/password', { currentPassword, newPassword });
  }

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.token.set(null);
    this.user.set(null);
  }

  private startSession(response: AuthResponse) {
    localStorage.setItem(TOKEN_KEY, response.token);
    this.token.set(response.token);
    this.storeUser(response.user);
  }

  private storeUser(user: User) {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.user.set(user);
  }

  private readStoredUser(): User | null {
    try {
      const stored = localStorage.getItem(USER_KEY);
      return stored && localStorage.getItem(TOKEN_KEY) ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  }
}
