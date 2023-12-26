import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent, SellingDialogComponent, SellingTableComponent } from '@selling-frontend/shared';
import { ColumnsDefinition, Order, OrderService } from '@selling-frontend/domain';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { getOrdersColumnsDefinition, getProductsColumnsDefinition } from './selling-orders.columns-definition';

@Component({
  selector: 'selling-orders',
  standalone: true,
  imports: [CommonModule, SellingButtonComponent, SellingTableComponent, SellingDialogComponent],
  templateUrl: './selling-orders.component.html',
  styleUrl: './selling-orders.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingOrdersComponent implements OnInit{

  isDialogVisible$ = new BehaviorSubject(false);

  constructor(private readonly orderService: OrderService){}

  
  // mockOrder: Order = {
  //   id: 1,
  //   name: 'Order #12345',
  //   userId: 'user123',
  //   userName: 'John Doe',
  //   state: 'Pending',
  //   totalPrice: 150.99,
  //   createdDate: new Date('2023-01-01T10:30:00'),
  //   products: [
  //     { id: 101, name: 'Product A', description: 'Description_1', price: 50.99, amount: 1 },
  //     { id: 102, name: 'Product B', description: 'Description_2', price: 30.50, amount: 2 },
  //     { id: 103, name: 'Product C', description: 'Description_3', price: 69.50, amount: 3 },
  //   ],
  // };

  data?: Order[];

  // products = [
  //   { id: 101, name: 'Product A', description: 'Description_1', price: 50.99, amount: 1 },
  //   { id: 102, name: 'Product B', description: 'Description_2', price: 30.50, amount: 2 },
  //   { id: 103, name: 'Product C', description: 'Description_3', price: 69.50, amount: 3 },
  // ];

  ordersColumnsDefinition!: ColumnsDefinition[];
  productsColumnsDefinition!: ColumnsDefinition[];

  //constructor(private readonly orderService: OrderService){ }

  ngOnInit(): void {
    this.orderService.getAll().subscribe((orders) => this.data = orders);
    debugger;
    this.ordersColumnsDefinition = getOrdersColumnsDefinition({
      makeDialogVisible: () => {
        this.isDialogVisible$.next(!this.isDialogVisible$.value);
      }
    });
    this.productsColumnsDefinition = getProductsColumnsDefinition();
  }

  closeDialog(value: boolean){
    this.isDialogVisible$.next(value);
  }
}
