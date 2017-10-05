import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BcbSharedModule } from '../../shared';
import {
    EmpresaService,
    EmpresaPopupService,
    EmpresaComponent,
    EmpresaDetailComponent,
    EmpresaDialogComponent,
    EmpresaPopupComponent,
    EmpresaDeletePopupComponent,
    EmpresaDeleteDialogComponent,
    empresaRoute,
    empresaPopupRoute,
} from './';

const ENTITY_STATES = [
    ...empresaRoute,
    ...empresaPopupRoute,
];

@NgModule({
    imports: [
        BcbSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EmpresaComponent,
        EmpresaDetailComponent,
        EmpresaDialogComponent,
        EmpresaDeleteDialogComponent,
        EmpresaPopupComponent,
        EmpresaDeletePopupComponent,
    ],
    entryComponents: [
        EmpresaComponent,
        EmpresaDialogComponent,
        EmpresaPopupComponent,
        EmpresaDeleteDialogComponent,
        EmpresaDeletePopupComponent,
    ],
    providers: [
        EmpresaService,
        EmpresaPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcbEmpresaModule {}
