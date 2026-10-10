export type OrderState = 'NEW' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

export interface Shipping {
  fullName: string;
  street: string;
  city: string;
  zipCode: string;
  country: string;
  phone: string;
}

export interface OrderItem {
  productId: number;
  productName: string;
  imageUrl: string | null;
  categoryIcon: string | null;
  unitPrice: number;
  quantity: number;
  lineTotal: number;
}

export interface Order {
  id: number;
  username: string;
  realName: string;
  date: string;
  state: OrderState;
  items: OrderItem[];
  itemCount: number;
  itemsPrice: number;
  deliveryMethod: string;
  deliveryLabel: string;
  deliveryPrice: number;
  paymentMethod: string;
  paymentLabel: string;
  paymentPrice: number;
  totalPrice: number;
  shipping: Shipping;
  note: string | null;
}

export interface OrderCreate {
  items: { productId: number; quantity: number }[];
  deliveryMethod: string;
  paymentMethod: string;
  shipping: Shipping;
  note: string | null;
}

export interface CheckoutOption {
  code: string;
  label: string;
  description: string;
  price: number;
}

export interface CheckoutOptions {
  deliveryMethods: CheckoutOption[];
  paymentMethods: CheckoutOption[];
}

export interface OrderFilter {
  state?: OrderState | null;
  user?: string | null;
  dateTo?: string | null;
}
