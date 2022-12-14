package com.empresa.aplicacion.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 Empresa
 */

@Entity
@Table(name = "enterprises")
public class Empresa extends Auditable implements Serializable {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="native")
    @GenericGenerator(name="native",strategy="native")
    @Column(name = "enterprise_id")
    private Integer id;

    @Column(unique=true)
    private String name;

    @Column(unique=true)
    private String nit;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String logo;


    // Mapeo de relacion empleados/empresas
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "employe_enterprise",
            joinColumns = {@JoinColumn(name = "enterprise_id")},
            inverseJoinColumns = {@JoinColumn(name = "employe_id")})
    public Set<Empleado> employes;

    public Empresa() {
    }

    public Empresa(Integer id) {
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Empleado> getEmployes() {
        return employes;
    }

    public void setEmployes(Set<Empleado> employes) {
        this.employes = employes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(id, empresa.id) && Objects.equals(name, empresa.name) && Objects.equals(nit, empresa.nit) && Objects.equals(phone, empresa.phone) && Objects.equals(address, empresa.address) && Objects.equals(logo, empresa.logo) && Objects.equals(employes, empresa.employes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nit, phone, address, logo, employes);
    }
}
