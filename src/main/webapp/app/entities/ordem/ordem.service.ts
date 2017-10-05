import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Ordem } from './ordem.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OrdemService {

    private resourceUrl = SERVER_API_URL + 'api/ordems';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/ordems';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(ordem: Ordem): Observable<Ordem> {
        const copy = this.convert(ordem);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(ordem: Ordem): Observable<Ordem> {
        const copy = this.convert(ordem);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Ordem> {
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
        entity.dataOrdem = this.dateUtils
            .convertDateTimeFromServer(entity.dataOrdem);
    }

    private convert(ordem: Ordem): Ordem {
        const copy: Ordem = Object.assign({}, ordem);

        copy.dataOrdem = this.dateUtils.toDate(ordem.dataOrdem);
        return copy;
    }
}
