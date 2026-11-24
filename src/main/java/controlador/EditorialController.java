package controlador;

import bd.EditorialDAO;
import bd.ReservacionDAO;
import excepciones.ConflictoReservacionException;
import excepciones.FileyException;
import excepciones.RegistroDuplicadoException;
import excepciones.RegistroNoEncontradoException;
import excepciones.ValidacionException;
import java.sql.SQLException;
import java.util.List;
import modelo.Editorial;

/** CONTROLADOR de editoriales: valida, aplica reglas y lanza excepciones logicas. */
public class EditorialController {
    private final EditorialDAO editorialDAO = new EditorialDAO();
    private final ReservacionDAO reservacionDAO = new ReservacionDAO();

    // ---- READ ----
    public List<Editorial> listar() throws SQLException {
        return editorialDAO.listarTodos();
    }

    /** Devuelve null si no existe (lo usan las notificaciones). */
    public Editorial buscarPorId(String idEditorial) throws SQLException {
        return editorialDAO.obtenerPorId(idEditorial);
    }

    // ---- CREATE ----
    public void crear(Editorial editorial) throws SQLException, FileyException {
        validar(editorial);
        if (editorialDAO.obtenerPorId(editorial.getIdEditorial()) != null) {
            throw new RegistroDuplicadoException("editorial", editorial.getIdEditorial());
        }
        editorialDAO.insertar(editorial);
    }

    // ---- UPDATE ----
    public void actualizar(Editorial editorial) throws SQLException, FileyException {
        validar(editorial);
        exigirExistente(editorial.getIdEditorial());
        editorialDAO.actualizar(editorial);
    }

    // ---- DELETE ----
    public void eliminar(String idEditorial) throws SQLException, FileyException {
        exigirExistente(idEditorial);
        if (reservacionDAO.contarPorEditorial(idEditorial) > 0) {
            throw new ConflictoReservacionException(
                    "No se puede borrar la editorial " + idEditorial + ": tiene reservaciones registradas");
        }
        editorialDAO.eliminar(idEditorial);
    }

    // ---- reglas internas ----
    private void exigirExistente(String idEditorial) throws SQLException, RegistroNoEncontradoException {
        if (editorialDAO.obtenerPorId(idEditorial) == null) {
            throw new RegistroNoEncontradoException("editorial", idEditorial);
        }
    }

    private void validar(Editorial e) throws ValidacionException {
        if (e.getIdEditorial() == null || e.getIdEditorial().isBlank()) {
            throw new ValidacionException("El id de la editorial es obligatorio");
        }
        if (e.getNombre() == null || e.getNombre().isBlank()) {
            throw new ValidacionException("El nombre de la editorial es obligatorio");
        }
        if (e.getCorreoContacto() == null || !e.getCorreoContacto().contains("@")) {
            throw new ValidacionException("El correo de contacto no es valido");
        }
    }
}
