/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { BcbTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OrdemDetailComponent } from '../../../../../../main/webapp/app/entities/ordem/ordem-detail.component';
import { OrdemService } from '../../../../../../main/webapp/app/entities/ordem/ordem.service';
import { Ordem } from '../../../../../../main/webapp/app/entities/ordem/ordem.model';

describe('Component Tests', () => {

    describe('Ordem Management Detail Component', () => {
        let comp: OrdemDetailComponent;
        let fixture: ComponentFixture<OrdemDetailComponent>;
        let service: OrdemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BcbTestModule],
                declarations: [OrdemDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OrdemService,
                    JhiEventManager
                ]
            }).overrideTemplate(OrdemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrdemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdemService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Ordem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ordem).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
