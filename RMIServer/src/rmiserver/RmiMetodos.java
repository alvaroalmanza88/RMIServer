/*
 * CODIGO REALIZADO POR ALVARO ALMANZA.
 * UNIVERSIDAD INTERNACIONAL I DE CASTILLA
 */
package rmiserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;
import static rmiserver.RMIServer.DB_URL;
import static rmiserver.RMIServer.JDBC_DRIVER;
import static rmiserver.RMIServer.PASS;
import static rmiserver.RMIServer.USER;

/**
 *
 * @author alvar
 */
public class RmiMetodos extends UnicastRemoteObject implements RmiInterface
{    
    
    
    public RmiMetodos() throws RemoteException 
    {
        super();
    }
    
    @Override
    public boolean doUserLogin(String usuario, String pass) throws RemoteException 
    {
        Connection conexion;
        boolean comprobacion=false;
        Statement st;
        String passCOD = "";
        
        // Codificamos la contraseña antes de almacenarla
        try {
            passCOD = stringMD5(pass);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        try {
            conexion = conectarBD();
            st = conexion.createStatement();
            String sql = "SELECT COUNT(*) AS NUM "
                    + "FROM g1_tienda_virtual.usuarios "
                    + "WHERE V_EMAIL = '" + usuario + "' "
                    + "and V_PASS = '" + passCOD + "';";
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next())
                {
                    if ( 1 == rs.getInt("NUM"))
                       comprobacion = true;
                }
            }
            st.close();
            desconectarBD(conexion);
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comprobacion;
    }

    /**
     *
     * @param usuario
     * @param pass
     * @param email
     * @param email_conf
     * @param telefono
     * @return
     * @throws RemoteException
     */
    @Override
    public String registerNewUser(String usuario, String pass, String email,
            String email_conf, String telefono) throws RemoteException 
    {       
        Connection conexion;
        Statement st;
        String passCOD;
        
        if (0 == usuario.length())
           return "EL NOMBRE DE USUARIO ESTA VACIO";
        
        if (!validarNombre(usuario))
            return "EL NOMBRE DE USUARIO DEBE SER ALFANUMÉRICO";
        
        if (!validarPass(pass))
            return "FORMATO DE CONTRASEÑA INCORRECTO";
        
        if (!validarEmail(email))
            return "FORMATO DE EMAIL INCORRECTO";
        
        if (!email.equals(email_conf))
            return "LOS EMAILS NO COINCIDEN";
        
        if (!validarTelefono(telefono))
            return "EL FORMATO DEL TELEFONO NO ES CORRECTO";
        
        // Codificamos la contraseña antes de almacenarla
        try {
            passCOD = stringMD5(pass);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
            return "SE PRODUCJO UN ERROR INTERNO";
        } catch (IOException ex) {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
            return "SE PRODUCJO UN ERROR INTERNO";
        }
        
        try {
            conexion = conectarBD();
            st = conexion.createStatement();
            String sql = "INSERT INTO `g1_tienda_virtual`.`usuarios` ("
                    + "`V_NOMBRE`, `V_EMAIL`, `V_PASS`, `V_TELEFONO`) "
                    + "VALUES ("
                    + "'" + usuario + "', '" + email + "', "
                    + "'" + passCOD + "', "
                    + "'" + telefono + "');";
            
            System.out.println(sql);
            st.executeUpdate(sql);
            
            st.close();
            desconectarBD(conexion);
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "200";
    }

    @Override
    public boolean insertProductInShop(String producto, String precio) throws RemoteException 
    {
        Connection conexion;
        Statement st;
        
        if(0 == producto.length() || 0 == precio.length())
            return false;
        
        if (!validarFloat(precio))
        {
            return false;
        }
        try {
            conexion = conectarBD();
            st = conexion.createStatement();
            
            String sql = "INSERT INTO `g1_tienda_virtual`.`productos` ("
                    + "`V_PRODUCTO`, `F_PRECIO`) "
                    + "VALUES ("
                    + "'" + producto + "', "
                    + "'" + precio + "');";
            
            System.out.println(sql);
            
            st.executeUpdate(sql);
            st.close();
            desconectarBD(conexion);
            
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public List<String> showProductsInShop() throws RemoteException 
    {
       List<String> resultado = new ArrayList<>();
       Connection conexion;
       
       Statement st;
        try {
            conexion = conectarBD();
            st = conexion.createStatement();
            String sql = "SELECT `V_PRODUCTO` AS PRODUCTO"
                    + " FROM g1_tienda_virtual.productos";
            ResultSet rs= st.executeQuery(sql);
            
            while(rs.next())
            {
                resultado.add(rs.getString("PRODUCTO"));
            }
            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       return resultado;
    }

    @Override
    public boolean deleteProductoInShop(String producto) throws RemoteException 
    {
        Connection conexion;
        Statement st;
        
        try {
            conexion = conectarBD();
            st = conexion.createStatement();
            String sql = "DELETE FROM `g1_tienda_virtual`.`productos` "
                    + "WHERE `V_PRODUCTO`='" + producto + "';";
            int deleted = st.executeUpdate(sql);
            
            return 0 != deleted;
            
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
       return true;
    }
    
    private Connection conectarBD()
    {
        Connection conexion = null;
        try
        {
            Class.forName(JDBC_DRIVER);
            conexion = DriverManager.getConnection(DB_URL, USER,PASS);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            System.out.println("NO SE HA PODIDO ACCEDER A LA BASE DE DATOS");
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        return(conexion);
    }
    
    private void desconectarBD(Connection conexion)
    {        
        try {
            conexion.close();
        } catch (SQLException ex) {
            System.out.println("NO SE HA PODIDO CERRAR LA BASE DE DATOS");
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }
    
    private String stringMD5 (String cadena)
        throws NoSuchAlgorithmException, IOException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(cadena.getBytes());
        byte[] digest = md.digest();
        String cadenaMD = DatatypeConverter
        .printHexBinary(digest).toLowerCase();
        
        return cadenaMD;
    }

    private boolean validarNombre(final String cadena) 
    {
        for(int i = 0; i < cadena.length(); ++i) {
            char caracter = cadena.charAt(i);

            if(!Character.isLetterOrDigit(caracter)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean validarPass(String pass)
    {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return pass.matches(pattern);
    }
    
    private boolean validarEmail(String email)
    {        
        String mail_string = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern pattern = Pattern.compile(mail_string);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    private boolean validarTelefono(String telefono)
    {
        String phone_string = "\\+?\\d{2}[ -.]?\\d{7,9}|[1234567890 -.]{10,16}";
        Pattern pattern = Pattern.compile(phone_string);
        Matcher matcher = pattern.matcher(telefono);
        return matcher.matches();
    }
    
    private boolean validarFloat(String numero)
    {
        try
        {
            Float.parseFloat(numero);
            return true;
        }
        catch(NumberFormatException | NullPointerException ex)
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
