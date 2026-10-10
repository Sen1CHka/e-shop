import { OrderState, ProductSort } from '../models';

export interface StateInfo {
  code: OrderState;
  label: string;
  description: string;
}

export const STATES: StateInfo[] = [
  { code: 'NEW', label: 'Received', description: 'We have received your order' },
  { code: 'PROCESSING', label: 'Being prepared', description: 'We are packing your order' },
  { code: 'SHIPPED', label: 'Shipped', description: 'The order is on its way' },
  { code: 'DELIVERED', label: 'Delivered', description: 'The order has been delivered' },
  { code: 'CANCELLED', label: 'Cancelled', description: 'The order was cancelled' },
];

/** States an order goes through when everything goes well */
export const ORDER_FLOW: OrderState[] = ['NEW', 'PROCESSING', 'SHIPPED', 'DELIVERED'];

export function stateInfo(state: OrderState): StateInfo {
  return STATES.find((s) => s.code === state) ?? STATES[0];
}

export const SORT_OPTIONS: { code: ProductSort; label: string }[] = [
  { code: 'newest', label: 'Newest first' },
  { code: 'price_asc', label: 'Price: low to high' },
  { code: 'price_desc', label: 'Price: high to low' },
  { code: 'name', label: 'Name A-Z' },
];

export const LOW_STOCK_LIMIT = 5;
