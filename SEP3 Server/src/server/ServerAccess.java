package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAccess extends Remote{

   GroundRIServerRead acquireGroundRead() throws RemoteException;

   AirRIServerRead acquireAirRead() throws RemoteException;

   void releaseRead() throws RemoteException;

   GroundRIServerWrite acquireGroundWrite() throws RemoteException;

   AirRIServerWrite acquireAirWrite() throws RemoteException;

   void releaseWrite() throws RemoteException;

}
