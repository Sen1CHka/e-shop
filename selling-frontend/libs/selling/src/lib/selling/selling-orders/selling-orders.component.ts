import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent, SellingDialogComponent, SellingTableComponent } from '@selling-frontend/shared';
import { ColumnsDefinition, Order, OrderService, Product } from '@selling-frontend/domain';
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

  private readonly orderService = inject(OrderService);

  isDialogVisible$ = new BehaviorSubject(false);
  currentOrderProducts$ = new BehaviorSubject<Product[]>([]);
  data$?: Observable<Order[]>;

  ordersColumnsDefinition!: ColumnsDefinition[];
  productsColumnsDefinition!: ColumnsDefinition[];


  ngOnInit(): void {
    this.data$ = this.orderService.getAll();
    this.ordersColumnsDefinition = getOrdersColumnsDefinition({
      makeDialogVisible: (row: Order) => {
        this.isDialogVisible$.next(!this.isDialogVisible$.value);
        this.currentOrderProducts$.next(row.products);
      }
    });
    this.productsColumnsDefinition = getProductsColumnsDefinition();
  }

  closeDialog(value: boolean){
    this.isDialogVisible$.next(value);
  }
}

