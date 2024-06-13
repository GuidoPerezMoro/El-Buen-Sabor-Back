package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.dto.Estadisticas.CostoGanancia;
import com.entidades.buenSabor.domain.entities.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido,Long>{

    // mysql -> date(p.fecha_pedido)
    // H2 ->  PARSEDATETIME(p.fecha_pedido, 'yyyy-MM-dd') || FORMATDATETIME(p.fecha_pedido, 'yyyy-MM-dd')

    @Query(value = "SELECT FORMATDATETIME(p.fecha_pedido, 'yyyy-MM-dd') AS fecha, SUM(p.total) AS ingresos " +
            "FROM pedido p " +
            "WHERE p.fecha_pedido BETWEEN :initialDate AND :endDate " +
            "GROUP BY FORMATDATETIME(p.fecha_pedido, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> ingresosDiarios(Date initialDate, Date endDate);

    // mysql -> date(p.fecha_pedido)
    // H2 ->  PARSEDATETIME(p.fecha_pedido, 'yyyy-MM-dd') || FORMATDATETIME(p.fecha_pedido, 'yyyy-MM-dd')

    @Query(value = "SELECT FORMATDATETIME(p.fecha_pedido, 'yyyy-MM') AS mes, SUM(p.total) AS ingresos " +
            "FROM Pedido p " +
            "WHERE p.fecha_pedido BETWEEN :startDate AND :endDate " +
            "GROUP BY FORMATDATETIME(p.fecha_pedido, 'yyyy-MM')", nativeQuery = true)
    List<Object[]> ingresosMensuales(Date startDate, Date endDate);

    @Query(value = "SELECT " +
            "SUM(dp.cantidad * ai.precioCompra) AS costos, " +
            "SUM(p.total) AS ganancias, " +
            "(SUM(p.total) - SUM(dp.cantidad * ai.precioCompra)) AS resultado " +
            "FROM Pedido p " +
            "JOIN p.detallePedidos dp " +
            "JOIN dp.articulo a " +
            "LEFT JOIN ArticuloInsumo ai ON a.id = ai.id " +
            "WHERE p.fechaPedido BETWEEN :initialDate AND :endDate")
    CostoGanancia findCostosGananciasByFecha(LocalDate initialDate, LocalDate endDate);
}
