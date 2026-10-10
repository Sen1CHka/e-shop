/**
 * Cart line kept in the browser. Name, price and stock are a copy of the product
 * so the cart can be drawn immediately; they are refreshed from the server.
 */
export interface CartItem {
  productId: number;
  quantity: number;
  name: string;
  price: number;
  imageUrl: string | null;
  categoryIcon: string | null;
  availableAmount: number;
}
