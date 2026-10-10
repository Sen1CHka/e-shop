import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import {
  CategoryService,
  ConfirmService,
  Page,
  PricePipe,
  Product,
  ProductService,
  ToastService,
  errorMessage,
  fieldErrors,
} from '@selling-frontend/domain';
import {
  SellingDialogComponent,
  SellingEmptyStateComponent,
  SellingFieldComponent,
  SellingIconComponent,
  SellingPaginationComponent,
  SellingProductImageComponent,
  StockInfo,
  applyServerErrors,
  stockInfo,
} from '@selling-frontend/shared';

const PAGE_SIZE = 10;

@Component({
  selector: 'selling-products',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PricePipe,
    SellingIconComponent,
    SellingDialogComponent,
    SellingFieldComponent,
    SellingPaginationComponent,
    SellingProductImageComponent,
    SellingEmptyStateComponent,
  ],
  templateUrl: './selling-products.component.html',
  styleUrl: './selling-products.component.scss',
})
export class SellingProductsComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly categoryService = inject(CategoryService);
  private readonly confirm = inject(ConfirmService);
  private readonly toasts = inject(ToastService);
  private readonly route = inject(ActivatedRoute);

  readonly categories = this.categoryService.categories;
  readonly page = signal<Page<Product> | null>(null);

  query = '';
  categoryId: number | null = null;
  editing: Product | null = null;
  isDialogVisible = false;
  isSaving = false;

  form = this.createForm();

  ngOnInit(): void {
    this.query = this.route.snapshot.queryParamMap.get('q') ?? '';
    this.load();
  }

  search() {
    this.load(0);
  }

  load(pageNumber = this.page()?.page ?? 0) {
    this.productService
      .searchAll({ q: this.query.trim(), categoryId: this.categoryId, sort: 'name', page: pageNumber, size: PAGE_SIZE })
      .subscribe({
        next: (page) => {
          // Removing the last row of the last page would otherwise leave an empty table
          if (page.content.length === 0 && page.page > 0) {
            this.load(page.page - 1);
          } else {
            this.page.set(page);
          }
        },
        error: (error) => this.toasts.error(errorMessage(error)),
      });
  }

  stock(product: Product): StockInfo {
    return stockInfo(product.availableAmount);
  }

  open(product?: Product) {
    this.editing = product ?? null;
    this.form = this.createForm(product);
    this.isDialogVisible = true;
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    const product = {
      ...value,
      // The form is valid, so the numeric fields are filled
      price: value.price ?? 0,
      availableAmount: value.availableAmount ?? 0,
      brand: value.brand.trim() || null,
      imageUrl: value.imageUrl.trim() || null,
      description: value.description.trim() || null,
    };
    const request = this.editing
      ? this.productService.update(this.editing.id, product)
      : this.productService.create(product);
    this.isSaving = true;
    request.subscribe({
      next: (saved) => {
        this.isSaving = false;
        this.isDialogVisible = false;
        this.toasts.success(`${saved.name} has been saved`);
        this.load();
        // Product counts shown next to the categories may have changed
        this.categoryService.reload();
      },
      error: (error) => {
        this.isSaving = false;
        this.toasts.error(errorMessage(error));
        applyServerErrors(this.form, fieldErrors(error));
      },
    });
  }

  async remove(product: Product) {
    const confirmed = await this.confirm.ask({
      title: `Remove ${product.name} from the shop?`,
      text: 'Customers will no longer see or buy it. Existing orders keep it, and you can put it back on sale by editing it.',
      confirmLabel: 'Remove from the shop',
      danger: true,
    });
    if (!confirmed) {
      return;
    }
    this.productService.delete(product.id).subscribe({
      next: () => {
        this.toasts.success(`${product.name} is no longer on sale`);
        this.load();
        this.categoryService.reload();
      },
      error: (error) => this.toasts.error(errorMessage(error)),
    });
  }

  trackById(index: number, product: Product) {
    return product.id;
  }

  private createForm(product?: Product) {
    return new FormGroup({
      name: new FormControl(product?.name ?? '', {
        nonNullable: true,
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      brand: new FormControl(product?.brand ?? '', { nonNullable: true, validators: Validators.maxLength(80) }),
      categoryId: new FormControl(product?.categoryId ?? null),
      price: new FormControl(product?.price ?? null, [Validators.required, Validators.min(0.01)]),
      availableAmount: new FormControl(product?.availableAmount ?? null, [
        Validators.required,
        Validators.min(0),
        Validators.pattern(/^\d+$/),
      ]),
      imageUrl: new FormControl(product?.imageUrl ?? '', { nonNullable: true, validators: Validators.maxLength(1000) }),
      description: new FormControl(product?.description ?? '', {
        nonNullable: true,
        validators: Validators.maxLength(2000),
      }),
      active: new FormControl(product?.active ?? true, { nonNullable: true }),
    });
  }
}
