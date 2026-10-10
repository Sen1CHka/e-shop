export interface Category {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  icon: string | null;
  productCount: number;
}

export interface CategoryEdit {
  name: string;
  description: string | null;
  icon: string | null;
}

export interface Product {
  id: number;
  name: string;
  description: string | null;
  price: number;
  availableAmount: number;
  brand: string | null;
  imageUrl: string | null;
  categoryId: number | null;
  categoryName: string | null;
  categoryIcon: string | null;
  active: boolean;
  createdAt: string;
  averageRating: number;
  reviewCount: number;
}

export interface ProductEdit {
  name: string;
  description: string | null;
  price: number;
  availableAmount: number;
  brand: string | null;
  imageUrl: string | null;
  categoryId: number | null;
  active: boolean;
}

export type ProductSort = 'newest' | 'price_asc' | 'price_desc' | 'name';

export interface ProductQuery {
  q?: string | null;
  categoryId?: number | null;
  minPrice?: number | null;
  maxPrice?: number | null;
  inStock?: boolean;
  sort?: ProductSort | null;
  page?: number;
  size?: number;
}

export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Review {
  id: number;
  productId: number;
  username: string;
  realName: string;
  rating: number;
  comment: string | null;
  createdAt: string;
}

export interface ReviewEdit {
  rating: number;
  comment: string | null;
}
