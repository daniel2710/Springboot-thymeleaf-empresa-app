package com.empresa.aplicacion.service;

import com.empresa.aplicacion.Exception.UsernameOrIdNotFound;
import com.empresa.aplicacion.entity.Empresa;
import com.empresa.aplicacion.entity.MovimientoDinero;
import com.empresa.aplicacion.repository.MovDineroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class MovDineroServiceImpl implements MovDineroService{
    @Autowired
    MovDineroRepository movDineroRepository;

    @Override
    public Iterable<MovimientoDinero> getAllMovDinero() {
        return movDineroRepository.findAll();
    }

    @Override
    public MovimientoDinero createMovDinero(MovimientoDinero movimientoDinero){
        movimientoDinero = movDineroRepository.save(movimientoDinero);
        return movimientoDinero;
    }

    @Override
    public MovimientoDinero getMovDineroById(Long id) throws Exception {
        return movDineroRepository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El id del movimiento no existe"));
    }

    protected void mapMovDinero(MovimientoDinero from, MovimientoDinero to) {
        to.setConcept(from.getConcept());
        to.setAmount(from.getAmount());
    }

    @Override
    public MovimientoDinero updateMovDinero(MovimientoDinero fromMovDinero) throws Exception {
        MovimientoDinero toMovDinero = getMovDineroById(fromMovDinero.getId());
        mapMovDinero(fromMovDinero, toMovDinero);
        return movDineroRepository.save(toMovDinero);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteMovDinero(Long idMovDinero) throws Exception{
        movDineroRepository.deleteById(idMovDinero);
    }

}
