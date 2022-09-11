package com.empresa.aplicacion.service;

import com.empresa.aplicacion.entity.MovimientoDinero;

public interface MovDineroService {

    public Iterable<MovimientoDinero> getAllMovDinero();

    public MovimientoDinero createMovDinero(MovimientoDinero movimientoDinero);

    public MovimientoDinero getMovDineroById(Long id) throws Exception;

    public MovimientoDinero updateMovDinero(MovimientoDinero movimientoDinero) throws Exception;

    public void deleteMovDinero(Long id) throws Exception;
}
