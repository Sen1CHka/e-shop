/* eslint-disable @angular-eslint/component-selector */
import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent, SellingTableComponent } from '@selling-frontend/shared';
import { ColumnsDefinition, User, UserService } from '@selling-frontend/domain';
import { BehaviorSubject } from 'rxjs';
import { getUserColumnsDefinition } from './selling-users.columns-definition';
import { UserFormComponent } from './components';

@Component({
  selector: 'selling-users',
  standalone: true,
  imports: [
    CommonModule,
    SellingButtonComponent,
    SellingTableComponent,
    UserFormComponent
  ],
  templateUrl: './selling-users.component.html',
  styleUrl: './selling-users.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingUsersComponent implements OnInit{
  private readonly userService = inject(UserService);
  
  data$ = new BehaviorSubject<User[]>([]);
  isDialogVisible$ = new BehaviorSubject(false);
  isEditDialogVisible$ = new BehaviorSubject(false);
  currentEditableUser$ = new BehaviorSubject<User | undefined>(undefined);
  usersColumnsDefinition!: ColumnsDefinition[];

  ngOnInit(): void {
    this.loadUsers();
    this.usersColumnsDefinition = getUserColumnsDefinition({
      deleteUser: (row: User) => this.deleteUser(row),
      updateUser: (row: User) => {
        this.isEditDialogVisible$.next(true);
        this.currentEditableUser$.next(row);
      },
    });
  }

  closeDialog(value: boolean) {
    this.isDialogVisible$.next(value);
  }

  updateUsers(isSaved: boolean) {
    if (isSaved) {
      this.loadUsers();
    }
  }

  createUsers(){
    this.isEditDialogVisible$.next(true);
    this.currentEditableUser$.next(undefined);
  }

  filterData(filterObject: any) {
    this.userService.filterAll(filterObject).subscribe(
      data => this.data$.next(data)
    );
  }

  private loadUsers() {
    this.userService.getAll().subscribe(
      data => this.data$.next(data)
    );
  }

  private deleteUser(row: User) {
    if (row && row.username) {
      this.userService
        .deleteWithUsername(row.username)
        .subscribe(() => this.updateUsers(true));
    }
  }
}
