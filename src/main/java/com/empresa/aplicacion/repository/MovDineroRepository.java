package com.empresa.aplicacion.repository;

import com.empresa.aplicacion.entity.MovimientoDinero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovDineroRepository extends JpaRepository<MovimientoDinero, Integer> {


    // TRANSACCIONES DE UNA EMPRESA POR SU ID
    @Query(value = "SELECT * FROM transactions where empresaid = :idempresa", nativeQuery = true)
    List<MovimientoDinero> transactionByIdEmpresa(@Param("idempresa") Integer idempresa);

    // INGRESOS
    @Query(value = "SELECT * FROM transactions where concept = 'ingreso' and empresaid = :idempresa", nativeQuery = true)
    List<MovimientoDinero> ingresos(@Param("idempresa") Integer idempresa);

    // EGRESOS
    @Query(value = "SELECT * FROM transactions where concept = 'egreso' and empresaid = :idempresa", nativeQuery = true)
    List<MovimientoDinero> egresos(@Param("idempresa") Integer idempresa);
}
