package controlador;

import bd.LecturaBD;
import java.sql.SQLException;
import java.util.List;
import modelo.Editorial;

/** CONTROLADOR: logica de negocio de las editoriales. */
public class EditorialController {
    private final LecturaBD lectura = new LecturaBD();

    public List<Editorial> listar() throws SQLException {
        return lectura.listarEditoriales();
    }

    public Editorial buscarPorId(String idEditorial) throws SQLException {
        return lectura.obtenerEditorialPorId(idEditorial);
    }
}
