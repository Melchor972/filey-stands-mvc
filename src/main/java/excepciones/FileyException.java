package excepciones;

/**
 * Excepcion base de errores LOGICOS / de validacion del sistema.
 * Los errores tecnicos de la BD siguen siendo java.sql.SQLException.
 */
public class FileyException extends Exception {
    public FileyException(String mensaje) {
        super(mensaje);
    }
}
