import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '@selling-frontend/domain';
import { SellingConfirmComponent, SellingToastsComponent } from '@selling-frontend/shared';

@Component({
  standalone: true,
  imports: [RouterModule, SellingToastsComponent, SellingConfirmComponent],
  selector: 'selling-frontend-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  private readonly auth = inject(AuthService);

  title = 'selling-frontend';

  ngOnInit(): void {
    this.auth.restoreSession();
  }
}
