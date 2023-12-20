import { Route } from '@angular/router';

export const appRoutes: Route[] = [
    {
        path: '',
        loadChildren: () => import('@selling-frontend/selling').then(c => c.sellingRoutingShellRoutes),
    }
];
