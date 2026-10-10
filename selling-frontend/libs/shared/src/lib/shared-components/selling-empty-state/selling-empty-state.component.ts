import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { IconName, SellingIconComponent } from '../selling-icon';

/** Explains why a list is empty and offers the next step through projected content */
@Component({
  selector: 'selling-empty-state',
  standalone: true,
  imports: [SellingIconComponent],
  template: `
    <div class="empty-state">
      <div class="empty-state-icon"><selling-icon [name]="icon" [size]="32"></selling-icon></div>
      <h2 class="h5 mb-1">{{ heading }}</h2>
      <p class="text-secondary mb-3">{{ text }}</p>
      <ng-content></ng-content>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingEmptyStateComponent {
  @Input()
  icon: IconName = 'package';

  @Input({ required: true })
  heading!: string;

  @Input()
  text = '';
}
