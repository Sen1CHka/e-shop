import { Component, Input, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import {
  AuthService,
  CartService,
  ConfirmService,
  ORDER_FLOW,
  Order,
  OrderService,
  OrderState,
  PricePipe,
  ProductService,
  StateInfo,
  ToastService,
  errorMessage,
  stateInfo,
} from '@selling-frontend/domain';
import {
  SellingEmptyStateComponent,
  SellingIconComponent,
  SellingOrderStateComponent,
  SellingProductImageComponent,
} from '@selling-frontend/shared';

@Component({
  selector: 'shop-order-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    PricePipe,
    SellingIconComponent,
    SellingOrderStateComponent,
    SellingProductImageComponent,
    SellingEmptyStateComponent,
  ],
  templateUrl: './shop-order-detail.component.html',
  styleUrl: './shop-order-detail.component.scss',
})
export class ShopOrderDetailComponent {
  readonly auth = inject(AuthService);
  private readonly orderService = inject(OrderService);
  private readonly productService = inject(ProductService);
  private readonly cart = inject(CartService);
  private readonly confirm = inject(ConfirmService);
  private readonly toasts = inject(ToastService);
  private readonly router = inject(Router);

  readonly flow = ORDER_FLOW;
  readonly order = signal<Order | null>(null);
  readonly notFound = signal(false);

  isPlaced = false;
  isCancelling = false;

  @Input()
  set id(id: string) {
    this.order.set(null);
    this.notFound.set(false);
    this.orderService.getById(Number(id)).subscribe({
      next: (order) => this.order.set(order),
      error: () => this.notFound.set(true),
    });
  }

  // "?placed=1" is added by the checkout to show the thank-you message
  @Input()
  set placed(value: string | undefined) {
    this.isPlaced = value === '1';
  }

  info(state: OrderState): StateInfo {
    return stateInfo(state);
  }

  /** Index of the last step of the flow the order has reached */
  reached(order: Order): number {
    return ORDER_FLOW.indexOf(order.state);
  }

  canCancel(order: Order): boolean {
    return order.state === 'NEW' || order.state === 'PROCESSING';
  }

  async cancel(order: Order) {
    const confirmed = await this.confirm.ask({
      title: `Cancel order #${order.id}?`,
      text: 'The order will not be delivered. This cannot be undone, but you can always order again.',
      confirmLabel: 'Cancel order',
      cancelLabel: 'Keep order',
      danger: true,
    });
    if (!confirmed) {
      return;
    }
    this.isCancelling = true;
    this.orderService.cancel(order.id).subscribe({
      next: (cancelled) => {
        this.isCancelling = false;
        this.order.set(cancelled);
        this.toasts.success(`Order #${order.id} has been cancelled`);
      },
      error: (error) => {
        this.isCancelling = false;
        this.toasts.error(errorMessage(error));
      },
    });
  }

  orderAgain(order: Order) {
    this.productService.getByIds(order.items.map((item) => item.productId)).subscribe({
      next: (products) => {
        let missing = 0;
        for (const item of order.items) {
          const product = products.find((p) => p.id === item.productId);
          const added = product?.active ? this.cart.add(product, item.quantity) : 0;
          if (added < item.quantity) {
            missing++;
          }
        }
        if (missing > 0) {
          this.toasts.info('Some products are sold out or limited, the rest is in your cart');
        } else {
          this.toasts.success('The products have been added to your cart');
        }
        this.router.navigate(['/cart']);
      },
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }
}
