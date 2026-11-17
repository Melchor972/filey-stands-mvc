package controlador;

import bd.LecturaBD;
import java.sql.SQLException;
import java.util.List;
import modelo.Configuracion;
import modelo.Stand;

/** CONTROLADOR: logica de negocio de los stands (usa la capa de BD, no SQL directo). */
public class StandController {
    private final LecturaBD lectura = new LecturaBD();

    public List<Stand> listar() throws SQLException {
        return lectura.listarStands();
    }

    public Stand buscarPorId(String idStand) throws SQLException {
        return lectura.obtenerStandPorId(idStand);
    }

    /** Regla de negocio: precio = metros cuadrados x costo por metro cuadrado. */
    public double calcularPrecio(Stand stand, Configuracion config) {
        return stand.getMetrosCuadrados() * config.getCostoPorMetroCuadrado();
    }
}
