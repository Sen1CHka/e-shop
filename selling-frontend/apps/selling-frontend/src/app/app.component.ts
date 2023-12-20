import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SellingButtonComponent } from '@selling-frontend/shared';

@Component({
  standalone: true,
  imports: [RouterModule, SellingButtonComponent],
  selector: 'selling-frontend-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'selling-frontend';
}
