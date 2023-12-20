import { ColumnsDefinition, Order } from "@selling-frontend/domain";

export function getOrdersColumnsDefinition(defs: { makeDialogVisible: () => void }) : ColumnsDefinition[]{
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
            headerName: 'User Id',
            value: 'userId',
        },
        {
            headerName: 'User Name',
            value: 'userName',
        },
        {
            headerName: 'State',
            value: 'state',
        },
        {
            headerName: 'Price',
            value: 'totalPrice',
        },
        {
            headerName: 'Created Date',
            value: (row: Order) => row.createdDate.toDateString(),
        },
        {
            columnAction: [{
                label: 'Detail',
                onClick: defs.makeDialogVisible
            }]
        },
    ];
}


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
            value: 'amount',
        },
    ];
}