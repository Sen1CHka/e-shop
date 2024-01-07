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
import { BehaviorSubject, Observable, map, switchMap, take } from 'rxjs';
import { getProductsColumnsDefinition } from './selling-products.columns-definition';
import { ProductFilterComponent, ProductFormComponent } from './components';

@Component({
  selector: 'selling-products',
  standalone: true,
  imports: [
    CommonModule,
    SellingButtonComponent,
    SellingTableComponent,
    SellingDialogComponent,
    ProductFormComponent,
    ProductFilterComponent
  ],
  templateUrl: './selling-products.component.html',
  styleUrl: './selling-products.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingProductsComponent implements OnInit {

  private readonly productService = inject(ProductService);
  
  data$ = new BehaviorSubject<Product[]>([]);
  isDialogVisible$ = new BehaviorSubject(false);
  isEditDialogVisible$ = new BehaviorSubject(false);
  currentEditableProduct$ = new BehaviorSubject<Product | undefined>(undefined);
  productsColumnsDefinition!: ColumnsDefinition[];

  ngOnInit(): void {
    this.loadProducts();
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

  updateProducts(isSaved: boolean) {
    if (isSaved) {
      this.loadProducts();
    }
  }

  createProducts(){
    this.isEditDialogVisible$.next(true);
    this.currentEditableProduct$.next(undefined);
  }

  filterData(filterObject: any) {
    this.productService.filterAll(filterObject).subscribe(
      data => this.data$.next(data)
    );
  }

  private loadProducts() {
    this.productService.getAll().subscribe(
      data => this.data$.next(data)
    );
  }

  private deleteProduct(row: Product) {
    if (row && row.id !== null && row.id !== undefined) {
      this.productService
        .delete(row.id)
        .subscribe(() => this.updateProducts(true));
    }
  }
}
