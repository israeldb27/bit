import { BaseEntity } from './../../shared';

export const enum StatusCustodiaEmpresa {
    'ABERTA',
    'FECHADA',
    'CANCELADA'
}

export class Custodia implements BaseEntity {
    constructor(
        public id?: number,
        public status?: StatusCustodiaEmpresa,
        public dataAbertura?: any,
        public dataFechamento?: any,
        public quantidadeTotal?: number,
        public valorTotal?: number,
        public ordems?: BaseEntity[],
    ) {
    }
}
