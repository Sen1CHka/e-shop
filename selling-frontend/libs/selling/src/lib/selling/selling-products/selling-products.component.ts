import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent } from '@selling-frontend/shared';

@Component({
  selector: 'selling-products',
  standalone: true,
  imports: [CommonModule, SellingButtonComponent],
  templateUrl: './selling-products.component.html',
  styleUrl: './selling-products.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingProductsComponent {}
