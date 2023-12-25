import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'selling-form',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './selling-form.component.html',
  styleUrl: './selling-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingFormComponent {}
