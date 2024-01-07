import { ColumnsDefinition, User } from '@selling-frontend/domain';

export function getUserColumnsDefinition(defs: {
  deleteUser: (row: User) => void;
  updateUser: (row: User) => void;
}): ColumnsDefinition[] {
  return [
    {
      headerName: 'Name',
      value: 'realName',
    },
    {
      headerName: 'Email',
      value: 'email',
    },
    {
      headerName: 'Username',
      value: 'username',
    },
    {
      columnAction: [
        {
          label: 'Edit',
          onClick: defs.deleteUser,
        },
        {
          label: 'Delete',
          onClick: defs.deleteUser,
        },
      ],
    },
  ];
}
