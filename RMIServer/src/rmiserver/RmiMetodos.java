/*
 * CODIGO REALIZADO POR ALVARO ALMANZA.
 * UNIVERSIDAD INTERNACIONAL I DE CASTILLA
 */
package rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import static rmiserver.RMIServer.conexion;

/**
 *
 * @author alvar
 */
public class RmiMetodos extends UnicastRemoteObject implements RmiInterface
{
    Connection Con = conexion;
    
    public RmiMetodos() throws RemoteException 
    {
        String Usuario, pass, producto;
    }

    @Override
    public boolean doUserLogin(String Usuario, String Pass) throws RemoteException 
    {
       boolean comprobacion=false;
       Statement st;
        try {
            st = Con.createStatement();
            st.executeQuery("AQUI VA LA QUERY");
            comprobacion=true;
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
       return comprobacion;
    }

    @Override
    public boolean registerNewUser(String Usuario, String Pass) throws RemoteException 
    {
       boolean comprobacion=false;
       Statement st;
        try {
            st = Con.createStatement();
            st.executeQuery("AQUI VA LA QUERY");
            comprobacion=true;
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
       return comprobacion;
    }

    @Override
    public boolean insertProductInShop(String producto, String Pass) throws RemoteException 
    {
       boolean comprobacion=false;
       Statement st;
        try {
            st = Con.createStatement();
            st.executeQuery("AQUI VA LA QUERY");
            comprobacion=true;
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
       return comprobacion;
    }

    @Override
    public boolean showProductsInShop() throws RemoteException 
    {
       boolean comprobacion=false;
       String tabla="temporal";
       Statement st;
        try {
            st = Con.createStatement();
            ResultSet r1= st.executeQuery("Select * from '"+tabla+"'");
            comprobacion=true;
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
       return comprobacion;
    }

    @Override
    public boolean deleteProductoInShop(String producto) throws RemoteException 
    {
       boolean comprobacion=false;
       Statement st;
        try {
            st = Con.createStatement();
            st.executeQuery("AQUI VA LA QUERY");
            comprobacion=true;
        } catch (SQLException ex) 
        {
            Logger.getLogger(RmiMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
       return comprobacion;
    }

}
