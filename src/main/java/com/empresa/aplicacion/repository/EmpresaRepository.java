package com.empresa.aplicacion.repository;

import com.empresa.aplicacion.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
