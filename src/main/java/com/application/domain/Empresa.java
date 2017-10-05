package com.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.application.domain.enumeration.StatusEmpresaEnum;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "data_ultima_atualizacao")
    private LocalDate dataUltimaAtualizacao;

    @Column(name = "cnpj")
    private Long cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEmpresaEnum status;

    @Column(name = "quantidade_acoes")
    private Long quantidadeAcoes;

    @Column(name = "valor_inicial")
    private Double valorInicial;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public Empresa razaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
        return this;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNome() {
        return nome;
    }

    public Empresa nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Empresa dataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public Empresa dataUltimaAtualizacao(LocalDate dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
        return this;
    }

    public void setDataUltimaAtualizacao(LocalDate dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public Long getCnpj() {
        return cnpj;
    }

    public Empresa cnpj(Long cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public StatusEmpresaEnum getStatus() {
        return status;
    }

    public Empresa status(StatusEmpresaEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusEmpresaEnum status) {
        this.status = status;
    }

    public Long getQuantidadeAcoes() {
        return quantidadeAcoes;
    }

    public Empresa quantidadeAcoes(Long quantidadeAcoes) {
        this.quantidadeAcoes = quantidadeAcoes;
        return this;
    }

    public void setQuantidadeAcoes(Long quantidadeAcoes) {
        this.quantidadeAcoes = quantidadeAcoes;
    }

    public Double getValorInicial() {
        return valorInicial;
    }

    public Empresa valorInicial(Double valorInicial) {
        this.valorInicial = valorInicial;
        return this;
    }

    public void setValorInicial(Double valorInicial) {
        this.valorInicial = valorInicial;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empresa empresa = (Empresa) o;
        if (empresa.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), empresa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", razaoSocial='" + getRazaoSocial() + "'" +
            ", nome='" + getNome() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataUltimaAtualizacao='" + getDataUltimaAtualizacao() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", status='" + getStatus() + "'" +
            ", quantidadeAcoes='" + getQuantidadeAcoes() + "'" +
            ", valorInicial='" + getValorInicial() + "'" +
            "}";
    }
}
