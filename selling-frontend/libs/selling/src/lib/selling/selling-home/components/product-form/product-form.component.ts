/* eslint-disable @angular-eslint/component-selector */
import { CommonModule } from "@angular/common";
import { Component, Input, OnInit, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Product, ProductService } from "@selling-frontend/domain";
import { SellingButtonComponent, SellingDialogComponent } from '@selling-frontend/shared';
import { BehaviorSubject, take } from "rxjs";

@Component({
    selector: 'selling-product-form',
    templateUrl: './product-form.component.html',
    standalone: true,
    imports: [CommonModule, SellingDialogComponent, ReactiveFormsModule, SellingButtonComponent]
})
export class ProductFormComponent implements OnInit {

    @Input() product?: Product;

    @Input() isDialogVisible$ = new BehaviorSubject(false);

    private readonly productService = inject(ProductService);

    formGroup!: FormGroup;

    ngOnInit(): void {
        this.formGroup = this.createFormGroup();
    }

    saveProduct(){
        if(this.product?.id || this.product?.id === 0){
            return this.productService.update(this.product?.id, this.mapToProduct()).pipe(take(1)).subscribe();
        }
        return this.productService.create(this.mapToProduct()).pipe(take(1)).subscribe();
    }

    closeDialog(){
        this.isDialogVisible$.next(false);
    }

    private createFormGroup(): FormGroup {
        const formGroup = new FormGroup({
            name: new FormControl(this.product?.name ?? null, Validators.required),
            description: new FormControl(this.product?.description ?? null, Validators.required),
            price: new FormControl(this.product?.price ?? null, Validators.required),
            availableAmount: new FormControl(this.product?.availableAmount ?? null, Validators.required),
        });
        return formGroup;
    }

    private mapToProduct(): Product {
        return {
            id: this.product?.id,
            name: this.formGroup.get('name')?.value,
            description: this.formGroup.get('description')?.value,
            price: this.formGroup.get('price')?.value,
            availableAmount: this.formGroup.get('availableAmount')?.value
        };
    }
}