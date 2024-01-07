import { ColumnsDefinition, Product } from '@selling-frontend/domain';

export function getProductsColumnsDefinition(defs: {
  deleteProduct: (row: Product) => void;
  updateProduct: (row: Product) => void;
}): ColumnsDefinition[] {
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
      columnAction: [
        {
          label: 'Edit',
          onClick: defs.updateProduct,
        },
        {
          label: 'Delete',
          onClick: defs.deleteProduct,
        },
      ],
    },
  ];
}
