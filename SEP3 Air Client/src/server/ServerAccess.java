package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAccess extends Remote{

   AirRIServerRead acquireAirRead() throws RemoteException;

   void releaseRead() throws RemoteException;

   AirRIServerWrite acquireAirWrite() throws RemoteException;

   void releaseWrite() throws RemoteException;

}
