package modelo;

import java.time.LocalDate;

/**
 * MODELO: una reservacion une una Editorial con un Stand.
 * Guarda los identificadores (llaves foraneas) y no los objetos completos.
 */
public class Reservacion {
    private int idReservacion;
    private String idStand;
    private String idEditorial;
    private LocalDate fechaReservacion;
    private LocalDate vigenciaReserva;   // puede ser null (stand ya ocupado)
    private double porcentajePagado;     // 0.0 a 1.0

    public Reservacion(int idReservacion, String idStand, String idEditorial,
                       LocalDate fechaReservacion, LocalDate vigenciaReserva,
                       double porcentajePagado) {
        this.idReservacion = idReservacion;
        this.idStand = idStand;
        this.idEditorial = idEditorial;
        this.fechaReservacion = fechaReservacion;
        this.vigenciaReserva = vigenciaReserva;
        this.porcentajePagado = porcentajePagado;
    }

    public int getIdReservacion() { return idReservacion; }
    public String getIdStand() { return idStand; }
    public String getIdEditorial() { return idEditorial; }
    public LocalDate getFechaReservacion() { return fechaReservacion; }
    public LocalDate getVigenciaReserva() { return vigenciaReserva; }
    public double getPorcentajePagado() { return porcentajePagado; }
}
