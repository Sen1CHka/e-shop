import { Product } from "./product";

export interface Order {
    id?: number;
    userId: string;
    username: string;
    state: string;
    stateId: number;
    totalPrice: number;
    date: Date;
    products: Product[];
}