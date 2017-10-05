import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Custodia } from './custodia.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CustodiaService {

    private resourceUrl = SERVER_API_URL + 'api/custodias';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/custodias';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(custodia: Custodia): Observable<Custodia> {
        const copy = this.convert(custodia);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(custodia: Custodia): Observable<Custodia> {
        const copy = this.convert(custodia);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Custodia> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.dataAbertura = this.dateUtils
            .convertDateTimeFromServer(entity.dataAbertura);
        entity.dataFechamento = this.dateUtils
            .convertDateTimeFromServer(entity.dataFechamento);
    }

    private convert(custodia: Custodia): Custodia {
        const copy: Custodia = Object.assign({}, custodia);

        copy.dataAbertura = this.dateUtils.toDate(custodia.dataAbertura);

        copy.dataFechamento = this.dateUtils.toDate(custodia.dataFechamento);
        return copy;
    }
}
