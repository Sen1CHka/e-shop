import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Toast, ToastService } from '@selling-frontend/domain';
import { SellingIconComponent } from '../selling-icon';

@Component({
  selector: 'selling-toasts',
  standalone: true,
  imports: [CommonModule, SellingIconComponent],
  template: `
    <div class="toasts" aria-live="polite">
      <div
        *ngFor="let toast of toastService.toasts(); trackBy: trackById"
        class="toast-message"
        [ngClass]="'toast-' + toast.type"
        [attr.role]="toast.type === 'error' ? 'alert' : 'status'"
      >
        <selling-icon [name]="toast.type === 'success' ? 'check-circle' : toast.type === 'error' ? 'alert' : 'info'">
        </selling-icon>
        <span class="toast-text">{{ toast.text }}</span>
        <button *ngIf="toast.action as action" type="button" class="toast-action" (click)="run(toast)">
          {{ action.label }}
        </button>
        <button type="button" class="toast-close" aria-label="Dismiss" (click)="toastService.dismiss(toast.id)">
          <selling-icon name="x" [size]="16"></selling-icon>
        </button>
      </div>
    </div>
  `,
  styleUrl: './selling-toasts.component.scss',
})
export class SellingToastsComponent {
  readonly toastService = inject(ToastService);

  trackById(index: number, toast: Toast) {
    return toast.id;
  }

  run(toast: Toast) {
    toast.action?.run();
    this.toastService.dismiss(toast.id);
  }
}
