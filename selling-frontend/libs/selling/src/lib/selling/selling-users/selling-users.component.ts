import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AdminService,
  AuthService,
  ConfirmService,
  Role,
  ToastService,
  User,
  errorMessage,
} from '@selling-frontend/domain';
import { SellingIconComponent } from '@selling-frontend/shared';

@Component({
  selector: 'selling-users',
  standalone: true,
  imports: [CommonModule, SellingIconComponent],
  template: `
    <h1 class="page-title">Users</h1>

    <div class="panel p-0">
      <div class="table-responsive" *ngIf="users() as list; else loading">
        <table class="table table-hover data-table">
          <caption class="visually-hidden">
            Users
          </caption>
          <thead>
            <tr>
              <th scope="col">Name</th>
              <th scope="col">Username</th>
              <th scope="col">E-mail</th>
              <th scope="col">Registered</th>
              <th scope="col">Role</th>
              <th scope="col"><span class="visually-hidden">Actions</span></th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let user of list; trackBy: trackById">
              <td>
                <strong>{{ user.realName }}</strong>
                <span *ngIf="isMe(user)" class="badge text-bg-light ms-2">You</span>
              </td>
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.createdAt | date : 'd MMM y' }}</td>
              <td>
                <label class="visually-hidden" [attr.for]="'role-' + user.id">Role of {{ user.realName }}</label>
                <select
                  class="form-select form-select-sm role-select"
                  [id]="'role-' + user.id"
                  [disabled]="isMe(user)"
                  [title]="isMe(user) ? 'You cannot change your own role' : ''"
                  #roleSelect
                  (change)="changeRole(user, roleSelect)"
                >
                  <option value="USER" [selected]="user.role === 'USER'">Customer</option>
                  <option value="ADMIN" [selected]="user.role === 'ADMIN'">Administrator</option>
                </select>
              </td>
              <td class="text-end">
                <button
                  *ngIf="!isMe(user)"
                  type="button"
                  class="icon-button danger"
                  [attr.aria-label]="'Delete ' + user.realName"
                  title="Delete user"
                  (click)="remove(user)"
                >
                  <selling-icon name="trash" [size]="18"></selling-icon>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <ng-template #loading><div class="skeleton m-3" style="height: 14rem"></div></ng-template>
    </div>
  `,
  styles: ['.role-select { min-width: 9rem; max-width: 11rem; }'],
})
export class SellingUsersComponent implements OnInit {
  private readonly adminService = inject(AdminService);
  private readonly auth = inject(AuthService);
  private readonly confirm = inject(ConfirmService);
  private readonly toasts = inject(ToastService);

  readonly users = signal<User[] | null>(null);

  ngOnInit(): void {
    this.load();
  }

  isMe(user: User): boolean {
    return user.id === this.auth.user()?.id;
  }

  async changeRole(user: User, select: HTMLSelectElement) {
    const role = select.value as Role;
    // Giving somebody full control over the shop deserves a second look
    if (role === 'ADMIN') {
      const confirmed = await this.confirm.ask({
        title: `Make ${user.realName} an administrator?`,
        text: 'Administrators can change products, orders and other users.',
        confirmLabel: 'Make administrator',
      });
      if (!confirmed) {
        select.value = user.role;
        return;
      }
    }
    this.adminService.changeRole(user.id, role).subscribe({
      next: (updated) => {
        this.users.update((users) => users && users.map((item) => (item.id === updated.id ? updated : item)));
        this.toasts.success(`${user.realName} is now ${role === 'ADMIN' ? 'an administrator' : 'a customer'}`);
      },
      error: (error) => {
        select.value = user.role;
        this.toasts.error(errorMessage(error));
      },
    });
  }

  async remove(user: User) {
    const confirmed = await this.confirm.ask({
      title: `Delete ${user.realName}?`,
      text: 'The account is deleted together with its orders, reviews and addresses. This cannot be undone.',
      confirmLabel: 'Delete user',
      danger: true,
    });
    if (!confirmed) {
      return;
    }
    this.adminService.deleteUser(user.id).subscribe({
      next: () => {
        this.users.update((users) => users && users.filter((item) => item.id !== user.id));
        this.toasts.success(`${user.realName} has been deleted`);
      },
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }

  trackById(index: number, user: User) {
    return user.id;
  }

  private load() {
    this.adminService.getUsers().subscribe({
      next: (users) => this.users.set(users),
      error: (error) => {
        this.users.set([]);
        this.toasts.error(errorMessage(error));
      },
    });
  }
}
