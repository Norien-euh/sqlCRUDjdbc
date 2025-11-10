import java.sql.*;

public class Main {
    private static Connection conexion = null;

    public static void main(String[] args) {
        DriverManager.drivers().forEach(driver -> System.out.println(driver.toString()));

        if (realizarConexion()) {
            seleccionarEnemigos();
            insertarEnemigo("Carlos", "Francia", "Espía internacional");
            modificarEnemigo("Carlos", "Alemania", "Agente doble");
            borrarEnemigo("Carlos");
            seleccionarEnemigos();

            cerrarConexion();
        } else {
            System.out.println("No se puede continuar sin conexión a la base de datos");
        }
    }

    public static boolean realizarConexion(){
        try {
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Enemigos?user=postgres&password=norienIsRoot");
            System.out.println("Conexion realizada");
            return true;
        }catch (SQLException e){
            System.out.println("No se conecto la bd");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void cerrarConexion(){
        if(conexion!=null){
            try {
                conexion.close();
                System.out.println("Conexion cerrada");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void seleccionarEnemigos(){
        try{
            Statement consulta = conexion.createStatement();
            ResultSet resultado = consulta.executeQuery("SELECT * FROM enemigosespana");

            System.out.println("\n--- LISTA DE ENEMIGOS ---");
            while (resultado.next()){
                String nombre = resultado.getString("nombre");
                String pais = resultado.getString("pais");
                String afiliacion = resultado.getString("afiliacion");
                System.out.println("ENEMIGO: "+ nombre +" - País: "+ pais +" - Afiliación: "+ afiliacion);
            }
            System.out.println("--- FIN DE LA LISTA ---\n");

        } catch (SQLException e) {
            System.out.println("Error al seleccionar enemigos: " + e.getMessage());
        }
    }

    public static void insertarEnemigo(String nombre, String pais, String afiliacion){
        try{
            Statement consulta = conexion.createStatement();
            String sql = "INSERT INTO enemigosespana (nombre, pais, afiliacion) VALUES ('" + nombre + "', '" + pais + "', '" + afiliacion + "')";
            int filasAfectadas = consulta.executeUpdate(sql);

            if(filasAfectadas > 0){
                System.out.println("Enemigo '" + nombre + "' insertado correctamente");
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar enemigo: " + e.getMessage());
        }
    }

    public static void modificarEnemigo(String nombre, String nuevoPais, String nuevaAfiliacion){
        try{
            Statement consulta = conexion.createStatement();
            String sql = "UPDATE enemigosespana SET pais = '" + nuevoPais + "', afiliacion = '" + nuevaAfiliacion + "' WHERE nombre = '" + nombre + "'";
            int filasAfectadas = consulta.executeUpdate(sql);

            if(filasAfectadas > 0){
                System.out.println("Enemigo '" + nombre + "' modificado correctamente");
            } else {
                System.out.println("No se encontró el enemigo '" + nombre + "' para modificar");
            }

        } catch (SQLException e) {
            System.out.println("Error al modificar enemigo: " + e.getMessage());
        }
    }

    public static void borrarEnemigo(String nombre){
        try{
            Statement consulta = conexion.createStatement();
            String sql = "DELETE FROM enemigosespana WHERE nombre = '" + nombre + "'";
            int filasAfectadas = consulta.executeUpdate(sql);

            if(filasAfectadas > 0){
                System.out.println("Enemigo '" + nombre + "' eliminado correctamente");
            } else {
                System.out.println("No se encontró el enemigo '" + nombre + "' para eliminar");
            }

        } catch (SQLException e) {
            System.out.println("Error al borrar enemigo: " + e.getMessage());
        }
    }
}