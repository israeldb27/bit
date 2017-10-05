import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Custodia } from './custodia.model';
import { CustodiaPopupService } from './custodia-popup.service';
import { CustodiaService } from './custodia.service';

@Component({
    selector: 'jhi-custodia-delete-dialog',
    templateUrl: './custodia-delete-dialog.component.html'
})
export class CustodiaDeleteDialogComponent {

    custodia: Custodia;

    constructor(
        private custodiaService: CustodiaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.custodiaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'custodiaListModification',
                content: 'Deleted an custodia'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-custodia-delete-popup',
    template: ''
})
export class CustodiaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private custodiaPopupService: CustodiaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.custodiaPopupService
                .open(CustodiaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
