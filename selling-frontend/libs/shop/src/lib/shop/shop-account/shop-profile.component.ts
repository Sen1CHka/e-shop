import { Component, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { AuthService, ToastService, errorMessage, fieldErrors } from '@selling-frontend/domain';
import { SellingFieldComponent, SellingIconComponent, applyServerErrors } from '@selling-frontend/shared';

// Validator of the repeated password; it compares itself with the new password next to it
function matchesNewPassword(control: AbstractControl): ValidationErrors | null {
  const newPassword = control.parent?.get('newPassword')?.value;
  return control.value && control.value !== newPassword ? { mismatch: true } : null;
}

@Component({
  selector: 'shop-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SellingFieldComponent, SellingIconComponent],
  template: `
    <h1 class="page-title">My profile</h1>

    <section class="panel mb-4" aria-labelledby="details-title">
      <h2 id="details-title" class="h5 mb-3">Personal details</h2>
      <form [formGroup]="profileForm" (ngSubmit)="saveProfile()" novalidate>
        <div class="row gx-3 gy-1">
          <selling-field class="col-md-6" label="Username" for="username" hint="The username cannot be changed" [control]="null">
            <input id="username" type="text" class="form-control" [value]="auth.user()?.username" disabled />
          </selling-field>
          <selling-field class="col-md-6" label="Full name" for="realName" [control]="profileForm.controls.realName">
            <input id="realName" type="text" class="form-control" formControlName="realName" autocomplete="name" />
          </selling-field>
          <selling-field class="col-md-6" label="E-mail" for="email" [control]="profileForm.controls.email">
            <input id="email" type="email" class="form-control" formControlName="email" autocomplete="email" />
          </selling-field>
          <selling-field class="col-md-6" label="Phone" for="phone" [optional]="true" [control]="profileForm.controls.phone">
            <input id="phone" type="tel" class="form-control" formControlName="phone" autocomplete="tel" />
          </selling-field>
        </div>
        <button type="submit" class="btn btn-primary mt-2" [disabled]="isSavingProfile || profileForm.pristine">
          Save changes
        </button>
      </form>
    </section>

    <section class="panel" aria-labelledby="password-title">
      <h2 id="password-title" class="h5 mb-3">Change password</h2>
      <form [formGroup]="passwordForm" (ngSubmit)="changePassword()" novalidate class="password-form">
        <div *ngIf="passwordError" class="form-error mb-3" role="alert">
          <selling-icon name="alert"></selling-icon>
          <span>{{ passwordError }}</span>
        </div>
        <selling-field class="mb-1" label="Current password" for="currentPassword" [control]="passwordForm.controls.currentPassword">
          <input id="currentPassword" type="password" class="form-control" formControlName="currentPassword" autocomplete="current-password" />
        </selling-field>
        <selling-field
          class="mb-1"
          label="New password"
          for="newPassword"
          hint="At least 6 characters"
          [control]="passwordForm.controls.newPassword"
        >
          <input id="newPassword" type="password" class="form-control" formControlName="newPassword" autocomplete="new-password" />
        </selling-field>
        <selling-field class="mb-2" label="New password again" for="repeatPassword" [control]="passwordForm.controls.repeatPassword">
          <input id="repeatPassword" type="password" class="form-control" formControlName="repeatPassword" autocomplete="new-password" />
        </selling-field>
        <button type="submit" class="btn btn-primary" [disabled]="isSavingPassword">Change password</button>
      </form>
    </section>
  `,
  styles: ['.password-form { max-width: 24rem; }'],
})
export class ShopProfileComponent {
  readonly auth = inject(AuthService);
  private readonly toasts = inject(ToastService);

  readonly profileForm = new FormGroup({
    realName: new FormControl(this.auth.user()?.realName ?? '', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)],
    }),
    email: new FormControl(this.auth.user()?.email ?? '', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),
    phone: new FormControl(this.auth.user()?.phone ?? '', {
      nonNullable: true,
      validators: Validators.pattern(/^\+?[\d ]{9,16}$/),
    }),
  });

  readonly passwordForm = new FormGroup({
    currentPassword: new FormControl('', { nonNullable: true, validators: Validators.required }),
    newPassword: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(6)] }),
    repeatPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, matchesNewPassword],
    }),
  });

  isSavingProfile = false;
  isSavingPassword = false;
  passwordError = '';

  constructor() {
    // Editing the new password changes whether the repeated one still matches
    this.passwordForm.controls.newPassword.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.passwordForm.controls.repeatPassword.updateValueAndValidity());
  }

  saveProfile() {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }
    const { realName, email, phone } = this.profileForm.getRawValue();
    this.isSavingProfile = true;
    this.auth.updateProfile({ realName, email, phone: phone.trim() || null }).subscribe({
      next: () => {
        this.isSavingProfile = false;
        this.profileForm.markAsPristine();
        this.toasts.success('Your profile has been saved');
      },
      error: (error) => {
        this.isSavingProfile = false;
        this.toasts.error(errorMessage(error));
        applyServerErrors(this.profileForm, fieldErrors(error));
      },
    });
  }

  changePassword() {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }
    const { currentPassword, newPassword } = this.passwordForm.getRawValue();
    this.isSavingPassword = true;
    this.passwordError = '';
    this.auth.changePassword(currentPassword, newPassword).subscribe({
      next: () => {
        this.isSavingPassword = false;
        this.passwordForm.reset();
        this.toasts.success('Your password has been changed');
      },
      error: (error) => {
        this.isSavingPassword = false;
        this.passwordError = errorMessage(error);
      },
    });
  }
}
