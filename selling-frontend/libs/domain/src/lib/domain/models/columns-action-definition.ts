export interface ColumnActionDefinition{
    label?: string;
    onClick?: (row: any) => any;
    routerLink?: string;
    queryParams?: { [key: string]: any };
}