import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent } from '@selling-frontend/shared';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'selling-home',
  standalone: true,
  imports: [
    CommonModule,
    SellingButtonComponent,
  ],
  templateUrl: './selling-home.component.html',
  styleUrl: './selling-home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingHomeComponent {
}
