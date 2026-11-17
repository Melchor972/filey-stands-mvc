package bd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * CAPA DE BD: crea las tablas y carga datos de ejemplo ejecutando
 * schema.sql y datos.sql. Solo se usa con BD en memoria (H2), porque
 * ahi las tablas desaparecen al cerrar el programa.
 */
public class InicializadorBD {

    private InicializadorBD() { }

    /** Crea tablas y datos de ejemplo solo si db.properties lo indica (bd.inicializar=true). */
    public static void inicializar() throws SQLException {
        if (!Boolean.parseBoolean(ConexionBD.getPropiedad("bd.inicializar", "false"))) {
            return;
        }
        ejecutarScript("/schema.sql");
        ejecutarScript("/datos.sql");
    }

    /** Prueba de conexion para el main: devuelve una descripcion de la BD conectada. */
    public static String probarConexion() throws SQLException {
        try (Connection con = ConexionBD.getConexion()) {
            if (!con.isValid(2)) {
                throw new SQLException("La conexion no es valida");
            }
            return con.getMetaData().getDatabaseProductName() + " "
                    + con.getMetaData().getDatabaseProductVersion()
                    + " | URL: " + con.getMetaData().getURL();
        }
    }

    private static void ejecutarScript(String recurso) throws SQLException {
        String contenido = leerRecurso(recurso);
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement()) {
            for (String sentencia : contenido.split(";")) {
                String sql = sentencia.trim();
                if (!sql.isEmpty()) {
                    st.execute(sql);
                }
            }
        }
    }

    /** Lee el archivo ignorando las lineas de comentario que empiezan con "--". */
    private static String leerRecurso(String recurso) {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = InicializadorBD.class.getResourceAsStream(recurso)) {
            if (in == null) {
                throw new IllegalStateException("No se encontro " + recurso);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().startsWith("--")) {
                    sb.append(linea).append('\n');
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + recurso, e);
        }
        return sb.toString();
    }
}
