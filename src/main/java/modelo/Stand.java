package modelo;

/**
 * MODELO: datos puros de un stand. No contiene logica de negocio
 * (el calculo del precio vive en StandController).
 */
public class Stand {
    private String idStand;
    private String dimensiones;
    private double metrosCuadrados;
    private double precio;
    private EstadoStand estado;

    public Stand(String idStand, String dimensiones, double metrosCuadrados,
                 double precio, EstadoStand estado) {
        this.idStand = idStand;
        this.dimensiones = dimensiones;
        this.metrosCuadrados = metrosCuadrados;
        this.precio = precio;
        this.estado = estado;
    }

    public String getIdStand() { return idStand; }
    public String getDimensiones() { return dimensiones; }
    public double getMetrosCuadrados() { return metrosCuadrados; }
    public double getPrecio() { return precio; }
    public EstadoStand getEstado() { return estado; }

    public void setPrecio(double precio) { this.precio = precio; }
    public void setEstado(EstadoStand estado) { this.estado = estado; }
}
