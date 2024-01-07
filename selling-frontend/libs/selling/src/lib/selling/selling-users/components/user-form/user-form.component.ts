/* eslint-disable @angular-eslint/component-selector */
import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  inject,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { User, UserService } from '@selling-frontend/domain';
import {
  SellingButtonComponent,
  SellingDialogComponent,
} from '@selling-frontend/shared';
import { BehaviorSubject, take } from 'rxjs';

@Component({
  selector: 'selling-user-form',
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    SellingDialogComponent,
    ReactiveFormsModule,
    SellingButtonComponent,
  ],
})
export class UserFormComponent implements OnInit, OnChanges {
  @Input() isDialogVisible$ = new BehaviorSubject(false);
  @Input() user?: User;
  @Output() isUserSaved = new EventEmitter<boolean>(false);

  private readonly userService = inject(UserService);

  formGroup!: FormGroup;

  ngOnInit(): void {
    this.formGroup = this.createFormGroup();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user'] && this.formGroup) {
      this.formGroup.patchValue(changes['user'].currentValue);
    }
  }

  saveUser() {
    if (this.user?.username) {
      return this.userService
        .updateWithUsername(this.user?.username, this.mapToUser())
        .pipe(take(1))
        .subscribe(() => {
          this.isUserSaved.emit(true);
          this.closeDialog();
        });
    }
    return this.userService
      .create(this.mapToUser())
      .pipe(take(1))
      .subscribe(() => {
        this.isUserSaved.emit(true);
        this.closeDialog();
      });
  }

  closeDialog() {
    this.isDialogVisible$.next(false);
    this.formGroup.reset();
    if (this.user) {
      this.formGroup.patchValue(this.user);
    }
  }

  private createFormGroup(): FormGroup {
    const formGroup = new FormGroup({
      realName: new FormControl(
        this.user?.realName ?? null,
        Validators.required
      ),
      username: new FormControl(
        this.user?.username ?? null,
        Validators.required
      ),
      email: new FormControl(this.user?.email ?? null, Validators.required),
      password: new FormControl(
        null,
        Validators.required
      ),
    });
    return formGroup;
  }

  private mapToUser(): User {
    return {
      realName: this.formGroup.get('realName')?.value,
      username: this.formGroup.get('username')?.value,
      email: this.formGroup.get('email')?.value,
      password: this.formGroup.get('password')?.value,
    };
  }
}
