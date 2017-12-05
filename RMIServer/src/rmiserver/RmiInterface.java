/*
 * CODIGO REALIZADO POR ALVARO ALMANZA.
 * UNIVERSIDAD INTERNACIONAL I DE CASTILLA
 */
package rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author alvar
 */
public interface RmiInterface extends Remote
{
    public boolean  doUserLogin (String Usuario, String Pass)throws RemoteException;
    public boolean  registerNewUser (String Usuario, String Pass)throws RemoteException;
    public boolean  insertProductInShop (String producto, String Pass)throws RemoteException;
    public boolean  showProductsInShop ()throws RemoteException;
    public boolean  deleteProductoInShop (String producto)throws RemoteException;
}
