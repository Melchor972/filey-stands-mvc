package controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Configuracion;
import modelo.Editorial;
import modelo.EstadoStand;
import modelo.Reservacion;
import modelo.Stand;

/**
 * CONTROLADOR: logica de las notificaciones (viene del PD2).
 * Ahora lee los datos desde la BD a traves de los otros controladores y
 * DEVUELVE los avisos como texto; la Vista es quien los imprime.
 */
public class NotificacionController {
    private static final int DIAS_AVISO_RESERVA = 2;
    private static final int DIAS_AVISO_COSTO = 3;

    private final StandController standController;
    private final EditorialController editorialController;
    private final ReservacionController reservacionController;

    public NotificacionController(StandController standController,
                                  EditorialController editorialController,
                                  ReservacionController reservacionController) {
        this.standController = standController;
        this.editorialController = editorialController;
        this.reservacionController = reservacionController;
    }

    /** 1. Reservas a punto de vencer (o ya vencidas) de stands en estado RESERVADO. */
    public List<String> verificarVencimientoReservas() throws SQLException {
        LocalDate hoy = LocalDate.now();
        List<String> avisos = new ArrayList<>();
        for (Reservacion r : reservacionController.listar()) {
            Stand stand = standController.buscarPorId(r.getIdStand());
            LocalDate vigencia = r.getVigenciaReserva();
            if (stand == null || stand.getEstado() != EstadoStand.RESERVADO || vigencia == null) {
                continue;
            }
            if (vigencia.minusDays(DIAS_AVISO_RESERVA).isBefore(hoy)) {
                Editorial ed = editorialController.buscarPorId(r.getIdEditorial());
                String estado = vigencia.isBefore(hoy) ? "YA VENCIO el " : "esta a punto de vencer el ";
                avisos.add("[ALERTA] La editorial " + ed.getNombre()
                        + " (Correo: " + ed.getCorreoContacto() + ") tiene su reserva del stand "
                        + stand.getIdStand() + " que " + estado + vigencia);
            }
        }
        return avisos;
    }

    /** 2. Pronto pago: avisa a las editoriales con reserva pendiente mientras aun aplique el descuento. */
    public List<String> verificarProntoPago(Configuracion config) throws SQLException {
        LocalDate hoy = LocalDate.now();
        List<String> avisos = new ArrayList<>();
        if (hoy.isAfter(config.getFechaProntoPago())) {
            avisos.add("[AVISO] El periodo de pronto pago ha finalizado.");
            return avisos;
        }
        for (Reservacion r : reservacionController.listar()) {
            Stand stand = standController.buscarPorId(r.getIdStand());
            if (stand != null && stand.getEstado() == EstadoStand.RESERVADO) {
                Editorial ed = editorialController.buscarPorId(r.getIdEditorial());
                avisos.add("[AVISO] Editorial " + ed.getNombre()
                        + ": aun aplica descuento del " + (int) (config.getDescuentoProntoPago() * 100)
                        + "% por pronto pago antes del " + config.getFechaProntoPago());
            }
        }
        return avisos;
    }

    /** 3. Fecha limite para cubrir el costo total del stand (editoriales con reserva pendiente). */
    public List<String> verificarFechaLimiteCosto(Configuracion config) throws SQLException {
        LocalDate hoy = LocalDate.now();
        List<String> avisos = new ArrayList<>();
        if (!config.getFechaLimiteCosto().minusDays(DIAS_AVISO_COSTO).isBefore(hoy)) {
            return avisos;
        }
        for (Reservacion r : reservacionController.listar()) {
            Stand stand = standController.buscarPorId(r.getIdStand());
            if (stand != null && stand.getEstado() == EstadoStand.RESERVADO) {
                Editorial ed = editorialController.buscarPorId(r.getIdEditorial());
                avisos.add("[URGENTE] Editorial " + ed.getNombre()
                        + ", la fecha limite para cubrir el costo total del stand "
                        + stand.getIdStand() + " es el: " + config.getFechaLimiteCosto());
            }
        }
        return avisos;
    }
}
