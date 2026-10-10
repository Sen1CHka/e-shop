import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import {
  ConfirmService,
  Order,
  OrderFilter,
  OrderService,
  OrderState,
  PricePipe,
  STATES,
  ToastService,
  errorMessage,
  stateInfo,
} from '@selling-frontend/domain';
import {
  SellingDialogComponent,
  SellingEmptyStateComponent,
  SellingIconComponent,
  SellingOrderStateComponent,
} from '@selling-frontend/shared';

@Component({
  selector: 'selling-orders',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    PricePipe,
    SellingIconComponent,
    SellingDialogComponent,
    SellingOrderStateComponent,
    SellingEmptyStateComponent,
  ],
  templateUrl: './selling-orders.component.html',
  styleUrl: './selling-orders.component.scss',
})
export class SellingOrdersComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  private readonly confirm = inject(ConfirmService);
  private readonly toasts = inject(ToastService);
  private readonly route = inject(ActivatedRoute);

  readonly states = STATES;
  readonly orders = signal<Order[] | null>(null);
  readonly detail = signal<Order | null>(null);

  filter: OrderFilter = { state: null, user: '', dateTo: null };

  get isFiltered(): boolean {
    return !!(this.filter.state || this.filter.user || this.filter.dateTo);
  }

  ngOnInit(): void {
    // The dashboard links here with a preselected state or an order to open
    const params = this.route.snapshot.queryParamMap;
    const state = params.get('state') as OrderState;
    if (STATES.some((item) => item.code === state)) {
      this.filter.state = state;
    }
    this.load(Number(params.get('open')) || null);
  }

  load(openId: number | null = null) {
    this.orderService.filterAll(this.filter).subscribe({
      next: (orders) => {
        this.orders.set(orders);
        if (openId) {
          this.detail.set(orders.find((order) => order.id === openId) ?? null);
        }
      },
      error: (error) => {
        this.orders.set([]);
        this.toasts.error(errorMessage(error));
      },
    });
  }

  clearFilter() {
    this.filter = { state: null, user: '', dateTo: null };
    this.load();
  }

  isFinal(order: Order): boolean {
    return order.state === 'DELIVERED' || order.state === 'CANCELLED';
  }

  async changeState(order: Order, select: HTMLSelectElement) {
    const state = select.value as OrderState;
    // Delivered and cancelled orders can no longer be changed, so both steps are confirmed first
    if (state === 'CANCELLED' || state === 'DELIVERED') {
      const confirmed = await this.confirm.ask({
        title: state === 'CANCELLED' ? `Cancel order #${order.id}?` : `Mark order #${order.id} as delivered?`,
        text:
          state === 'CANCELLED'
            ? 'The goods return to stock and the order can no longer be changed.'
            : 'A delivered order is closed and can no longer be changed.',
        confirmLabel: state === 'CANCELLED' ? 'Cancel order' : 'Mark as delivered',
        cancelLabel: 'Keep as it is',
        danger: state === 'CANCELLED',
      });
      if (!confirmed) {
        select.value = order.state;
        return;
      }
    }
    this.orderService.updateState(order.id, state).subscribe({
      next: (updated) => {
        this.replace(updated);
        this.toasts.success(`Order #${order.id} is now: ${stateInfo(state).label}`);
      },
      error: (error) => {
        select.value = order.state;
        this.toasts.error(errorMessage(error));
      },
    });
  }

  trackById(index: number, order: Order) {
    return order.id;
  }

  private replace(updated: Order) {
    this.orders.update((orders) => orders && orders.map((order) => (order.id === updated.id ? updated : order)));
    if (this.detail()?.id === updated.id) {
      this.detail.set(updated);
    }
  }
}
