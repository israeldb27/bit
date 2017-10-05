import { BaseEntity } from './../../shared';

export const enum TipoOrdermEnum {
    'COMPRA',
    'VENDA'
}

export const enum StatusOrdemEnum {
    'CRIADO',
    'EXECUTADO',
    'CANCELADO',
    'SUSPENSO'
}

export class Ordem implements BaseEntity {
    constructor(
        public id?: number,
        public tipo?: TipoOrdermEnum,
        public valorOrdem?: number,
        public quantidade?: number,
        public dataOrdem?: any,
        public status?: StatusOrdemEnum,
        public custodia?: BaseEntity,
    ) {
    }
}
