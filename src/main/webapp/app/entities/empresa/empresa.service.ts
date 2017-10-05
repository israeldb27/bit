import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Empresa } from './empresa.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmpresaService {

    private resourceUrl = SERVER_API_URL + 'api/empresas';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/empresas';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(empresa: Empresa): Observable<Empresa> {
        const copy = this.convert(empresa);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(empresa: Empresa): Observable<Empresa> {
        const copy = this.convert(empresa);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Empresa> {
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
        entity.dataCadastro = this.dateUtils
            .convertLocalDateFromServer(entity.dataCadastro);
        entity.dataUltimaAtualizacao = this.dateUtils
            .convertLocalDateFromServer(entity.dataUltimaAtualizacao);
    }

    private convert(empresa: Empresa): Empresa {
        const copy: Empresa = Object.assign({}, empresa);
        copy.dataCadastro = this.dateUtils
            .convertLocalDateToServer(empresa.dataCadastro);
        copy.dataUltimaAtualizacao = this.dateUtils
            .convertLocalDateToServer(empresa.dataUltimaAtualizacao);
        return copy;
    }
}
