package com.empresa.aplicacion.controller;

import java.util.stream.Collectors;
import javax.validation.Valid;

import com.empresa.aplicacion.entity.Empresa;
import com.empresa.aplicacion.entity.Empleado;
import com.empresa.aplicacion.repository.EmpresaRepository;
import com.empresa.aplicacion.repository.RoleRepository;
import com.empresa.aplicacion.Exception.UsernameOrIdNotFound;
import com.empresa.aplicacion.dto.ChangePasswordForm;
import com.empresa.aplicacion.service.EmpleadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class EmpleadoController {

	// Constante que se agregara como clase en tabla (thymeleaf)
	private final String TAB_FORM = "formTab";
	private final String TAB_LIST = "listTab";

	@Autowired
	EmpleadoService empleadoService;

	@Autowired
	EmpresaRepository empresaRepository;

	@Autowired
    RoleRepository roleRepository;

	// Atributos de modelo para la plantilla Empleado
	private void baseAttributerForEmpleadoForm(Model model, Empleado empleado, Empresa empresa, String activeTab) {
		model.addAttribute("empleadoForm", empleado);
		model.addAttribute("empleadoList", empleadoService.getAllEmpleados());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("empresaForm", empresa);
		model.addAttribute("empresas", empresaRepository.findAll());
		model.addAttribute(activeTab,"active");
	}

	// Retorna la plantilla de crear empleado, empleado-form (vista del formulario)
	@GetMapping("/empleadoForm")
	public String userForm(Model model) {
		baseAttributerForEmpleadoForm(model, new Empleado(), new Empresa(), TAB_LIST );
		return "empleado/empleado-view";
	}

	// Metodo para Crear un nuevo empleado
	@PostMapping("/empleadoForm")
	public String createEmpleado(@Valid @ModelAttribute("empleadoForm") Empleado empleado, Empresa empresa, BindingResult result, Model model) {

		if(result.hasErrors()) {
			baseAttributerForEmpleadoForm(model, empleado, empresa, TAB_FORM);
		}else {
			try {

				empleadoService.createEmpleado(empleado);
				baseAttributerForEmpleadoForm(model, new Empleado(), new Empresa(), TAB_LIST );

			}catch (Exception e) {

				model.addAttribute("formErrorMessage", e.getMessage());
				baseAttributerForEmpleadoForm(model, empleado,empresa, TAB_FORM );
			}
		}
		return "empleado/empleado-view";
	}


	// retorna la vista para editar un empleado, recibe un pathVariable (id)
	@GetMapping("/editEmpleado/{id}")
	public String getEditUserForm(Model model, @PathVariable(name = "id") Integer id, Empresa empresa)throws Exception{

		Empleado empleadoToEdit = empleadoService.getEmpleadoById(id);

		baseAttributerForEmpleadoForm(model, empleadoToEdit,empresa, TAB_FORM );

		model.addAttribute("editMode","true");
		model.addAttribute("passwordForm",new ChangePasswordForm(id));

		return "empleado/empleado-view";
	}

	// Metodo par editar un empleado
	@PostMapping("/editEmpleado")
	public String postEditUserForm(@Valid @ModelAttribute("empleadoForm") Empleado empleado, Empresa empresa, BindingResult result, Model model) {
		if(result.hasErrors()) {
			baseAttributerForEmpleadoForm(model, empleado,empresa, TAB_FORM );
			model.addAttribute("editMode","true");
			model.addAttribute("passwordForm",new ChangePasswordForm(empleado.getId()));
		}else {
			try {

				empleadoService.updateEmpleado(empleado);
				baseAttributerForEmpleadoForm(model, new Empleado(), new Empresa(), TAB_LIST );

			} catch (Exception e) {

				model.addAttribute("formErrorMessage" , e.getMessage());
				baseAttributerForEmpleadoForm(model, empleado,empresa, TAB_FORM );
				model.addAttribute("editMode","true");
				model.addAttribute("passwordForm",new ChangePasswordForm(empleado.getId()));

			}
		}
		return "empleado/empleado-view";
	}

	// Metodo para cambiar la contraseña
	@PostMapping("/editEmpleado/changePassword")
	public ResponseEntity postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		try {
			if( errors.hasErrors()) {
				String result = errors.getAllErrors()
						.stream().map(x -> x.getDefaultMessage())
						.collect(Collectors.joining(""));

				throw new Exception(result);
			}
			empleadoService.changePassword(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}

	// metodo para eliminar un empleado por id
	@GetMapping("/deleteEmpleado/{id}")
	public String deleteUser(Model model, @PathVariable(name="id") Integer id) {
		try {
			empleadoService.deleteEmpleado(id);
			baseAttributerForEmpleadoForm(model, new Empleado(), new Empresa(), TAB_LIST );
		}
		catch (UsernameOrIdNotFound uoin) {
			model.addAttribute("listErrorMessage", uoin.getMessage());
		}
		return "redirect:/empleadoForm";
	}

	@GetMapping("/empleadoForm/cancel")
	public String cancelEditUser(ModelMap model) {
		return "redirect:/empleadoForm";
	}



}


