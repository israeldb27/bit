import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Custodia } from './custodia.model';
import { CustodiaService } from './custodia.service';

@Component({
    selector: 'jhi-custodia-detail',
    templateUrl: './custodia-detail.component.html'
})
export class CustodiaDetailComponent implements OnInit, OnDestroy {

    custodia: Custodia;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private custodiaService: CustodiaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCustodias();
    }

    load(id) {
        this.custodiaService.find(id).subscribe((custodia) => {
            this.custodia = custodia;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCustodias() {
        this.eventSubscriber = this.eventManager.subscribe(
            'custodiaListModification',
            (response) => this.load(this.custodia.id)
        );
    }
}
