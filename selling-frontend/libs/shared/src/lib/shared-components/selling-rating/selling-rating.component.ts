import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingIconComponent } from '../selling-icon';

const LABELS = ['', 'Terrible', 'Poor', 'Average', 'Good', 'Excellent'];

/**
 * Star rating. Read-only by default; with [editable] the stars become buttons
 * and the chosen value is emitted through (valueChange).
 */
@Component({
  selector: 'selling-rating',
  standalone: true,
  imports: [CommonModule, SellingIconComponent],
  template: `
    <ng-container *ngIf="editable; else readOnly">
      <span class="stars" role="radiogroup" aria-label="Rating" (mouseleave)="hovered = 0">
        <button
          *ngFor="let star of stars"
          type="button"
          class="star-button"
          role="radio"
          [attr.aria-checked]="value === star"
          [attr.aria-label]="star + ' of 5 - ' + labels[star]"
          (mouseenter)="hovered = star"
          (focus)="hovered = star"
          (blur)="hovered = 0"
          (click)="valueChange.emit(star)"
        >
          <selling-icon name="star" [size]="size" [filled]="star <= (hovered || value)"></selling-icon>
        </button>
      </span>
      <span class="rating-label">{{ labels[hovered || value] || 'Choose a rating' }}</span>
    </ng-container>

    <ng-template #readOnly>
      <span class="stars" role="img" [attr.aria-label]="ariaLabel">
        <span class="stars-empty">
          <selling-icon *ngFor="let star of stars" name="star" [size]="size" [filled]="true"></selling-icon>
        </span>
        <span class="stars-full" [style.width.%]="(value / 5) * 100">
          <selling-icon *ngFor="let star of stars" name="star" [size]="size" [filled]="true"></selling-icon>
        </span>
      </span>
      <span *ngIf="count !== null" class="rating-count">
        <ng-container *ngIf="count > 0; else noReviews">{{ value | number : '1.1-1' }} ({{ count }})</ng-container>
        <ng-template #noReviews>No reviews yet</ng-template>
      </span>
    </ng-template>
  `,
  styleUrl: './selling-rating.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingRatingComponent {
  @Input()
  value = 0;

  /** Number of reviews shown next to the stars; null hides it */
  @Input()
  count: number | null = null;

  @Input()
  size = 16;

  @Input()
  editable = false;

  @Output()
  valueChange = new EventEmitter<number>();

  readonly stars = [1, 2, 3, 4, 5];
  readonly labels = LABELS;
  hovered = 0;

  get ariaLabel(): string {
    return this.count === 0 ? 'No reviews yet' : `Rated ${this.value.toFixed(1)} out of 5`;
  }
}
