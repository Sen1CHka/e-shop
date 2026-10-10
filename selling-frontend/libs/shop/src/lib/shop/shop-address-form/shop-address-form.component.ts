import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Shipping } from '@selling-frontend/domain';
import { SellingFieldComponent } from '@selling-frontend/shared';

export type AddressForm = FormGroup<{
  fullName: FormControl<string>;
  street: FormControl<string>;
  city: FormControl<string>;
  zipCode: FormControl<string>;
  country: FormControl<string>;
  phone: FormControl<string>;
}>;

export function createAddressForm(value?: Partial<Shipping> | null): AddressForm {
  const text = (initial: string | null | undefined, maxLength: number) =>
    new FormControl(initial ?? '', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(maxLength)],
    });
  return new FormGroup({
    fullName: text(value?.fullName, 100),
    street: text(value?.street, 120),
    city: text(value?.city, 80),
    zipCode: new FormControl(value?.zipCode ?? '', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\d{3} ?\d{2}$/)],
    }),
    country: text(value?.country ?? 'Czech Republic', 60),
    phone: new FormControl(value?.phone ?? '', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\+?[\d ]{9,16}$/)],
    }),
  });
}

/** Fields of a delivery address, used in the checkout and in the address book */
@Component({
  selector: 'shop-address-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SellingFieldComponent],
  template: `
    <div class="row gx-3 gy-1" [formGroup]="form">
      <selling-field class="col-12" label="Full name" [for]="prefix + 'fullName'" [control]="form.controls.fullName">
        <input [id]="prefix + 'fullName'" type="text" class="form-control" formControlName="fullName" autocomplete="name" />
      </selling-field>
      <selling-field class="col-12" label="Street and number" [for]="prefix + 'street'" [control]="form.controls.street">
        <input [id]="prefix + 'street'" type="text" class="form-control" formControlName="street" autocomplete="street-address" />
      </selling-field>
      <selling-field class="col-sm-8" label="City" [for]="prefix + 'city'" [control]="form.controls.city">
        <input [id]="prefix + 'city'" type="text" class="form-control" formControlName="city" autocomplete="address-level2" />
      </selling-field>
      <selling-field
        class="col-sm-4"
        label="ZIP code"
        hint="e.g. 160 00"
        [for]="prefix + 'zipCode'"
        [control]="form.controls.zipCode"
      >
        <input
          [id]="prefix + 'zipCode'"
          type="text"
          inputmode="numeric"
          class="form-control"
          formControlName="zipCode"
          autocomplete="postal-code"
        />
      </selling-field>
      <selling-field class="col-sm-6" label="Country" [for]="prefix + 'country'" [control]="form.controls.country">
        <input [id]="prefix + 'country'" type="text" class="form-control" formControlName="country" autocomplete="country-name" />
      </selling-field>
      <selling-field
        class="col-sm-6"
        label="Phone"
        hint="For the courier, e.g. +420 601 111 222"
        [for]="prefix + 'phone'"
        [control]="form.controls.phone"
      >
        <input [id]="prefix + 'phone'" type="tel" class="form-control" formControlName="phone" autocomplete="tel" />
      </selling-field>
    </div>
  `,
})
export class ShopAddressFormComponent {
  @Input({ required: true })
  form!: AddressForm;

  /** Keeps input ids unique when two address forms are on one page */
  @Input()
  prefix = 'address-';
}
