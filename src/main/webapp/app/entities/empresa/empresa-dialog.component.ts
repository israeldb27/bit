import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Empresa } from './empresa.model';
import { EmpresaPopupService } from './empresa-popup.service';
import { EmpresaService } from './empresa.service';

@Component({
    selector: 'jhi-empresa-dialog',
    templateUrl: './empresa-dialog.component.html'
})
export class EmpresaDialogComponent implements OnInit {

    empresa: Empresa;
    isSaving: boolean;
    dataCadastroDp: any;
    dataUltimaAtualizacaoDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private empresaService: EmpresaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.empresa.id !== undefined) {
            this.subscribeToSaveResponse(
                this.empresaService.update(this.empresa));
        } else {
            this.subscribeToSaveResponse(
                this.empresaService.create(this.empresa));
        }
    }

    private subscribeToSaveResponse(result: Observable<Empresa>) {
        result.subscribe((res: Empresa) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Empresa) {
        this.eventManager.broadcast({ name: 'empresaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-empresa-popup',
    template: ''
})
export class EmpresaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private empresaPopupService: EmpresaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.empresaPopupService
                    .open(EmpresaDialogComponent as Component, params['id']);
            } else {
                this.empresaPopupService
                    .open(EmpresaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
