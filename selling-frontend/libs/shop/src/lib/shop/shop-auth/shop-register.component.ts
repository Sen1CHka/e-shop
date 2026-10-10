import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService, ToastService, errorMessage, fieldErrors } from '@selling-frontend/domain';
import { SellingFieldComponent, SellingIconComponent, applyServerErrors } from '@selling-frontend/shared';
import { safeReturnUrl } from './shop-login.component';

@Component({
  selector: 'shop-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, SellingFieldComponent, SellingIconComponent],
  templateUrl: './shop-register.component.html',
  styleUrl: './shop-auth.scss',
})
export class ShopRegisterComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toasts = inject(ToastService);

  readonly form = new FormGroup({
    realName: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(100)] }),
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    username: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(30),
        Validators.pattern(/^[A-Za-z0-9._-]*$/),
      ],
    }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(6)] }),
  });

  target = '/';
  showPassword = false;
  isSubmitting = false;
  error = '';

  @Input()
  set returnUrl(url: string | undefined) {
    this.target = safeReturnUrl(url);
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.isSubmitting = true;
    this.error = '';
    this.auth.register(this.form.getRawValue()).subscribe({
      next: ({ user }) => {
        this.toasts.success(`Your account is ready, ${user.realName.split(' ')[0]}`);
        this.router.navigateByUrl(this.target);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.error = errorMessage(error);
        applyServerErrors(this.form, fieldErrors(error));
      },
    });
  }
}
