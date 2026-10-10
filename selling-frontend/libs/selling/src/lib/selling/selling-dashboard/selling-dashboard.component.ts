import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminService, PricePipe, STATES, Stats } from '@selling-frontend/domain';
import { SellingIconComponent, SellingOrderStateComponent } from '@selling-frontend/shared';

@Component({
  selector: 'selling-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, PricePipe, SellingIconComponent, SellingOrderStateComponent],
  templateUrl: './selling-dashboard.component.html',
  styleUrl: './selling-dashboard.component.scss',
})
export class SellingDashboardComponent implements OnInit {
  private readonly adminService = inject(AdminService);

  readonly states = STATES;
  readonly stats = signal<Stats | null>(null);
  readonly failed = signal(false);

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.failed.set(false);
    this.adminService.getStats().subscribe({
      next: (stats) => this.stats.set(stats),
      error: () => this.failed.set(true),
    });
  }

  share(count: number, total: number): number {
    return total === 0 ? 0 : (count / total) * 100;
  }
}
