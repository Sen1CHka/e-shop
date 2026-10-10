import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SellingEmptyStateComponent } from '@selling-frontend/shared';

@Component({
  selector: 'shop-not-found',
  standalone: true,
  imports: [RouterModule, SellingEmptyStateComponent],
  template: `
    <div class="panel">
      <selling-empty-state
        icon="search"
        heading="We could not find this page"
        text="The address may be mistyped or the page has been moved."
      >
        <div class="d-flex gap-2 justify-content-center flex-wrap">
          <a routerLink="/" class="btn btn-primary">Go to the home page</a>
          <a routerLink="/catalog" class="btn btn-outline-primary">Browse products</a>
        </div>
      </selling-empty-state>
    </div>
  `,
})
export class ShopNotFoundComponent {}
