package com.empresa.aplicacion.controller;

import com.empresa.aplicacion.Exception.UsernameOrIdNotFound;
import com.empresa.aplicacion.entity.Empresa;
import com.empresa.aplicacion.entity.MovimientoDinero;
import com.empresa.aplicacion.repository.MovDineroRepository;
import com.empresa.aplicacion.service.EmpleadoService;
import com.empresa.aplicacion.service.EmpresaService;
import com.empresa.aplicacion.service.MovDineroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class EmpresaController {

    @Autowired
    EmpresaService empresaService;

    @Autowired
    EmpleadoService empleadoService;

    @Autowired
    MovDineroService movDineroService;

    @Autowired
    MovDineroRepository movDineroRepository;

    private void baseAttributerForEmpresaList(Model model, Empresa empresa, MovimientoDinero movimientoDinero) {
        model.addAttribute("empresaList", empresaService.getAllEmpresa());
        model.addAttribute("empleadoList", empleadoService.getAllEmpleados());
        model.addAttribute("movDineroList", movDineroService.getAllMovDinero());
        model.addAttribute("empresaForm", empresa);
        model.addAttribute("movDineroForm", movimientoDinero);
    }

    // Listado de empresas
    @GetMapping("/empresas")
    public String empresaList(Model model) {
        baseAttributerForEmpresaList(model, new Empresa(), new MovimientoDinero());
        return "/empresa/lista";
    }

    // Crear Empresa - vista(get)
    @GetMapping("/empresa/create")
    public String empresaCreate(Model model) {
        baseAttributerForEmpresaList(model, new Empresa(), new MovimientoDinero());
        return "/empresa/create";
    }

    // Crear Empresa - post
    @PostMapping("/empresa/create")
    public String createEmpresa(@ModelAttribute("empresaForm") Empresa empresa, MovimientoDinero movimientoDinero, @RequestParam("file") MultipartFile logo, BindingResult result, Model model) {

        if (result.hasErrors()) {
            baseAttributerForEmpresaList(model, empresa, movimientoDinero);
        } else {
            try {
                if (!logo.isEmpty()) {
                    //Path directorioLogos = Paths.get("src//main//resources//static/logosEmpresas");
                    String rutaAbsoluta = "C://Empresas//recursos";

                    try {
                        byte[] bytesLogos = logo.getBytes();
                        Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + logo.getOriginalFilename());
                        Files.write(rutaCompleta, bytesLogos);

                        empresa.setLogo(logo.getOriginalFilename());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                empresaService.createEmpresa(empresa);
                baseAttributerForEmpresaList(model, new Empresa(), new MovimientoDinero());

            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                baseAttributerForEmpresaList(model, empresa, movimientoDinero);
            }
        }

        return "/empresa/create";
    }

    // Editar empresa por id - vista(get)
    @GetMapping("/editEmpresa/{id}")
    public String getEditEmpresaForm(Model model, MovimientoDinero movimientoDinero, @PathVariable(name = "id") Long id) throws Exception {
        Empresa empresaToEdit = empresaService.getEmpresaById(id);
        baseAttributerForEmpresaList(model, empresaToEdit, movimientoDinero);

        return "/empresa/editar";
    }

    // Editar empresa por id - post
    @PostMapping("/editEmpresa")
    public String postEditEmpresaForm(@Valid @ModelAttribute("empresaForm") Empresa empresa, @RequestParam("file") MultipartFile logo, BindingResult result, Model model, MovimientoDinero movimientoDinero) {
        if (result.hasErrors()) {
            baseAttributerForEmpresaList(model, empresa, movimientoDinero);
        } else {
            try {
                if (!logo.isEmpty()) {
                    //Path directorioLogos = Paths.get("src//main//resources//static/logosEmpresas");
                    String rutaAbsoluta = "C://Empresas//recursos";

                    try {
                        byte[] bytesLogos = logo.getBytes();
                        Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + logo.getOriginalFilename());
                        Files.write(rutaCompleta, bytesLogos);
                        empresa.setLogo(logo.getOriginalFilename());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                empresaService.updateEmpresa(empresa);
                baseAttributerForEmpresaList(model, new Empresa(), new MovimientoDinero());
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                baseAttributerForEmpresaList(model, empresa, movimientoDinero);
            }
        }
        return "/empresa/lista";

    }

    // Detalles de la empresa por id (empleados, ingresos y egresos)
    @GetMapping("/empresa/empleados/{idempresa}")
    public String getDetallesEmpresa(Model model, @PathVariable(name = "idempresa") Long idempresa) throws Exception {
        Empresa empresaById = empresaService.getEmpresaById(idempresa);
        model.addAttribute("empresaUsers", empresaById);
        return "/empresa/empleados";
    }


    @GetMapping("/deleteEmpresa/{id}")
    public String deleteUser(Model model, @PathVariable(name="id")Long id) throws Exception {
        try {
            empresaService.deleteEmpresa(id);
        }
        catch (UsernameOrIdNotFound uoin) {
            model.addAttribute("listErrorMessage",uoin.getMessage());
        }
        return "empresa/lista";
    }



    // Movimiento Dinero (Ingresos y Egresos)

    // Ingresos y Egresos - vista
    @GetMapping("/empresa/ingresos/{idempresa}")
    public String ingresos(Model model, @PathVariable(name = "idempresa") Long idempresa, Empresa empresa, MovimientoDinero movimientoDinero) throws Exception {
        baseAttributerForEmpresaList(model, empresa, movimientoDinero);
        Empresa empresaById = empresaService.getEmpresaById(idempresa);
        model.addAttribute("empresaTransactions", empresaById);
        List<MovimientoDinero> empresaMovimientos = movDineroRepository.transactionByIdEmpresa(idempresa);
        model.addAttribute("empresaMovimientos", empresaMovimientos);

        // PARA OBETENER LOS INGRESOS
        Double totalIngresos = 0.00;
        for (MovimientoDinero data : movDineroRepository.ingresos(idempresa)) {
            totalIngresos += data.getAmount();
        }
        model.addAttribute("totalIngresos", totalIngresos);

        // PARA OBETENER LOS EGRESOS
        Double totalEgresos = 0.00;
        for (MovimientoDinero data : movDineroRepository.egresos(idempresa)) {
            totalEgresos += data.getAmount();
        }
        model.addAttribute("totalEgresos", totalEgresos);

        // PARA OBTENER EL TOTAL(BALACE)
        Double balance = totalIngresos-totalEgresos;
        model.addAttribute("balance", balance);


        return "empresa/movimientos-lista";
    }

    // Nuevo Movimiento vista
    @GetMapping("/empresa/ingresos/nuevo/{idempresa}")
    public String nuevoMovimiento(Model model, @PathVariable(name = "idempresa") Long idempresa) throws Exception {
        Empresa empresaById = empresaService.getEmpresaById(idempresa);
        model.addAttribute("empresaId", empresaById);
        baseAttributerForEmpresaList(model, new Empresa(), new MovimientoDinero());
        return "/empresa/movimientos-nuevo";
    }

    // Nuevo Movimiento Form
    @PostMapping("/empresa/ingresos/nuevo")
    public String nuevoMovimientoForm(@Valid @ModelAttribute("movDineroForm") MovimientoDinero movimientoDinero, Empresa empresa, Model model, BindingResult result) {

        if (result.hasErrors()) {
            baseAttributerForEmpresaList(model, empresa, movimientoDinero);
        } else {
            try {
                movDineroService.createMovDinero(movimientoDinero);
                baseAttributerForEmpresaList(model, new Empresa(), new MovimientoDinero());

            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                baseAttributerForEmpresaList(model, empresa, movimientoDinero);
            }
        }
        return "/empresa/lista";
    }

    // Editar empresa por id - vista(get)
    @GetMapping("/editMovDinero/{idMovDinero}")
    public String getEditMovDinero(Model model, Empresa empresa, @PathVariable(name = "idMovDinero") Long idMovDinero) throws Exception {
        MovimientoDinero editMovDinero = movDineroService.getMovDineroById(idMovDinero);
        baseAttributerForEmpresaList(model , empresa, editMovDinero);
        return "/empresa/movimientos-editar";
    }

    // Editar empresa por id - post
    @PostMapping("/editMovDinero")
    public String editMovDinero(@Valid @ModelAttribute("movDineroForm")  MovimientoDinero movimientoDinero, BindingResult result, Model model, Empresa empresa) {
        if (result.hasErrors()) {
            baseAttributerForEmpresaList(model, empresa, movimientoDinero);
        } else {
            try {
                movDineroService.updateMovDinero(movimientoDinero);
                baseAttributerForEmpresaList(model, new Empresa(), new MovimientoDinero());
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                baseAttributerForEmpresaList(model, empresa, movimientoDinero);
            }
        }
        return "/empresa/lista";

    }

}