package controlador;

import bd.ReservacionDAO;
import bd.StandDAO;
import excepciones.ConflictoReservacionException;
import excepciones.FileyException;
import excepciones.RegistroDuplicadoException;
import excepciones.RegistroNoEncontradoException;
import excepciones.ValidacionException;
import java.sql.SQLException;
import java.util.List;
import modelo.Configuracion;
import modelo.EstadoStand;
import modelo.Stand;

/**
 * CONTROLADOR de stands: valida, aplica las reglas de negocio y lanza las
 * excepciones logicas. Delega el SQL en StandDAO.
 */
public class StandController {
    private final StandDAO standDAO = new StandDAO();
    private final ReservacionDAO reservacionDAO = new ReservacionDAO();

    // ---- READ ----
    public List<Stand> listar() throws SQLException {
        return standDAO.listarTodos();
    }

    /** Devuelve null si no existe (lo usan las notificaciones). */
    public Stand buscarPorId(String idStand) throws SQLException {
        return standDAO.obtenerPorId(idStand);
    }

    /** Regla de negocio: precio = metros cuadrados x costo por metro cuadrado. */
    public double calcularPrecio(Stand stand, Configuracion config) {
        return stand.getMetrosCuadrados() * config.getCostoPorMetroCuadrado();
    }

    // ---- CREATE ----
    public void crear(Stand stand) throws SQLException, FileyException {
        validar(stand);
        if (standDAO.obtenerPorId(stand.getIdStand()) != null) {
            throw new RegistroDuplicadoException("stand", stand.getIdStand());
        }
        standDAO.insertar(stand);
    }

    // ---- UPDATE ----
    public void actualizar(Stand stand) throws SQLException, FileyException {
        validar(stand);
        exigirExistente(stand.getIdStand());
        standDAO.actualizar(stand);
    }

    /** Cambio de estado (ej. BLOQUEADO -> DISPONIBLE cuando el admin libera el stand). */
    public void cambiarEstado(String idStand, EstadoStand nuevoEstado) throws SQLException, FileyException {
        Stand stand = exigirExistente(idStand);
        stand.setEstado(nuevoEstado);
        standDAO.actualizar(stand);
    }

    // ---- DELETE ----
    public void eliminar(String idStand) throws SQLException, FileyException {
        exigirExistente(idStand);
        if (reservacionDAO.contarPorStand(idStand) > 0) {
            throw new ConflictoReservacionException(
                    "No se puede borrar el stand " + idStand + ": tiene reservaciones registradas");
        }
        standDAO.eliminar(idStand);
    }

    // ---- reglas internas ----
    private Stand exigirExistente(String idStand) throws SQLException, RegistroNoEncontradoException {
        Stand stand = standDAO.obtenerPorId(idStand);
        if (stand == null) {
            throw new RegistroNoEncontradoException("stand", idStand);
        }
        return stand;
    }

    private void validar(Stand s) throws ValidacionException {
        if (s.getIdStand() == null || s.getIdStand().isBlank()) {
            throw new ValidacionException("El id del stand es obligatorio");
        }
        if (s.getDimensiones() == null || s.getDimensiones().isBlank()) {
            throw new ValidacionException("Las dimensiones del stand son obligatorias");
        }
        if (s.getMetrosCuadrados() <= 0) {
            throw new ValidacionException("Los metros cuadrados deben ser mayores a 0");
        }
        if (s.getPrecio() < 0) {
            throw new ValidacionException("El precio no puede ser negativo");
        }
        if (s.getEstado() == null) {
            throw new ValidacionException("El estado del stand es obligatorio");
        }
    }
}
