import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BcbOrdemModule } from './ordem/ordem.module';
import { BcbCustodiaModule } from './custodia/custodia.module';
import { BcbEmpresaModule } from './empresa/empresa.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        BcbOrdemModule,
        BcbCustodiaModule,
        BcbEmpresaModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcbEntityModule {}
