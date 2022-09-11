package com.empresa.aplicacion.service;

import com.empresa.aplicacion.entity.Empresa;

public interface EmpresaService {

    public Iterable<Empresa> getAllEmpresa();

    public Empresa createEmpresa(Empresa empresa);

    public Empresa getEmpresaById(Long id) throws Exception;

    public Empresa updateEmpresa(Empresa empresa) throws Exception;

    public void deleteEmpresa(Long id) throws Exception;


}
