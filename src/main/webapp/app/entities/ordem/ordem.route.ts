import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OrdemComponent } from './ordem.component';
import { OrdemDetailComponent } from './ordem-detail.component';
import { OrdemPopupComponent } from './ordem-dialog.component';
import { OrdemDeletePopupComponent } from './ordem-delete-dialog.component';

export const ordemRoute: Routes = [
    {
        path: 'ordem',
        component: OrdemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.ordem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ordem/:id',
        component: OrdemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.ordem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ordemPopupRoute: Routes = [
    {
        path: 'ordem-new',
        component: OrdemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.ordem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ordem/:id/edit',
        component: OrdemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.ordem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ordem/:id/delete',
        component: OrdemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.ordem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
