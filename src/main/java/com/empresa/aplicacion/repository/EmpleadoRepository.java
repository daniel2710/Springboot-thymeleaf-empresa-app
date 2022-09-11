package com.empresa.aplicacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.empresa.aplicacion.entity.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

	public Optional<Empleado> findByUsername(String username);

 }
