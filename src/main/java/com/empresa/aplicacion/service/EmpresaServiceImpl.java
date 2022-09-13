package com.empresa.aplicacion.service;

import com.empresa.aplicacion.Exception.UsernameOrIdNotFound;
import com.empresa.aplicacion.entity.Empresa;
import com.empresa.aplicacion.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class EmpresaServiceImpl implements EmpresaService{

    @Autowired
    EmpresaRepository empresaRepository;

    @Override
    public Iterable<Empresa> getAllEmpresa() {
        return empresaRepository.findAll();
    }

    @Override
    public Empresa createEmpresa(Empresa empresa){
        empresa = empresaRepository.save(empresa);
        return empresa;
    }


    @Override
    public Empresa getEmpresaById(Integer id) throws Exception {
        return empresaRepository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El id de la empresa no existe"));
    }

    protected void mapEmpresa(Empresa from,Empresa to) {
        to.setName(from.getName());
        to.setNit(from.getNit());
        to.setAddress(to.getAddress());
        to.setPhone(from.getPhone());
        to.setLogo(from.getLogo());
        to.setEmployes(from.getEmployes());
    }

    @Override
    public Empresa updateEmpresa(Empresa fromEmpresa) throws Exception {
        Empresa toEmpresa = getEmpresaById(fromEmpresa.getId());
        mapEmpresa(fromEmpresa, toEmpresa);
        return empresaRepository.save(toEmpresa);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteEmpresa(Integer id) throws Exception {
        Empresa empresa = getEmpresaById(id);
        empresaRepository.deleteById(id);
    }

}
