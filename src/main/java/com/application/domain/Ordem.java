package com.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.application.domain.enumeration.TipoOrdermEnum;

import com.application.domain.enumeration.StatusOrdemEnum;

/**
 * A Ordem.
 */
@Entity
@Table(name = "ordem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ordem")
public class Ordem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoOrdermEnum tipo;

    @Column(name = "valor_ordem")
    private Double valorOrdem;

    @Column(name = "quantidade")
    private Long quantidade;

    @Column(name = "data_ordem")
    private ZonedDateTime dataOrdem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusOrdemEnum status;

    @ManyToOne
    private Custodia custodia;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoOrdermEnum getTipo() {
        return tipo;
    }

    public Ordem tipo(TipoOrdermEnum tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoOrdermEnum tipo) {
        this.tipo = tipo;
    }

    public Double getValorOrdem() {
        return valorOrdem;
    }

    public Ordem valorOrdem(Double valorOrdem) {
        this.valorOrdem = valorOrdem;
        return this;
    }

    public void setValorOrdem(Double valorOrdem) {
        this.valorOrdem = valorOrdem;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public Ordem quantidade(Long quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public ZonedDateTime getDataOrdem() {
        return dataOrdem;
    }

    public Ordem dataOrdem(ZonedDateTime dataOrdem) {
        this.dataOrdem = dataOrdem;
        return this;
    }

    public void setDataOrdem(ZonedDateTime dataOrdem) {
        this.dataOrdem = dataOrdem;
    }

    public StatusOrdemEnum getStatus() {
        return status;
    }

    public Ordem status(StatusOrdemEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusOrdemEnum status) {
        this.status = status;
    }

    public Custodia getCustodia() {
        return custodia;
    }

    public Ordem custodia(Custodia custodia) {
        this.custodia = custodia;
        return this;
    }

    public void setCustodia(Custodia custodia) {
        this.custodia = custodia;
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
        Ordem ordem = (Ordem) o;
        if (ordem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ordem{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", valorOrdem='" + getValorOrdem() + "'" +
            ", quantidade='" + getQuantidade() + "'" +
            ", dataOrdem='" + getDataOrdem() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
