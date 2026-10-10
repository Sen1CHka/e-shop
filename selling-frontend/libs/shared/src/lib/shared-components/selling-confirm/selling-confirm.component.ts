import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmService } from '@selling-frontend/domain';
import { SellingDialogComponent } from '../selling-dialog';

/** Draws the question asked through ConfirmService; placed once in the app root */
@Component({
  selector: 'selling-confirm',
  standalone: true,
  imports: [CommonModule, SellingDialogComponent],
  template: `
    <selling-dialog
      *ngIf="confirm.request() as request"
      [isVisible]="true"
      [dialogTitle]="request.title"
      (closeDialogEvent)="confirm.answer(false)"
    >
      <p class="text-secondary mb-4">{{ request.text }}</p>
      <div class="d-flex justify-content-end gap-2 flex-wrap">
        <button type="button" class="btn btn-outline-secondary" (click)="confirm.answer(false)">
          {{ request.cancelLabel ?? 'Cancel' }}
        </button>
        <button
          type="button"
          class="btn"
          [ngClass]="request.danger ? 'btn-danger' : 'btn-primary'"
          (click)="confirm.answer(true)"
        >
          {{ request.confirmLabel }}
        </button>
      </div>
    </selling-dialog>
  `,
})
export class SellingConfirmComponent {
  readonly confirm = inject(ConfirmService);
}
