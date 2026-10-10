import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Order, OrderService, PricePipe } from '@selling-frontend/domain';
import {
  SellingEmptyStateComponent,
  SellingIconComponent,
  SellingOrderStateComponent,
  SellingProductImageComponent,
} from '@selling-frontend/shared';

@Component({
  selector: 'shop-orders',
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
  template: `
    <h1 class="page-title">My orders</h1>

    <div *ngIf="failed()" class="form-error">
      <selling-icon name="alert"></selling-icon>
      <span>Your orders could not be loaded. <button type="button" class="link-button" (click)="load()">Try again</button></span>
    </div>

    <ng-container *ngIf="orders() as list; else loading">
      <div class="panel" *ngIf="list.length === 0">
        <selling-empty-state icon="package" heading="You have no orders yet" text="Once you order something, you can follow it here.">
          <a routerLink="/catalog" class="btn btn-primary">Start shopping</a>
        </selling-empty-state>
      </div>

      <a *ngFor="let order of list" class="order-card panel" [routerLink]="[order.id]">
        <div class="order-card-header">
          <div>
            <strong>Order #{{ order.id }}</strong>
            <span class="text-secondary"> · {{ order.date | date : 'd MMM y' }}</span>
          </div>
          <selling-order-state [state]="order.state"></selling-order-state>
        </div>
        <div class="order-card-body">
          <div class="order-thumbs">
            <span class="thumb" *ngFor="let item of order.items | slice : 0 : 4">
              <selling-product-image [name]="item.productName" [imageUrl]="item.imageUrl" [icon]="item.categoryIcon">
              </selling-product-image>
            </span>
            <span class="order-more" *ngIf="order.items.length > 4">+{{ order.items.length - 4 }}</span>
          </div>
          <div class="order-card-names">{{ names(order) }}</div>
          <div class="order-card-total">
            <strong>{{ order.totalPrice | price }}</strong>
            <span>{{ order.itemCount }} {{ order.itemCount === 1 ? 'item' : 'items' }}</span>
          </div>
          <selling-icon name="chevron-right"></selling-icon>
        </div>
      </a>
    </ng-container>

    <ng-template #loading>
      <ng-container *ngIf="!failed()">
        <div class="skeleton order-skeleton"></div>
        <div class="skeleton order-skeleton"></div>
      </ng-container>
    </ng-template>
  `,
  styleUrl: './shop-orders.component.scss',
})
export class ShopOrdersComponent implements OnInit {
  private readonly orderService = inject(OrderService);

  readonly orders = signal<Order[] | null>(null);
  readonly failed = signal(false);

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.failed.set(false);
    this.orderService.getMine().subscribe({
      next: (orders) => this.orders.set(orders),
      error: () => this.failed.set(true),
    });
  }

  names(order: Order): string {
    return order.items.map((item) => item.productName).join(', ');
  }
}
