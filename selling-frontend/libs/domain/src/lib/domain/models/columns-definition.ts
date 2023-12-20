import { ColumnActionDefinition } from "./columns-action-definition";

export interface ColumnsDefinition{
    headerName?: string;
    value?: ((row: any) => any) | string;
    columnAction?: ColumnActionDefinition[];
    widthClass?: string;
}