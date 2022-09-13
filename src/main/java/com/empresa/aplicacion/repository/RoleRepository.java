package com.empresa.aplicacion.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.empresa.aplicacion.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

	public Role findByName(String Integer);
}
