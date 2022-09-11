package com.empresa.aplicacion.service;

import java.util.Optional;

import com.empresa.aplicacion.Exception.CustomeFieldValidationException;
import com.empresa.aplicacion.entity.Empleado;
import com.empresa.aplicacion.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.empresa.aplicacion.Exception.UsernameOrIdNotFound;
import com.empresa.aplicacion.dto.ChangePasswordForm;


@Service
public class EmpleadoServiceImpl implements EmpleadoService {

	@Autowired
	EmpleadoRepository empleadoRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Iterable<Empleado> getAllEmpleados() {
		return empleadoRepository.findAll();
	}

	private boolean checkUsernameAvailable(Empleado empleado) throws Exception {
		Optional<Empleado> userFound = empleadoRepository.findByUsername(empleado.getUsername());
		if (userFound.isPresent()) {
			throw new CustomeFieldValidationException("Username no disponible","username");
		}
		return true;
	}

	private boolean checkPasswordValid(Empleado empleado) throws Exception {
		if (empleado.getConfirmPassword() == null || empleado.getConfirmPassword().isEmpty()) {
			throw new CustomeFieldValidationException("Confirm Password es obligatorio","confirmPassword");
		}

		if ( !empleado.getPassword().equals(empleado.getConfirmPassword())) {
			throw new CustomeFieldValidationException("No coindicen las Password","password");
		}
		return true;
	}


	@Override
	public Empleado createEmpleado(Empleado empleado) throws Exception {
		if (checkUsernameAvailable(empleado) && checkPasswordValid(empleado)) {
			String encodedPassword = bCryptPasswordEncoder.encode(empleado.getPassword());
			empleado.setPassword(encodedPassword);
			empleado = empleadoRepository.save(empleado);
		}
		return empleado;
	}

	@Override
	public Empleado getEmpleadoById(Long id) throws UsernameOrIdNotFound {
		return empleadoRepository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El Id del usuario no existe."));
	}

	@Override
	public Empleado updateEmpleado(Empleado fromEmpleado) throws Exception {
		Empleado toEmpleado = getEmpleadoById(fromEmpleado.getId());
		mapEmpleado(fromEmpleado, toEmpleado);
		return empleadoRepository.save(toEmpleado);
	}

	protected void mapEmpleado(Empleado from, Empleado to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setPhone(from.getPhone());
		to.setRoles(from.getRoles());
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteEmpleado(Long id) throws UsernameOrIdNotFound {
		Empleado empleado = getEmpleadoById(id);
		empleadoRepository.delete(empleado);
	}

	@Override
	public Empleado changePassword(ChangePasswordForm form) throws Exception {
		Empleado empleado = getEmpleadoById(form.getId());

		if ( !isLoggedUserADMIN() && !empleado.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception ("Current Password invalido.");
		}

		if( empleado.getPassword().equals(form.getNewPassword())) {
			throw new Exception ("Nuevo debe ser diferente al password actual.");
		}

		if( !form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception ("Nuevo Password y Confirm Password no coinciden.");
		}

		String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
		empleado.setPassword(encodePassword);
		return empleadoRepository.save(empleado);
	}

	private boolean isLoggedUserADMIN() {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserDetails loggedUser = null;
		Object roles = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;

			roles = loggedUser.getAuthorities().stream()
					.filter(x -> "ROLE_ADMIN".equals(x.getAuthority())).findFirst()
					.orElse(null);
		}
		return roles != null ? true : false;
	}

	private Empleado getLoggedUser() throws Exception {
		//Obtener el usuario/empleado logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserDetails loggedUser = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}

		Empleado myEmpleado = empleadoRepository
				.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception("Error obteniendo el usuario logeado desde la sesion."));

		return myEmpleado;
	}
}
