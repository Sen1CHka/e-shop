import { ColumnsDefinition, Order } from '@selling-frontend/domain';

export function getOrdersColumnsDefinition(defs: {
  makeDialogVisible: (row: Order) => void;
  updateOrder: (row: Order) => void;
  deleteOrder: (row: Order) => void;
}): ColumnsDefinition[] {
  return [
    {
      headerName: 'Id',
      value: 'id',
    },
    {
      headerName: 'User Name',
      value: 'username',
    },
    {
      headerName: 'State',
      value: 'state',
    },

    {
      headerName: 'Created Date',
      value: (row: Order) => new Date(row.date).toLocaleDateString(),
    },
    {
      headerName: 'Total Price',
      value: 'totalPrice',
    },
    {
      columnAction: [
        {
          label: 'Detail',
          onClick: defs.makeDialogVisible,
        },
        {
          label: 'Edit',
          onClick: defs.updateOrder,
        },
        {
          label: 'Delete',
          onClick: defs.deleteOrder,
        },
      ],
    },
  ];
}

export function getProductsColumnsDefinition(): ColumnsDefinition[] {
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
