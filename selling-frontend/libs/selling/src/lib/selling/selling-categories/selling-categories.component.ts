import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  Category,
  CategoryService,
  ConfirmService,
  ToastService,
  errorMessage,
  fieldErrors,
} from '@selling-frontend/domain';
import {
  SellingDialogComponent,
  SellingEmptyStateComponent,
  SellingFieldComponent,
  SellingIconComponent,
  applyServerErrors,
} from '@selling-frontend/shared';

@Component({
  selector: 'selling-categories',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SellingIconComponent,
    SellingDialogComponent,
    SellingFieldComponent,
    SellingEmptyStateComponent,
  ],
  template: `
    <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
      <h1 class="page-title mb-0">Categories</h1>
      <button type="button" class="btn btn-primary" (click)="open()">
        <selling-icon name="plus" [size]="16"></selling-icon> Add category
      </button>
    </div>

    <div class="panel p-0">
      <selling-empty-state
        *ngIf="categories().length === 0"
        icon="tag"
        heading="No categories yet"
        text="Categories help customers find products faster."
      >
        <button type="button" class="btn btn-primary" (click)="open()">Add category</button>
      </selling-empty-state>

      <div class="table-responsive" *ngIf="categories().length">
        <table class="table table-hover data-table">
          <caption class="visually-hidden">
            Categories
          </caption>
          <thead>
            <tr>
              <th scope="col">Category</th>
              <th scope="col">Description</th>
              <th scope="col" class="text-end">Products on sale</th>
              <th scope="col"><span class="visually-hidden">Actions</span></th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let category of categories()">
              <td class="text-nowrap">
                <span class="category-icon" aria-hidden="true">{{ category.icon }}</span>
                <strong>{{ category.name }}</strong>
              </td>
              <td class="text-secondary">{{ category.description }}</td>
              <td class="text-end">{{ category.productCount }}</td>
              <td class="text-end text-nowrap">
                <button type="button" class="icon-button" [attr.aria-label]="'Edit ' + category.name" title="Edit" (click)="open(category)">
                  <selling-icon name="edit" [size]="18"></selling-icon>
                </button>
                <button
                  type="button"
                  class="icon-button danger"
                  [attr.aria-label]="'Delete ' + category.name"
                  title="Delete"
                  (click)="remove(category)"
                >
                  <selling-icon name="trash" [size]="18"></selling-icon>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <selling-dialog
      [isVisible]="isDialogVisible"
      [dialogTitle]="editing ? 'Edit category' : 'New category'"
      (closeDialogEvent)="isDialogVisible = false"
    >
      <form [formGroup]="form" (ngSubmit)="save()" novalidate>
        <div class="row gx-3 gy-1">
          <selling-field class="col-8" label="Name" for="category-name" [control]="form.controls.name">
            <input id="category-name" type="text" class="form-control" formControlName="name" />
          </selling-field>
          <selling-field class="col-4" label="Icon" for="category-icon" hint="One emoji" [optional]="true" [control]="form.controls.icon">
            <input id="category-icon" type="text" class="form-control" formControlName="icon" />
          </selling-field>
          <selling-field class="col-12" label="Description" for="category-description" [optional]="true" [control]="form.controls.description">
            <textarea id="category-description" class="form-control" rows="3" formControlName="description"></textarea>
          </selling-field>
        </div>
        <div class="d-flex justify-content-end gap-2 mt-4">
          <button type="button" class="btn btn-outline-secondary" (click)="isDialogVisible = false">Cancel</button>
          <button type="submit" class="btn btn-primary" [disabled]="isSaving">Save category</button>
        </div>
      </form>
    </selling-dialog>
  `,
  styles: ['.category-icon { display: inline-block; width: 2rem; font-size: 1.25rem; }'],
})
export class SellingCategoriesComponent {
  private readonly categoryService = inject(CategoryService);
  private readonly confirm = inject(ConfirmService);
  private readonly toasts = inject(ToastService);

  readonly categories = this.categoryService.categories;

  editing: Category | null = null;
  isDialogVisible = false;
  isSaving = false;
  form = this.createForm();

  open(category?: Category) {
    this.editing = category ?? null;
    this.form = this.createForm(category);
    this.isDialogVisible = true;
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    const category = {
      name: value.name.trim(),
      icon: value.icon.trim() || null,
      description: value.description.trim() || null,
    };
    const request = this.editing
      ? this.categoryService.update(this.editing.id, category)
      : this.categoryService.create(category);
    this.isSaving = true;
    request.subscribe({
      next: (saved) => {
        this.isSaving = false;
        this.isDialogVisible = false;
        this.toasts.success(`Category ${saved.name} has been saved`);
      },
      error: (error) => {
        this.isSaving = false;
        this.toasts.error(errorMessage(error));
        applyServerErrors(this.form, fieldErrors(error));
      },
    });
  }

  async remove(category: Category) {
    const confirmed = await this.confirm.ask({
      title: `Delete category ${category.name}?`,
      text: 'Only a category without products can be deleted. This cannot be undone.',
      confirmLabel: 'Delete category',
      danger: true,
    });
    if (!confirmed) {
      return;
    }
    this.categoryService.delete(category.id).subscribe({
      next: () => this.toasts.success(`Category ${category.name} has been deleted`),
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }

  private createForm(category?: Category) {
    return new FormGroup({
      name: new FormControl(category?.name ?? '', {
        nonNullable: true,
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      icon: new FormControl(category?.icon ?? '', { nonNullable: true, validators: Validators.maxLength(16) }),
      description: new FormControl(category?.description ?? '', {
        nonNullable: true,
        validators: Validators.maxLength(500),
      }),
    });
  }
}
