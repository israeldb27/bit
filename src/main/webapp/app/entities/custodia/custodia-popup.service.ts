import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Custodia } from './custodia.model';
import { CustodiaService } from './custodia.service';

@Injectable()
export class CustodiaPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private custodiaService: CustodiaService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.custodiaService.find(id).subscribe((custodia) => {
                    custodia.dataAbertura = this.datePipe
                        .transform(custodia.dataAbertura, 'yyyy-MM-ddTHH:mm:ss');
                    custodia.dataFechamento = this.datePipe
                        .transform(custodia.dataFechamento, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.custodiaModalRef(component, custodia);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.custodiaModalRef(component, new Custodia());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    custodiaModalRef(component: Component, custodia: Custodia): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.custodia = custodia;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
