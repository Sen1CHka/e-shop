import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  AccountService,
  Address,
  AuthService,
  ConfirmService,
  ToastService,
  errorMessage,
  fieldErrors,
} from '@selling-frontend/domain';
import {
  SellingDialogComponent,
  SellingEmptyStateComponent,
  SellingIconComponent,
  applyServerErrors,
} from '@selling-frontend/shared';
import { ShopAddressFormComponent, createAddressForm } from '../shop-address-form/shop-address-form.component';

@Component({
  selector: 'shop-addresses',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SellingIconComponent,
    SellingDialogComponent,
    SellingEmptyStateComponent,
    ShopAddressFormComponent,
  ],
  template: `
    <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
      <h1 class="page-title mb-0">My addresses</h1>
      <button type="button" class="btn btn-primary" (click)="open()">
        <selling-icon name="plus" [size]="16"></selling-icon> Add address
      </button>
    </div>

    <ng-container *ngIf="addresses() as list; else loading">
      <div class="panel" *ngIf="list.length === 0">
        <selling-empty-state icon="map-pin" heading="No saved addresses" text="Save an address and the checkout will be a matter of seconds.">
          <button type="button" class="btn btn-primary" (click)="open()">Add address</button>
        </selling-empty-state>
      </div>

      <div class="address-grid">
        <article *ngFor="let address of list" class="panel address-card">
          <span *ngIf="address.defaultAddress" class="badge text-bg-primary mb-2">Default</span>
          <address class="mb-3">
            <strong>{{ address.fullName }}</strong><br />
            {{ address.street }}<br />
            {{ address.zipCode }} {{ address.city }}<br />
            {{ address.country }}<br />
            {{ address.phone }}
          </address>
          <div class="d-flex gap-3 flex-wrap mt-auto">
            <button type="button" class="link-button" (click)="open(address)">Edit</button>
            <button *ngIf="!address.defaultAddress" type="button" class="link-button" (click)="makeDefault(address)">
              Set as default
            </button>
            <button type="button" class="link-button text-danger" (click)="remove(address)">Delete</button>
          </div>
        </article>
      </div>
    </ng-container>
    <ng-template #loading><div class="skeleton" style="height: 12rem"></div></ng-template>

    <selling-dialog [isVisible]="isDialogVisible" [dialogTitle]="editing ? 'Edit address' : 'New address'" (closeDialogEvent)="isDialogVisible = false">
      <form (ngSubmit)="save()" novalidate>
        <shop-address-form [form]="form" prefix="book-"></shop-address-form>
        <div class="form-check mt-3" *ngIf="!editing?.defaultAddress">
          <input id="default-address" name="defaultAddress" type="checkbox" class="form-check-input" [(ngModel)]="defaultAddress" />
          <label for="default-address" class="form-check-label">Use as my default address</label>
        </div>
        <div class="d-flex justify-content-end gap-2 mt-4">
          <button type="button" class="btn btn-outline-secondary" (click)="isDialogVisible = false">Cancel</button>
          <button type="submit" class="btn btn-primary" [disabled]="isSaving">Save address</button>
        </div>
      </form>
    </selling-dialog>
  `,
  styles: [
    `
      .address-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(17rem, 1fr));
        gap: 1rem;
      }
      .address-card {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
      }
    `,
  ],
})
export class ShopAddressesComponent implements OnInit {
  private readonly accountService = inject(AccountService);
  private readonly auth = inject(AuthService);
  private readonly confirm = inject(ConfirmService);
  private readonly toasts = inject(ToastService);

  readonly addresses = signal<Address[] | null>(null);

  form = createAddressForm();
  editing: Address | null = null;
  defaultAddress = false;
  isDialogVisible = false;
  isSaving = false;

  ngOnInit(): void {
    this.load();
  }

  open(address?: Address) {
    this.editing = address ?? null;
    this.defaultAddress = address?.defaultAddress ?? false;
    this.form = createAddressForm(
      address ? { ...address, phone: address.phone ?? '' } : { fullName: this.auth.user()?.realName }
    );
    this.isDialogVisible = true;
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = { ...this.form.getRawValue(), defaultAddress: this.defaultAddress };
    const request = this.editing
      ? this.accountService.updateAddress(this.editing.id, value)
      : this.accountService.createAddress(value);
    this.isSaving = true;
    request.subscribe({
      next: () => {
        this.isSaving = false;
        this.isDialogVisible = false;
        this.toasts.success('The address has been saved');
        this.load();
      },
      error: (error) => {
        this.isSaving = false;
        this.toasts.error(errorMessage(error));
        applyServerErrors(this.form, fieldErrors(error));
      },
    });
  }

  makeDefault(address: Address) {
    this.accountService.updateAddress(address.id, { ...address, defaultAddress: true }).subscribe({
      next: () => {
        this.toasts.success('Your default address has been changed');
        this.load();
      },
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }

  async remove(address: Address) {
    const confirmed = await this.confirm.ask({
      title: 'Delete this address?',
      text: `${address.street}, ${address.city} will be removed from your account. Existing orders are not affected.`,
      confirmLabel: 'Delete address',
      danger: true,
    });
    if (!confirmed) {
      return;
    }
    this.accountService.deleteAddress(address.id).subscribe({
      next: () => {
        this.toasts.success('The address has been deleted');
        this.load();
      },
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }

  private load() {
    this.accountService.getAddresses().subscribe({
      next: (addresses) => this.addresses.set(addresses),
      error: (error) => {
        this.addresses.set([]);
        this.toasts.error(errorMessage(error));
      },
    });
  }
}
