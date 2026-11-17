package modelo;

import java.time.LocalDate;

/** MODELO: parametros del sistema (costos y plazos). */
public class Configuracion {
    private double costoPorMetroCuadrado;
    private LocalDate fechaLimiteCosto;
    private double porcentajeMinimoOcupado;
    private LocalDate fechaProntoPago;
    private double descuentoProntoPago;

    public Configuracion(double costoPorMetroCuadrado, LocalDate fechaLimiteCosto,
                         double porcentajeMinimoOcupado, LocalDate fechaProntoPago,
                         double descuentoProntoPago) {
        this.costoPorMetroCuadrado = costoPorMetroCuadrado;
        this.fechaLimiteCosto = fechaLimiteCosto;
        this.porcentajeMinimoOcupado = porcentajeMinimoOcupado;
        this.fechaProntoPago = fechaProntoPago;
        this.descuentoProntoPago = descuentoProntoPago;
    }

    public double getCostoPorMetroCuadrado() { return costoPorMetroCuadrado; }
    public LocalDate getFechaLimiteCosto() { return fechaLimiteCosto; }
    public double getPorcentajeMinimoOcupado() { return porcentajeMinimoOcupado; }
    public LocalDate getFechaProntoPago() { return fechaProntoPago; }
    public double getDescuentoProntoPago() { return descuentoProntoPago; }
}
