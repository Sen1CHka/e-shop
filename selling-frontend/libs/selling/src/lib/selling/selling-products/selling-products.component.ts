import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellingButtonComponent, SellingDialogComponent, SellingTableComponent } from '@selling-frontend/shared';
import { ColumnsDefinition, Product, ProductService } from '@selling-frontend/domain';
import { BehaviorSubject, Observable, map } from 'rxjs';
import {getProductsColumnsDefinition } from './selling-products.columns-definition';

@Component({
  selector: 'selling-products',
  standalone: true,
  imports: [CommonModule, SellingButtonComponent, SellingTableComponent, SellingDialogComponent],
  templateUrl: './selling-products.component.html',
  styleUrl: './selling-products.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingProductsComponent implements OnInit{

  private readonly orderService = inject(ProductService);

  isDialogVisible$ = new BehaviorSubject(false);
  data$?: Observable<Product[]>;

  productsColumnsDefinition!: ColumnsDefinition[];


  ngOnInit(): void {
    this.data$ = this.orderService.getAll();
    this.productsColumnsDefinition = getProductsColumnsDefinition();
  }

  closeDialog(value: boolean){
    this.isDialogVisible$.next(value);
  }
}


