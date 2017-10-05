import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Ordem } from './ordem.model';
import { OrdemPopupService } from './ordem-popup.service';
import { OrdemService } from './ordem.service';

@Component({
    selector: 'jhi-ordem-delete-dialog',
    templateUrl: './ordem-delete-dialog.component.html'
})
export class OrdemDeleteDialogComponent {

    ordem: Ordem;

    constructor(
        private ordemService: OrdemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ordemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ordemListModification',
                content: 'Deleted an ordem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ordem-delete-popup',
    template: ''
})
export class OrdemDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ordemPopupService: OrdemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ordemPopupService
                .open(OrdemDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
