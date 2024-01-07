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
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Order, OrderEdit, OrderService, Product, ProductService, STATES } from '@selling-frontend/domain';
import {
  SellingDialogComponent,
  SellingButtonComponent,
} from '@selling-frontend/shared';
import { BehaviorSubject, Observable, take } from 'rxjs';
import {NgSelectModule} from '@ng-select/ng-select';

@Component({
  selector: 'selling-order-form',
  templateUrl: './order-form.component.html',
  imports: [
    CommonModule,
    SellingDialogComponent,
    ReactiveFormsModule,
    SellingButtonComponent,
    NgSelectModule,
    FormsModule
  ],
  standalone: true,
})
export class OrderFormComponent implements OnInit, OnChanges {
  @Input() isDialogVisible$ = new BehaviorSubject(false);
  @Input() order?: Order;
  @Output() isOrderSaved = new EventEmitter<boolean>(false);

private readonly orderService = inject(OrderService);
  private readonly productService = inject(ProductService);

  productList$!: Observable<Product[]>;
  stateList = STATES;

  formGroup!: FormGroup;

  ngOnInit(): void {
    this.productList$ = this.productService.getAll();
    this.formGroup = this.createFormGroup();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['order'] && this.formGroup) {
      this.formGroup.get('state')?.patchValue(changes['order'].currentValue.stateId);
      this.formGroup.get('products')?.patchValue(changes['order'].currentValue.products);
    }
  }

  saveOrder() {
    if (this.order?.id || this.order?.id === 0) {
      return this.orderService
        .updateOrder(this.order?.id, this.mapToOrder())
        .pipe(take(1))
        .subscribe(() => {
          this.isOrderSaved.emit(true);
          this.closeDialog();
        });
    }
    return this.orderService
      .createOrder(this.mapToOrder())
      .pipe(take(1))
      .subscribe(() => {
        this.isOrderSaved.emit(true);
        this.closeDialog();
      });
  }

  closeDialog() {
    this.isDialogVisible$.next(false);
    this.formGroup.reset();
    if (this.order) {
      this.formGroup.get('state')?.patchValue(this.order.stateId);
      this.formGroup.get('products')?.patchValue(this.order.products);
    }
    else{
      this.formGroup.get('state')?.patchValue(STATES[0].id);
    }
  }

  private createFormGroup(): FormGroup {
    return new FormGroup({
      state: new FormControl(this.order?.stateId ?? STATES[0].id, Validators.required),
      products: new FormControl(this.order?.products),
    });
  }

  private mapToOrder(): OrderEdit {
    return {
      state: this.formGroup.get('state')?.value ?? null,
      products: this.formGroup.get('products')?.value ?? null
    };
  }
}
