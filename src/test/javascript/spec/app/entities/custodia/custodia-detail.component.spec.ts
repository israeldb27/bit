/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { BcbTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CustodiaDetailComponent } from '../../../../../../main/webapp/app/entities/custodia/custodia-detail.component';
import { CustodiaService } from '../../../../../../main/webapp/app/entities/custodia/custodia.service';
import { Custodia } from '../../../../../../main/webapp/app/entities/custodia/custodia.model';

describe('Component Tests', () => {

    describe('Custodia Management Detail Component', () => {
        let comp: CustodiaDetailComponent;
        let fixture: ComponentFixture<CustodiaDetailComponent>;
        let service: CustodiaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BcbTestModule],
                declarations: [CustodiaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CustodiaService,
                    JhiEventManager
                ]
            }).overrideTemplate(CustodiaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CustodiaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustodiaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Custodia(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.custodia).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
