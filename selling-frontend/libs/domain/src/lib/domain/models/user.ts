export type Role = 'USER' | 'ADMIN';

export interface User {
  id: number;
  username: string;
  realName: string;
  email: string;
  phone: string | null;
  role: Role;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface Registration {
  username: string;
  realName: string;
  email: string;
  password: string;
}

export interface ProfileEdit {
  realName: string;
  email: string;
  phone: string | null;
}

export interface Address {
  id: number;
  fullName: string;
  street: string;
  city: string;
  zipCode: string;
  country: string;
  phone: string | null;
  defaultAddress: boolean;
}

export type AddressEdit = Omit<Address, 'id'>;
