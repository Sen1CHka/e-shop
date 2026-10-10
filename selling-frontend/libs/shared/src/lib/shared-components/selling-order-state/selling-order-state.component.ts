import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OrderState, stateInfo } from '@selling-frontend/domain';

@Component({
  selector: 'selling-order-state',
  standalone: true,
  template: `<span class="state-badge" [class]="'state-badge state-' + state.toLowerCase()">{{ label }}</span>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingOrderStateComponent {
  @Input({ required: true })
  state!: OrderState;

  get label(): string {
    return stateInfo(this.state).label;
  }
}
