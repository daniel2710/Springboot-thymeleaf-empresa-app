package com.empresa.aplicacion.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class MovimientoDinero extends Auditable implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator="native")
    @GenericGenerator(name="native",strategy="native")
    @Column(name = "transaction_id")
    private Integer id;

    @Column
    private String concept;

    @Column
    private float amount;

    @Column
    private Integer empresaid;

    public MovimientoDinero() {
    }

    public MovimientoDinero(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public Integer getEmpresaid() {
        return empresaid;
    }

    public void setEmpresaid(Integer empresaid) {
        this.empresaid = empresaid;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimientoDinero that = (MovimientoDinero) o;
        return Float.compare(that.amount, amount) == 0 && Objects.equals(id, that.id) && Objects.equals(concept, that.concept) && Objects.equals(empresaid, that.empresaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, concept, amount, empresaid);
    }
}
