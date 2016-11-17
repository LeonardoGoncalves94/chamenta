package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Key;
import java.security.KeyPair;

public interface Iemergency extends Remote
{
     void receiveMessage(int number, String type, String location, IClient client) throws RemoteException;
     void checkOffense(boolean bool) throws RemoteException;
     Key getPublicKey() throws RemoteException;
     int registerChannel(String secret) throws RemoteException, Exception;
}


