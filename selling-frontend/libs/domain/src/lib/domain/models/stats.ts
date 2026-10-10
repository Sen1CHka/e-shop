import { Order, OrderState } from './order';
import { Product } from './product';

export interface Stats {
  productCount: number;
  userCount: number;
  orderCount: number;
  revenue: number;
  ordersByState: Record<OrderState, number>;
  lowStock: Product[];
  topProducts: { name: string; quantity: number }[];
  recentOrders: Order[];
}
