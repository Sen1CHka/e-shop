import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent } from '@selling-frontend/shared';
import { ProductFormComponent } from './components';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'selling-home',
  standalone: true,
  imports: [CommonModule, SellingButtonComponent, ProductFormComponent],
  templateUrl: './selling-home.component.html',
  styleUrl: './selling-home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingHomeComponent {
  isProductDialogVisible$ = new BehaviorSubject(false);
}
