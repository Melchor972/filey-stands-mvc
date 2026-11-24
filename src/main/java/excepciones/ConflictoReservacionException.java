package excepciones;

/** Conflicto de reglas: stand no disponible, o borrar algo que tiene reservaciones. */
public class ConflictoReservacionException extends FileyException {
    public ConflictoReservacionException(String mensaje) {
        super(mensaje);
    }
}
