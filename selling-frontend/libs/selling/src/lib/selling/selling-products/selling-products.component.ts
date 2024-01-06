/* eslint-disable @typescript-eslint/no-unused-vars */
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  SellingButtonComponent,
  SellingDialogComponent,
  SellingTableComponent,
} from '@selling-frontend/shared';
import {
  ColumnsDefinition,
  Product,
  ProductService,
} from '@selling-frontend/domain';
import { BehaviorSubject, Observable, map, take } from 'rxjs';
import { getProductsColumnsDefinition } from './selling-products.columns-definition';
import { ProductFormComponent } from '../selling-home/components';

@Component({
  selector: 'selling-products',
  standalone: true,
  imports: [
    CommonModule,
    SellingButtonComponent,
    SellingTableComponent,
    SellingDialogComponent,
    ProductFormComponent,
  ],
  templateUrl: './selling-products.component.html',
  styleUrl: './selling-products.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingProductsComponent implements OnInit {
  private readonly productService = inject(ProductService);

  isDialogVisible$ = new BehaviorSubject(false);
  isEditDialogVisible$ = new BehaviorSubject(false);
  currentEditableProduct$ = new BehaviorSubject<Product | undefined>(undefined);

  data$?: Observable<Product[]>;
  productsColumnsDefinition!: ColumnsDefinition[];

  ngOnInit(): void {
    this.data$ = this.productService.getAll();
    this.productsColumnsDefinition = getProductsColumnsDefinition({
      deleteProduct: (row: Product) => this.deleteProduct(row),
      updateProduct: (row: Product) => {
        this.isEditDialogVisible$.next(true);
        this.currentEditableProduct$.next(row);
      },
    });
  }

  closeDialog(value: boolean) {
    this.isDialogVisible$.next(value);
  }

  private deleteProduct(row: Product) {
    if (row.id || row.id === 0) {
      this.productService
        .delete(row.id)
        .pipe(take(1))
        .subscribe();
    }
  }
}
