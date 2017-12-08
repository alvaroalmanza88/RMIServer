/*
 * CODIGO REALIZADO POR ALVARO ALMANZA.
 * UNIVERSIDAD INTERNACIONAL I DE CASTILLA
 */
package rmiserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author alvar
 */
public class RMIServer 
{

    /**
     * @param args the command line arguments
     */
    public static Connection conexion;
    public static Statement st;
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    public static String DB_URL = "jdbc:mysql://localhost";
    public static String USER = "root";
    public static String PASS = "";
    
    public static void main(String[] args) throws RemoteException 
    {
        ConexionBD();
        // Una vez comprobado que existe la base de datos o se ha creado
        // se levanta el servidor.
        Registry miRegistry = LocateRegistry.createRegistry(1023);
        miRegistry.rebind("Tienda", new RmiMetodos());
    }
    
    
    /**
     * Crea una conexión con el motor de base de datos. Si no existe se crea:
     *    - La base de datos.
     *    - Las tablas usuarios, productos.
     *    - Un primer usuario admin con clave admin
     * 
     */
    private static void ConexionBD()
    {
        try
       {
            Class.forName(JDBC_DRIVER);
            conexion = DriverManager.getConnection(DB_URL, USER,PASS);
            st= conexion.createStatement();

            String sql = "CREATE DATABASE IF NOT EXISTS g1_tienda_virtual "
                    + "DEFAULT CHARACTER SET utf8 "
                    + "DEFAULT COLLATE utf8_general_ci;";
            st.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS `g1_tienda_virtual`.`usuarios` (" +
                "  `I_ID` INT NOT NULL AUTO_INCREMENT," +
                "  `V_NOMBRE` VARCHAR(100) NOT NULL," +
                "  `V_EMAIL` VARCHAR(100) NOT NULL," +
                "  `V_PASS` VARCHAR(32) NOT NULL," +
                "  `V_TELEFONO` VARCHAR(20) NOT NULL," +
                "  PRIMARY KEY (`I_ID`));";

            st.executeUpdate(sql);
           
            sql = "SELECT count(*) AS NUM FROM g1_tienda_virtual.usuarios;";
            int registros;
            try (ResultSet rs = st.executeQuery(sql)) {
                rs.next();
                registros = rs.getInt("NUM");
            }
           
            if (0 == registros)
            {
                sql = "INSERT INTO `g1_tienda_virtual`.`usuarios` ("
                    + "`V_NOMBRE`, `V_EMAIL`, `V_PASS`, `V_TELEFONO`) "
                    + "VALUES ("
                    + "'admin', 'admin@admin.com', "
                    + "'21232f297a57a5a743894a0e4a801fc3', "
                    + "'111111111');";
                st.executeUpdate(sql);
            }
           
            sql = "CREATE TABLE IF NOT EXISTS `g1_tienda_virtual`.`productos` (" +
                "  `I_ID` INT NOT NULL AUTO_INCREMENT," +
                "  `V_PRODUCTO` VARCHAR(100) NOT NULL," +
                "  `f_PRECIO` FLOAT NOT NULL," +
                "  PRIMARY KEY (`I_ID`));";
           
            st.executeUpdate(sql);          
            st.close();
            conexion.close();

            System.out.println("conectado");
              
        } catch (ClassNotFoundException | SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ACCEDER A LA BASE DE DATOS", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.getMessage());
            System.exit(0);
        }         
    }
}
