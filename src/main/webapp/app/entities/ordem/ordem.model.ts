import { BaseEntity } from './../../shared';

export const enum StatusOrdemEnum {
    'CRIADO',
    'EXECUTADO',
    'CANCELADO',
    'SUSPENSO'
}

export class Ordem implements BaseEntity {
    constructor(
        public id?: number,
        public tipo?: string,
        public valorOrdem?: number,
        public quantidade?: number,
        public dataOrdem?: any,
        public status?: StatusOrdemEnum,
        public custodia?: BaseEntity,
    ) {
    }
}
