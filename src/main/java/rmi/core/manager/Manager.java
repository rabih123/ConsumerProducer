package rmi.core.manager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Manager extends Remote
{
    void setTranslatedVal(String val, String transVal, String language) throws RemoteException;

    void notifyMangr() throws RemoteException;

    void executeJob(String popVal);

    void connectAdaptors();
}
