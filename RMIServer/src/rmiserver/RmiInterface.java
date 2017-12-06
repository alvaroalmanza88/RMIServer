/*
 * CODIGO REALIZADO POR ALVARO ALMANZA.
 * UNIVERSIDAD INTERNACIONAL I DE CASTILLA
 */
package rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author alvar
 */
public interface RmiInterface extends Remote
{
    public boolean  doUserLogin (String usuario, String pass)throws RemoteException;
    public String  registerNewUser (String usuario, String pass, String email, String email_conf, String telefono) throws RemoteException;
    public boolean  insertProductInShop (String producto, String precio)throws RemoteException;
    public List<String>  showProductsInShop ()throws RemoteException;
    public boolean  deleteProductoInShop (String producto)throws RemoteException;
}
