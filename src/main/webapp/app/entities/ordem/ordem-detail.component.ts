import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Ordem } from './ordem.model';
import { OrdemService } from './ordem.service';

@Component({
    selector: 'jhi-ordem-detail',
    templateUrl: './ordem-detail.component.html'
})
export class OrdemDetailComponent implements OnInit, OnDestroy {

    ordem: Ordem;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ordemService: OrdemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOrdems();
    }

    load(id) {
        this.ordemService.find(id).subscribe((ordem) => {
            this.ordem = ordem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOrdems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ordemListModification',
            (response) => this.load(this.ordem.id)
        );
    }
}
