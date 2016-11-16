package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Iemergency extends Remote
{
     void receiveMessage(int number, String type, String location, IClient client) throws RemoteException;
     void checkOffense(boolean bool) throws RemoteException;
}


