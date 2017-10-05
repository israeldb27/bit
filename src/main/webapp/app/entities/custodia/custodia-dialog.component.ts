import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Custodia } from './custodia.model';
import { CustodiaPopupService } from './custodia-popup.service';
import { CustodiaService } from './custodia.service';

@Component({
    selector: 'jhi-custodia-dialog',
    templateUrl: './custodia-dialog.component.html'
})
export class CustodiaDialogComponent implements OnInit {

    custodia: Custodia;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private custodiaService: CustodiaService,
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
        if (this.custodia.id !== undefined) {
            this.subscribeToSaveResponse(
                this.custodiaService.update(this.custodia));
        } else {
            this.subscribeToSaveResponse(
                this.custodiaService.create(this.custodia));
        }
    }

    private subscribeToSaveResponse(result: Observable<Custodia>) {
        result.subscribe((res: Custodia) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Custodia) {
        this.eventManager.broadcast({ name: 'custodiaListModification', content: 'OK'});
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
    selector: 'jhi-custodia-popup',
    template: ''
})
export class CustodiaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private custodiaPopupService: CustodiaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.custodiaPopupService
                    .open(CustodiaDialogComponent as Component, params['id']);
            } else {
                this.custodiaPopupService
                    .open(CustodiaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
