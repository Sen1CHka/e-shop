import { Route } from '@angular/router';

export const appRoutes: Route[] = [
    {
        path: '',
        loadChildren: () => import('@selling-frontend/shell').then(c => c.sellingRoutingShellRoutes),
    }
];
