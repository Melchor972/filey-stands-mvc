package controlador;

import bd.EditorialDAO;
import bd.ReservacionDAO;
import bd.StandDAO;
import excepciones.ConflictoReservacionException;
import excepciones.FileyException;
import excepciones.RegistroDuplicadoException;
import excepciones.RegistroNoEncontradoException;
import excepciones.ValidacionException;
import java.sql.SQLException;
import java.util.List;
import modelo.EstadoStand;
import modelo.Reservacion;
import modelo.Stand;

/**
 * CONTROLADOR de reservaciones. Es el que mas reglas tiene porque coordina
 * tres tablas: reservacion, stand y editorial.
 */
public class ReservacionController {
    private final ReservacionDAO reservacionDAO = new ReservacionDAO();
    private final StandDAO standDAO = new StandDAO();
    private final EditorialDAO editorialDAO = new EditorialDAO();

    // ---- READ ----
    public List<Reservacion> listar() throws SQLException {
        return reservacionDAO.listarTodos();
    }

    // ---- CREATE ----
    /** Solo se puede reservar un stand DISPONIBLE; al reservar, el stand pasa a RESERVADO. */
    public void crear(Reservacion r) throws SQLException, FileyException {
        validar(r);
        if (reservacionDAO.obtenerPorId(r.getIdReservacion()) != null) {
            throw new RegistroDuplicadoException("reservacion", r.getIdReservacion());
        }
        Stand stand = standDAO.obtenerPorId(r.getIdStand());
        if (stand == null) {
            throw new RegistroNoEncontradoException("stand", r.getIdStand());
        }
        if (editorialDAO.obtenerPorId(r.getIdEditorial()) == null) {
            throw new RegistroNoEncontradoException("editorial", r.getIdEditorial());
        }
        if (stand.getEstado() != EstadoStand.DISPONIBLE) {
            throw new ConflictoReservacionException("El stand " + stand.getIdStand()
                    + " no esta disponible (estado actual: " + stand.getEstado() + ")");
        }
        reservacionDAO.insertar(r);
        stand.setEstado(EstadoStand.RESERVADO);
        standDAO.actualizar(stand);
    }

    // ---- UPDATE (vigencia y porcentaje pagado) ----
    public void actualizar(Reservacion r) throws SQLException, FileyException {
        validar(r);
        if (reservacionDAO.obtenerPorId(r.getIdReservacion()) == null) {
            throw new RegistroNoEncontradoException("reservacion", r.getIdReservacion());
        }
        reservacionDAO.actualizar(r);
    }

    // ---- DELETE ----
    /** Al borrar la reservacion, el stand se libera (vuelve a DISPONIBLE). */
    public void eliminar(int idReservacion) throws SQLException, FileyException {
        Reservacion r = reservacionDAO.obtenerPorId(idReservacion);
        if (r == null) {
            throw new RegistroNoEncontradoException("reservacion", idReservacion);
        }
        reservacionDAO.eliminar(idReservacion);
        Stand stand = standDAO.obtenerPorId(r.getIdStand());
        if (stand != null && stand.getEstado() != EstadoStand.BLOQUEADO) {
            stand.setEstado(EstadoStand.DISPONIBLE);
            standDAO.actualizar(stand);
        }
    }

    // ---- reglas internas ----
    private void validar(Reservacion r) throws ValidacionException {
        if (r.getIdReservacion() <= 0) {
            throw new ValidacionException("El id de la reservacion debe ser mayor a 0");
        }
        if (r.getIdStand() == null || r.getIdStand().isBlank()
                || r.getIdEditorial() == null || r.getIdEditorial().isBlank()) {
            throw new ValidacionException("La reservacion necesita un stand y una editorial");
        }
        if (r.getFechaReservacion() == null) {
            throw new ValidacionException("La fecha de reservacion es obligatoria");
        }
        if (r.getVigenciaReserva() != null && r.getVigenciaReserva().isBefore(r.getFechaReservacion())) {
            throw new ValidacionException("La vigencia no puede ser anterior a la fecha de reservacion");
        }
        if (r.getPorcentajePagado() < 0 || r.getPorcentajePagado() > 1) {
            throw new ValidacionException("El porcentaje pagado debe estar entre 0 y 1");
        }
    }
}
