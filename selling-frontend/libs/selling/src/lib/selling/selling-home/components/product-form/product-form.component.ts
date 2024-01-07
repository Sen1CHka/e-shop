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
import { Product, ProductService } from '@selling-frontend/domain';
import {
  SellingButtonComponent,
  SellingDialogComponent,
} from '@selling-frontend/shared';
import { BehaviorSubject, take } from 'rxjs';

@Component({
  selector: 'selling-product-form',
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    SellingDialogComponent,
    ReactiveFormsModule,
    SellingButtonComponent,
  ],
})
export class ProductFormComponent implements OnInit, OnChanges {
  @Input() isDialogVisible$ = new BehaviorSubject(false);
  @Input() product?: Product;
  @Output() isProductSaved = new EventEmitter<boolean>(false);

  private readonly productService = inject(ProductService);

  formGroup!: FormGroup;

  ngOnInit(): void {
    this.formGroup = this.createFormGroup();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['product'] && this.formGroup) {
      this.formGroup.patchValue(changes['product'].currentValue);
    }
  }

  saveProduct() {
    if (this.product?.id || this.product?.id === 0) {
      return this.productService
        .update(this.product?.id, this.mapToProduct())
        .pipe(take(1))
        .subscribe(() => {
          this.isProductSaved.emit(true);
          this.closeDialog();
        });
    }
    return this.productService
      .create(this.mapToProduct())
      .pipe(take(1))
      .subscribe(() => {
        this.isProductSaved.emit(true);
        this.closeDialog();
      });
  }

  closeDialog() {
    this.isDialogVisible$.next(false);
    this.formGroup.reset();
    if (this.product) {
      this.formGroup.patchValue(this.product);
    }
  }

  private createFormGroup(): FormGroup {
    const formGroup = new FormGroup({
      name: new FormControl(this.product?.name ?? null, Validators.required),
      description: new FormControl(
        this.product?.description ?? null,
        Validators.required
      ),
      price: new FormControl(this.product?.price ?? null, Validators.required),
      availableAmount: new FormControl(
        this.product?.availableAmount ?? null,
        Validators.required
      ),
    });
    return formGroup;
  }

  private mapToProduct(): Product {
    return {
      id: this.product?.id,
      name: this.formGroup.get('name')?.value,
      description: this.formGroup.get('description')?.value,
      price: this.formGroup.get('price')?.value,
      availableAmount: this.formGroup.get('availableAmount')?.value,
    };
  }
}
