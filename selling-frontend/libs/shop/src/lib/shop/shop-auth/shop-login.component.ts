import { Component, ElementRef, Input, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService, ToastService, errorMessage } from '@selling-frontend/domain';
import { SellingFieldComponent, SellingIconComponent } from '@selling-frontend/shared';

/** Only local addresses are accepted, so a crafted link cannot send the user to another site */
export function safeReturnUrl(url: string | undefined): string {
  return url && url.startsWith('/') && !url.startsWith('//') ? url : '/';
}

@Component({
  selector: 'shop-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, SellingFieldComponent, SellingIconComponent],
  templateUrl: './shop-login.component.html',
  styleUrl: './shop-auth.scss',
})
export class ShopLoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toasts = inject(ToastService);

  readonly form = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: Validators.required }),
    password: new FormControl('', { nonNullable: true, validators: Validators.required }),
  });

  @ViewChild('passwordInput')
  passwordInput?: ElementRef<HTMLInputElement>;

  target = '/';
  showPassword = false;
  isSubmitting = false;
  error = '';

  // Address the user wanted before being asked to sign in, filled from the query string
  @Input()
  set returnUrl(url: string | undefined) {
    this.target = safeReturnUrl(url);
  }

  fill(username: string, password: string) {
    this.form.setValue({ username, password });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { username, password } = this.form.getRawValue();
    this.isSubmitting = true;
    this.error = '';
    this.auth.login(username.trim(), password).subscribe({
      next: ({ user }) => {
        this.toasts.success(`Welcome back, ${user.realName.split(' ')[0]}`);
        this.router.navigateByUrl(this.target);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.error = errorMessage(error);
        // The password stays in place and is selected, so a typo can be fixed or simply typed over
        this.passwordInput?.nativeElement.select();
      },
    });
  }
}
