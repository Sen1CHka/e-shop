import { ColumnsDefinition, Product } from "@selling-frontend/domain";

export function getProductsColumnsDefinition() : ColumnsDefinition[]{
    return [
        {
            headerName: 'Id',
            value: 'id',
        },
        {
            headerName: 'Name',
            value: 'name',
        },
        {
            headerName: 'Description',
            value: 'description',
        },
        {
            headerName: 'Price',
            value: 'price',
        },
        {
            headerName: 'Amount',
            value: 'availableAmount',
        },
        {
            columnAction: [{
                label: 'Edit'
            }]
        },
        {
            columnAction: [{
                label: 'Delete'
            }]
        }
    ];
}