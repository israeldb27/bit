import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CustodiaComponent } from './custodia.component';
import { CustodiaDetailComponent } from './custodia-detail.component';
import { CustodiaPopupComponent } from './custodia-dialog.component';
import { CustodiaDeletePopupComponent } from './custodia-delete-dialog.component';

export const custodiaRoute: Routes = [
    {
        path: 'custodia',
        component: CustodiaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.custodia.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'custodia/:id',
        component: CustodiaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.custodia.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const custodiaPopupRoute: Routes = [
    {
        path: 'custodia-new',
        component: CustodiaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.custodia.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'custodia/:id/edit',
        component: CustodiaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.custodia.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'custodia/:id/delete',
        component: CustodiaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bcbApp.custodia.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
