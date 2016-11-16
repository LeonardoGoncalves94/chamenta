package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
    void sendFeedback(boolean bool) throws RemoteException;
}
