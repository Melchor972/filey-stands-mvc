package excepciones;

/** Se intento actualizar o borrar (Update / Delete) algo que no existe. */
public class RegistroNoEncontradoException extends FileyException {
    public RegistroNoEncontradoException(String entidad, Object id) {
        super("No existe " + entidad + " con id " + id);
    }
}
