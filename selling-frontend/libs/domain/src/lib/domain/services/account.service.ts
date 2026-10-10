import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Address, AddressEdit } from '../models';
import { API_URL } from './api';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private readonly http = inject(HttpClient);
  private readonly basePath = API_URL + '/addresses';

  getAddresses(): Observable<Address[]> {
    return this.http.get<Address[]>(this.basePath);
  }

  createAddress(address: AddressEdit): Observable<Address> {
    return this.http.post<Address>(this.basePath, address);
  }

  updateAddress(id: number, address: AddressEdit): Observable<Address> {
    return this.http.put<Address>(`${this.basePath}/${id}`, address);
  }

  deleteAddress(id: number): Observable<void> {
    return this.http.delete<void>(`${this.basePath}/${id}`);
  }
}
