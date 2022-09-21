package com.empresa.aplicacion.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "employes")
public class Empleado implements Serializable{

    /**
     EMPLEADOS / EMPLOYES
     */
    private static final long serialVersionUID = 1671417246199538663L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="native")
    @GenericGenerator(name="native",strategy="native")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "employe_id")
    private Integer id;

    @Column
    @NotBlank
    private String firstName;
    @Column
    @NotBlank
    private String lastName;
    @Column
    @NotBlank
    private String email;
    @Column
    @NotBlank
    private String phone;
    @Column
    @NotBlank
    private String username;
    @Column
    @NotBlank
    private String password;

    @Transient
    private String confirmPassword;

    // Mapeo de relacion user/roles
    @Size(min=1)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employe_roles",
            joinColumns=@JoinColumn(name="employe_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles;

    public Empleado() {
        super();
    }

    public Empleado(Integer id) {
        super();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empleado user = (Empleado) o;
        return id.equals(user.id) && firstName.equals(user.firstName) && lastName.equals(user.lastName) && email.equals(user.email) && phone.equals(user.phone) && username.equals(user.username) && password.equals(user.password) && confirmPassword.equals(user.confirmPassword) && roles.equals(user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone, username, password, confirmPassword, roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", roles=" + roles +
                '}';
    }
}

