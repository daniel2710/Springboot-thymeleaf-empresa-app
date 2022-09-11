package com.empresa.aplicacion.repository;

import com.empresa.aplicacion.entity.MovimientoDinero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovDineroRepository extends JpaRepository<MovimientoDinero, Long> {


    // TRANSACCIONES DE UNA EMPRESA POR SU ID
    @Query(value = "SELECT * FROM spring_aplicacion.transactions where enterprise_id = :idempresa", nativeQuery = true)
    List<MovimientoDinero> transactionByIdEmpresa(@Param("idempresa") Long idempresa);

    // INGRESOS
    @Query(value = "SELECT * FROM spring_aplicacion.transactions where concept = 'ingreso' and enterprise_id = :idempresa", nativeQuery = true)
    List<MovimientoDinero> ingresos(@Param("idempresa") Long idempresa);


    // EGRESOS
    @Query(value = "SELECT * FROM spring_aplicacion.transactions where concept = 'egreso' and enterprise_id = :idempresa", nativeQuery = true)
    List<MovimientoDinero> egresos(@Param("idempresa") Long idempresa);
}
