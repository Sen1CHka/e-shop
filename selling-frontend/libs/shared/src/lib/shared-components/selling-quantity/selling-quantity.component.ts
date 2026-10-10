import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingIconComponent } from '../selling-icon';

@Component({
  selector: 'selling-quantity',
  standalone: true,
  imports: [CommonModule, SellingIconComponent],
  template: `
    <div class="quantity" role="group" [attr.aria-label]="label">
      <button type="button" aria-label="Decrease quantity" [disabled]="value <= 1" (click)="set(value - 1)">
        <selling-icon name="minus" [size]="16"></selling-icon>
      </button>
      <input
        type="number"
        inputmode="numeric"
        min="1"
        [max]="max"
        [value]="value"
        [attr.aria-label]="label"
        (change)="onInput($event)"
      />
      <button type="button" aria-label="Increase quantity" [disabled]="value >= max" (click)="set(value + 1)">
        <selling-icon name="plus" [size]="16"></selling-icon>
      </button>
    </div>
  `,
  styleUrl: './selling-quantity.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingQuantityComponent {
  @Input()
  value = 1;

  @Input()
  max = 99;

  @Input()
  label = 'Quantity';

  @Output()
  valueChange = new EventEmitter<number>();

  set(value: number) {
    const clamped = Math.max(1, Math.min(Math.floor(value) || 1, Math.max(this.max, 1)));
    if (clamped !== this.value) {
      this.valueChange.emit(clamped);
    }
    return clamped;
  }

  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    // Typed values outside the allowed range are corrected in place
    input.value = String(this.set(Number(input.value)));
  }
}
