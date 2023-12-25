import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'selling-input',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './selling-input.component.html',
  styleUrl: './selling-input.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingInputComponent {}
