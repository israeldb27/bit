import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BcbSharedModule } from '../../shared';
import {
    OrdemService,
    OrdemPopupService,
    OrdemComponent,
    OrdemDetailComponent,
    OrdemDialogComponent,
    OrdemPopupComponent,
    OrdemDeletePopupComponent,
    OrdemDeleteDialogComponent,
    ordemRoute,
    ordemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ordemRoute,
    ...ordemPopupRoute,
];

@NgModule({
    imports: [
        BcbSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OrdemComponent,
        OrdemDetailComponent,
        OrdemDialogComponent,
        OrdemDeleteDialogComponent,
        OrdemPopupComponent,
        OrdemDeletePopupComponent,
    ],
    entryComponents: [
        OrdemComponent,
        OrdemDialogComponent,
        OrdemPopupComponent,
        OrdemDeleteDialogComponent,
        OrdemDeletePopupComponent,
    ],
    providers: [
        OrdemService,
        OrdemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcbOrdemModule {}
