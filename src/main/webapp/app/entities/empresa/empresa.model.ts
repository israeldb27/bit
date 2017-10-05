import { BaseEntity } from './../../shared';

export const enum StatusEmpresaEnum {
    'CRIADA',
    'CANCELADA',
    'SUSPENSA',
    'EXCLUIDA'
}

export class Empresa implements BaseEntity {
    constructor(
        public id?: number,
        public razaoSocial?: string,
        public nome?: string,
        public dataCadastro?: any,
        public dataUltimaAtualizacao?: any,
        public cnpj?: number,
        public status?: StatusEmpresaEnum,
        public quantidadeAcoes?: number,
        public valorInicial?: number,
    ) {
    }
}
