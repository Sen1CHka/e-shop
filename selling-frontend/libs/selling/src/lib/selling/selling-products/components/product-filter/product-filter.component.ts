import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnInit, Output, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SellingButtonComponent } from '@selling-frontend/shared';

@Component({
  selector: 'selling-product-filter',
  templateUrl: './product-filter.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SellingButtonComponent],
})
export class ProductFilterComponent implements OnInit {

  @Output()
  filterObject = new EventEmitter<any>();

  formGroup!: FormGroup;

  ngOnInit(): void {
    this.formGroup = this.createFormGroup();
  }

  filterProduct() {
    this.filterObject.emit(this.formGroup.value);
  }

  private createFormGroup(): FormGroup {
    return new FormGroup({
      price: new FormControl(null),
    });
  }
}
