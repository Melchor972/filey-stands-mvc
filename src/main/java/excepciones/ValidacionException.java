package excepciones;

/** Datos invalidos: campos vacios, metros negativos, correo sin @, porcentaje fuera de rango... */
public class ValidacionException extends FileyException {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
