package controlador;

import bd.LecturaBD;
import java.sql.SQLException;
import java.util.List;
import modelo.Reservacion;

/** CONTROLADOR: logica de negocio de las reservaciones. */
public class ReservacionController {
    private final LecturaBD lectura = new LecturaBD();

    public List<Reservacion> listar() throws SQLException {
        return lectura.listarReservaciones();
    }
}
