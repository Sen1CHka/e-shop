import { Component, ElementRef, OnInit, QueryList, ViewChildren, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import {
  AccountService,
  Address,
  AuthService,
  CartService,
  CheckoutOption,
  CheckoutOptions,
  OrderService,
  PricePipe,
  Shipping,
  ToastService,
  errorMessage,
} from '@selling-frontend/domain';
import { SellingIconComponent, SellingProductImageComponent } from '@selling-frontend/shared';
import { ShopAddressFormComponent, createAddressForm } from '../shop-address-form/shop-address-form.component';

@Component({
  selector: 'shop-checkout',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    PricePipe,
    SellingIconComponent,
    SellingProductImageComponent,
    ShopAddressFormComponent,
  ],
  templateUrl: './shop-checkout.component.html',
  styleUrl: './shop-checkout.component.scss',
})
export class ShopCheckoutComponent implements OnInit {
  readonly cart = inject(CartService);
  private readonly auth = inject(AuthService);
  private readonly accountService = inject(AccountService);
  private readonly orderService = inject(OrderService);
  private readonly toasts = inject(ToastService);
  private readonly router = inject(Router);

  @ViewChildren('stepTitle')
  stepTitles!: QueryList<ElementRef<HTMLElement>>;

  readonly steps = ['Delivery address', 'Delivery and payment', 'Summary'];
  readonly step = signal(1);
  readonly addresses = signal<Address[]>([]);
  readonly options = signal<CheckoutOptions | null>(null);
  readonly shipping = signal<Shipping | null>(null);
  readonly isSubmitting = signal(false);
  readonly error = signal('');
  readonly stockProblem = signal(false);

  // Radio buttons are bound with ngModel, the signals below mirror them for the computed totals
  private readonly deliveryCode = signal('');
  private readonly paymentCode = signal('');

  readonly deliveryOption = computed(() => this.find(this.options()?.deliveryMethods, this.deliveryCode()));
  readonly paymentOption = computed(() => this.find(this.options()?.paymentMethods, this.paymentCode()));
  readonly total = computed(
    () => this.cart.total() + (this.deliveryOption()?.price ?? 0) + (this.paymentOption()?.price ?? 0)
  );

  selectedAddress: number | 'new' = 'new';
  saveAddress = true;
  note = '';
  addressForm = createAddressForm({ fullName: this.auth.user()?.realName, phone: this.auth.user()?.phone ?? '' });

  get delivery(): string {
    return this.deliveryCode();
  }

  set delivery(code: string) {
    this.deliveryCode.set(code);
  }

  get payment(): string {
    return this.paymentCode();
  }

  set payment(code: string) {
    this.paymentCode.set(code);
  }

  ngOnInit(): void {
    if (this.cart.isEmpty()) {
      this.router.navigate(['/cart']);
      return;
    }
    this.cart.refresh((changes) => {
      if (changes.length > 0) {
        this.toasts.info('Some products in your cart are no longer available, please check it');
        this.router.navigate(['/cart']);
      }
    });
    this.accountService.getAddresses().subscribe({
      next: (addresses) => {
        this.addresses.set(addresses);
        // The default address is listed first and preselected, so a returning customer just continues
        if (addresses.length > 0) {
          this.selectedAddress = addresses[0].id;
        }
      },
      error: () => undefined,
    });
    this.orderService.getCheckoutOptions().subscribe({
      next: (options) => {
        this.options.set(options);
        this.delivery = options.deliveryMethods[0]?.code ?? '';
        this.payment = options.paymentMethods[0]?.code ?? '';
      },
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }

  confirmAddress() {
    if (this.selectedAddress === 'new') {
      if (this.addressForm.invalid) {
        this.addressForm.markAllAsTouched();
        this.focusFirstInvalid();
        return;
      }
      this.shipping.set(this.addressForm.getRawValue());
    } else {
      const address = this.addresses().find((item) => item.id === this.selectedAddress);
      if (!address) {
        return;
      }
      this.shipping.set({
        fullName: address.fullName,
        street: address.street,
        city: address.city,
        zipCode: address.zipCode,
        country: address.country,
        phone: address.phone ?? '',
      });
    }
    this.goTo(2);
  }

  goTo(step: number) {
    this.error.set('');
    this.step.set(step);
    // Moves keyboard and screen reader focus to the heading of the new step
    setTimeout(() => this.stepTitles.first?.nativeElement.focus());
  }

  placeOrder() {
    const shipping = this.shipping();
    if (!shipping || this.isSubmitting()) {
      return;
    }
    this.isSubmitting.set(true);
    this.error.set('');
    this.stockProblem.set(false);
    this.orderService
      .create({
        items: this.cart.items().map((item) => ({ productId: item.productId, quantity: item.quantity })),
        deliveryMethod: this.delivery,
        paymentMethod: this.payment,
        shipping,
        note: this.note.trim() || null,
      })
      .subscribe({
        next: (order) => {
          if (this.selectedAddress === 'new' && this.saveAddress) {
            this.accountService
              .createAddress({ ...shipping, defaultAddress: false })
              .subscribe({ error: () => undefined });
          }
          this.cart.clear();
          this.router.navigate(['/account/orders', order.id], { queryParams: { placed: 1 } });
        },
        error: (error) => {
          this.isSubmitting.set(false);
          this.error.set(errorMessage(error, 'The order could not be placed, please try again.'));
          // 409 = something sold out in the meantime; the cart is refreshed so it shows the truth
          if (error instanceof HttpErrorResponse && error.status === 409) {
            this.stockProblem.set(true);
            this.cart.refresh();
          }
        },
      });
  }

  private find(options: CheckoutOption[] | undefined, code: string): CheckoutOption | null {
    return options?.find((option) => option.code === code) ?? null;
  }

  private focusFirstInvalid() {
    setTimeout(() => document.querySelector<HTMLElement>('shop-address-form .ng-invalid:not(div)')?.focus());
  }
}
