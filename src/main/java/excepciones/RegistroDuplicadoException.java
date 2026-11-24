package excepciones;

/** Se intento registrar (Create) un ID que ya existe. */
public class RegistroDuplicadoException extends FileyException {
    public RegistroDuplicadoException(String entidad, Object id) {
        super("Ya existe " + entidad + " con id " + id);
    }
}
