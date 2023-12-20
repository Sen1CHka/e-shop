import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ColumnsDefinition, KeyOrFunctionPipe } from '@selling-frontend/domain';
import { SellingButtonComponent } from '../selling-button';

@Component({
  selector: 'selling-table',
  standalone: true,
  imports: [CommonModule, KeyOrFunctionPipe, SellingButtonComponent],
  templateUrl: './selling-table.component.html',
  styleUrl: './selling-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingTableComponent<T> {
  @Input()
  columnDefinitions!: ColumnsDefinition[];

  @Input()
  data!: T[] | undefined;
}
