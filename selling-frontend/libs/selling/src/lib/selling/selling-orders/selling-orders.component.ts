import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  SellingButtonComponent,
  SellingDialogComponent,
  SellingTableComponent,
} from '@selling-frontend/shared';
import {
  ColumnsDefinition,
  Order,
  OrderService,
  Product,
} from '@selling-frontend/domain';
import { BehaviorSubject } from 'rxjs';
import {
  getOrdersColumnsDefinition,
  getProductsColumnsDefinition,
} from './selling-orders.columns-definition';
import { OrderFilterComponent, OrderFormComponent } from './components';

@Component({
  selector: 'selling-orders',
  standalone: true,
  imports: [
    CommonModule,
    SellingButtonComponent,
    SellingTableComponent,
    SellingDialogComponent,
    OrderFormComponent,
    OrderFilterComponent
  ],
  templateUrl: './selling-orders.component.html',
  styleUrl: './selling-orders.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingOrdersComponent implements OnInit {
  private readonly orderService = inject(OrderService);

  isDialogVisible$ = new BehaviorSubject(false);
  currentOrderProducts$ = new BehaviorSubject<Product[]>([]);
  data$ = new BehaviorSubject<Order[]>([]);
  isEditDialogVisible$ = new BehaviorSubject(false);
  currentEditableOrder$ = new BehaviorSubject<Order | undefined>(undefined);

  ordersColumnsDefinition!: ColumnsDefinition[];
  productsColumnsDefinition!: ColumnsDefinition[];

  ngOnInit(): void {
    this.loadOrders();
    this.ordersColumnsDefinition = getOrdersColumnsDefinition({
      makeDialogVisible: (row: Order) => {
        this.isDialogVisible$.next(!this.isDialogVisible$.value);
        this.currentOrderProducts$.next(row.products);
      },
      deleteOrder: (row: Order) => this.deleteOrder(row),
      updateOrder: (row: Order) => {
        this.isEditDialogVisible$.next(true);
        this.currentEditableOrder$.next(row);
      },
    });
    this.productsColumnsDefinition = getProductsColumnsDefinition();
  }

  closeDialog(value: boolean) {
    this.isDialogVisible$.next(value);
  }

  updateOrders(isSaved: boolean) {
    if (isSaved) {
      this.loadOrders();
    }
  }

  createOrders() {
    this.isEditDialogVisible$.next(true);
    this.currentEditableOrder$.next(undefined);
  }

  filterData(filterObject: any) {
    this.orderService.filterAll(filterObject).subscribe(
      data => this.data$.next(data)
    );
  }

  private loadOrders() {
    this.orderService.getAll().subscribe((data) => this.data$.next(data));
  }

  private deleteOrder(row: Order) {
    if (row && row.id !== null && row.id !== undefined) {
      this.orderService.delete(row.id).subscribe(() => this.updateOrders(true));
    }
  }
}
