/*
 * CODIGO REALIZADO POR ALVARO ALMANZA.
 * UNIVERSIDAD INTERNACIONAL I DE CASTILLA
 */
package rmiserver;

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
    public static void main(String[] args) 
    {
        ConexionBD();
    }
    
    private static void ConexionBD()
        {
            try
           {
               Class.forName("com.mysql.jdbc.Driver");
               conexion = DriverManager.getConnection("jdbc:mysql://localhost/TestLinux", "root","88192102");
               st= conexion.createStatement();
               System.out.println("conectado");
           } catch (ClassNotFoundException | SQLException ex) 
           {
               JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ACCEDER A LA BASE DE DATOS", "Error", JOptionPane.ERROR_MESSAGE);
               System.out.println(ex.getMessage());
               System.exit(0);
           }         
        }
}
