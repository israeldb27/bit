import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BcbSharedModule } from '../../shared';
import {
    CustodiaService,
    CustodiaPopupService,
    CustodiaComponent,
    CustodiaDetailComponent,
    CustodiaDialogComponent,
    CustodiaPopupComponent,
    CustodiaDeletePopupComponent,
    CustodiaDeleteDialogComponent,
    custodiaRoute,
    custodiaPopupRoute,
} from './';

const ENTITY_STATES = [
    ...custodiaRoute,
    ...custodiaPopupRoute,
];

@NgModule({
    imports: [
        BcbSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CustodiaComponent,
        CustodiaDetailComponent,
        CustodiaDialogComponent,
        CustodiaDeleteDialogComponent,
        CustodiaPopupComponent,
        CustodiaDeletePopupComponent,
    ],
    entryComponents: [
        CustodiaComponent,
        CustodiaDialogComponent,
        CustodiaPopupComponent,
        CustodiaDeleteDialogComponent,
        CustodiaDeletePopupComponent,
    ],
    providers: [
        CustodiaService,
        CustodiaPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcbCustodiaModule {}
