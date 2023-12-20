import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'selling-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './selling-button.component.html',
  styleUrl: './selling-button.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingButtonComponent {

  @Input()
  buttonLabel?: string;

  
}
