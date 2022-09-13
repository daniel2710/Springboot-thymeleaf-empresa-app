package com.empresa.aplicacion.service;

import com.empresa.aplicacion.Exception.UsernameOrIdNotFound;
import com.empresa.aplicacion.entity.Empleado;

import com.empresa.aplicacion.dto.ChangePasswordForm;

public interface EmpleadoService {

	public Iterable<Empleado> getAllEmpleados();

	public Empleado createEmpleado(Empleado empleado) throws Exception;

	public Empleado getEmpleadoById(Integer id) throws Exception;

	public Empleado updateEmpleado(Empleado empleado) throws Exception;

	public void deleteEmpleado(Integer id) throws UsernameOrIdNotFound;

	public Empleado changePassword(ChangePasswordForm form) throws Exception;
}
