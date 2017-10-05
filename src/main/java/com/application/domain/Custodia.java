package com.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.application.domain.enumeration.StatusCustodiaEmpresa;

/**
 * A Custodia.
 */
@Entity
@Table(name = "custodia")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "custodia")
public class Custodia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCustodiaEmpresa status;

    @Column(name = "data_abertura")
    private ZonedDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private ZonedDateTime dataFechamento;

    @Column(name = "quantidade_total")
    private Long quantidadeTotal;

    @Column(name = "valor_total")
    private Double valorTotal;

    @OneToMany(mappedBy = "custodia")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ordem> ordems = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusCustodiaEmpresa getStatus() {
        return status;
    }

    public Custodia status(StatusCustodiaEmpresa status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusCustodiaEmpresa status) {
        this.status = status;
    }

    public ZonedDateTime getDataAbertura() {
        return dataAbertura;
    }

    public Custodia dataAbertura(ZonedDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
        return this;
    }

    public void setDataAbertura(ZonedDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public ZonedDateTime getDataFechamento() {
        return dataFechamento;
    }

    public Custodia dataFechamento(ZonedDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
        return this;
    }

    public void setDataFechamento(ZonedDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Long getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public Custodia quantidadeTotal(Long quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
        return this;
    }

    public void setQuantidadeTotal(Long quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public Custodia valorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Set<Ordem> getOrdems() {
        return ordems;
    }

    public Custodia ordems(Set<Ordem> ordems) {
        this.ordems = ordems;
        return this;
    }

    public Custodia addOrdem(Ordem ordem) {
        this.ordems.add(ordem);
        ordem.setCustodia(this);
        return this;
    }

    public Custodia removeOrdem(Ordem ordem) {
        this.ordems.remove(ordem);
        ordem.setCustodia(null);
        return this;
    }

    public void setOrdems(Set<Ordem> ordems) {
        this.ordems = ordems;
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
        Custodia custodia = (Custodia) o;
        if (custodia.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), custodia.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Custodia{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", dataAbertura='" + getDataAbertura() + "'" +
            ", dataFechamento='" + getDataFechamento() + "'" +
            ", quantidadeTotal='" + getQuantidadeTotal() + "'" +
            ", valorTotal='" + getValorTotal() + "'" +
            "}";
    }
}
