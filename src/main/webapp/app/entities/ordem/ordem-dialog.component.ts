import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Ordem } from './ordem.model';
import { OrdemPopupService } from './ordem-popup.service';
import { OrdemService } from './ordem.service';
import { Custodia, CustodiaService } from '../custodia';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-ordem-dialog',
    templateUrl: './ordem-dialog.component.html'
})
export class OrdemDialogComponent implements OnInit {

    ordem: Ordem;
    isSaving: boolean;

    custodias: Custodia[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private ordemService: OrdemService,
        private custodiaService: CustodiaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.custodiaService.query()
            .subscribe((res: ResponseWrapper) => { this.custodias = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.ordem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ordemService.update(this.ordem));
        } else {
            this.subscribeToSaveResponse(
                this.ordemService.create(this.ordem));
        }
    }

    private subscribeToSaveResponse(result: Observable<Ordem>) {
        result.subscribe((res: Ordem) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Ordem) {
        this.eventManager.broadcast({ name: 'ordemListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackCustodiaById(index: number, item: Custodia) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-ordem-popup',
    template: ''
})
export class OrdemPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ordemPopupService: OrdemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ordemPopupService
                    .open(OrdemDialogComponent as Component, params['id']);
            } else {
                this.ordemPopupService
                    .open(OrdemDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
