package com.empresa.aplicacion.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.empresa.aplicacion.Exception.CustomeFieldValidationException;
import com.empresa.aplicacion.entity.Empresa;
import com.empresa.aplicacion.entity.MovimientoDinero;
import com.empresa.aplicacion.entity.Role;
import com.empresa.aplicacion.entity.Empleado;
import com.empresa.aplicacion.repository.EmpresaRepository;
import com.empresa.aplicacion.repository.RoleRepository;
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

import com.empresa.aplicacion.Exception.UsernameOrIdNotFound;
import com.empresa.aplicacion.dto.ChangePasswordForm;
import com.empresa.aplicacion.service.EmpleadoService;

@Controller
public class EmpleadoController {

	private final String TAB_FORM = "formTab";
	private final String TAB_LIST = "listTab";

	@Autowired
	EmpleadoService empleadoService;

	@Autowired
	EmpresaRepository empresaRepository;

	@Autowired
    RoleRepository roleRepository;

	@GetMapping(value = {"/", "/index"})
	public String index(){
		return "index";
	}

	@GetMapping("/login")
	public String login(){
		return "login";
	}

	@GetMapping("/home")
	public String home(Model model){
		return "home/home";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		Role userRole = roleRepository.findByName("OPERATIVO");
		List<Role> roles = Arrays.asList(userRole);

		model.addAttribute("signup",true);
		model.addAttribute("userForm", new Empleado());
		model.addAttribute("roles",roles);
		return "empleado/empleado-signup";
	}

	@PostMapping("/signup")
	public String signupAction(@Valid @ModelAttribute("empleadoForm") Empleado empleado, BindingResult result, ModelMap model) {
		Role userRole = roleRepository.findByName("OPERATIVO");
		List<Role> roles = Arrays.asList(userRole);
		model.addAttribute("empleadoForm", empleado);
		model.addAttribute("roles",roles);
		model.addAttribute("signup",true);

		if(result.hasErrors()) {
			return "empleado/empleado-signup";
		}else {
			try {
				empleadoService.createEmpleado(empleado);
			} catch (CustomeFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
			}catch (Exception e) {
				model.addAttribute("formErrorMessage",e.getMessage());
			}
		}
		return index();
	}

	private void baseAttributerForEmpleadoForm(Model model, Empleado empleado, Empresa empresa, String activeTab) {
		model.addAttribute("empleadoForm", empleado);
		model.addAttribute("empleadoList", empleadoService.getAllEmpleados());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("empresaForm", empresa);
		model.addAttribute("empresas", empresaRepository.findAll());
		model.addAttribute(activeTab,"active");
	}

	@GetMapping("/empleadoForm")
	public String userForm(Model model) {
		baseAttributerForEmpleadoForm(model, new Empleado(), new Empresa(), TAB_LIST );
		return "empleado/empleado-view";
	}

	@PostMapping("/empleadoForm")
	public String createEmpleado(@Valid @ModelAttribute("empleadoForm") Empleado empleado, Empresa empresa, BindingResult result, Model model) {
		if(result.hasErrors()) {
			baseAttributerForEmpleadoForm(model, empleado, empresa, TAB_FORM);
		}else {
			try {
				empleadoService.createEmpleado(empleado);
				baseAttributerForEmpleadoForm(model, new Empleado(), new Empresa(), TAB_LIST );

			} catch (CustomeFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				baseAttributerForEmpleadoForm(model, empleado, empresa, TAB_FORM );
			}catch (Exception e) {
				model.addAttribute("formErrorMessage",e.getMessage());
				baseAttributerForEmpleadoForm(model, empleado,empresa, TAB_FORM );
			}
		}
		return "empleado/empleado-view";
	}

	@GetMapping("/editEmpleado/{id}")
	public String getEditUserForm(Model model, @PathVariable(name ="id")Long id, Empresa empresa)throws Exception{
		Empleado empleadoToEdit = empleadoService.getEmpleadoById(id);

		baseAttributerForEmpleadoForm(model, empleadoToEdit,empresa, TAB_FORM );
		model.addAttribute("editMode","true");
		model.addAttribute("passwordForm",new ChangePasswordForm(id));

		return "empleado/empleado-view";
	}

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
				model.addAttribute("formErrorMessage",e.getMessage());

				baseAttributerForEmpleadoForm(model, empleado,empresa, TAB_FORM );
				model.addAttribute("editMode","true");
				model.addAttribute("passwordForm",new ChangePasswordForm(empleado.getId()));
			}
		}
		return "empleado/empleado-view";

	}

	@GetMapping("/empleadoForm/cancel")
	public String cancelEditUser(ModelMap model) {
		return "redirect:/empleadoForm";
	}

	@GetMapping("/deleteEmpleado/{id}")
	public String deleteUser(Model model, @PathVariable(name="id")Long id) {
		try {
			empleadoService.deleteEmpleado(id);
		}
		catch (UsernameOrIdNotFound uoin) {
			model.addAttribute("listErrorMessage",uoin.getMessage());
		}
		return userForm(model);
	}

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

}


