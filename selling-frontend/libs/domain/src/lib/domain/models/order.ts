import { Product } from "./product";

export interface Order {
    id: number;
    name: string;
    userId: string;
    userName: string;
    state: string;
    totalPrice: number;
    createdDate: Date;
    products: Product[];
}