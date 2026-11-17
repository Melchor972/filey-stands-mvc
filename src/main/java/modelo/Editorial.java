package modelo;

/** MODELO: datos puros de una editorial. */
public class Editorial {
    private String idEditorial;
    private String nombre;
    private String correoContacto;

    public Editorial(String idEditorial, String nombre, String correoContacto) {
        this.idEditorial = idEditorial;
        this.nombre = nombre;
        this.correoContacto = correoContacto;
    }

    public String getIdEditorial() { return idEditorial; }
    public String getNombre() { return nombre; }
    public String getCorreoContacto() { return correoContacto; }
}
