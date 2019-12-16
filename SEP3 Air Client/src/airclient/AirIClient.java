package airclient;

import java.rmi.RemoteException;

import model.StaticPosition;

public interface AirIClient
{
   void reRoutePlane(String registrationNo, StaticPosition position)
         throws RemoteException;

   void establishConnection() throws RemoteException;

   void setPlaneOnCourse(String registrationNo, int startNodeId) throws RemoteException;

}
