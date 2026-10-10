import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingIconComponent } from '../selling-icon';

@Component({
  selector: 'selling-pagination',
  standalone: true,
  imports: [CommonModule, SellingIconComponent],
  template: `
    <nav *ngIf="totalPages > 1" aria-label="Pages">
      <ul class="pagination justify-content-center flex-wrap mb-0">
        <li class="page-item" [class.disabled]="page === 0">
          <button type="button" class="page-link" aria-label="Previous page" [disabled]="page === 0" (click)="go(page - 1)">
            <selling-icon name="chevron-left" [size]="16"></selling-icon>
          </button>
        </li>
        <li *ngFor="let number of pages" class="page-item" [class.active]="number === page">
          <button
            type="button"
            class="page-link"
            [attr.aria-current]="number === page ? 'page' : null"
            [attr.aria-label]="'Page ' + (number + 1)"
            (click)="go(number)"
          >
            {{ number + 1 }}
          </button>
        </li>
        <li class="page-item" [class.disabled]="page >= totalPages - 1">
          <button
            type="button"
            class="page-link"
            aria-label="Next page"
            [disabled]="page >= totalPages - 1"
            (click)="go(page + 1)"
          >
            <selling-icon name="chevron-right" [size]="16"></selling-icon>
          </button>
        </li>
      </ul>
    </nav>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingPaginationComponent {
  /** Zero-based index of the current page */
  @Input()
  page = 0;

  @Input()
  totalPages = 0;

  @Output()
  pageChange = new EventEmitter<number>();

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, index) => index);
  }

  go(page: number) {
    if (page !== this.page && page >= 0 && page < this.totalPages) {
      this.pageChange.emit(page);
    }
  }
}
