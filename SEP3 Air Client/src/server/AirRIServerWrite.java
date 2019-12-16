package server;

import java.rmi.RemoteException;

import airclient.AirRIClient;
import model.StaticPosition;

public interface AirRIServerWrite extends AirRIServerRead
{
	void addAirClient(AirRIClient client) throws RemoteException;

	void reRoutePlane(String registrationNo,StaticPosition position) throws RemoteException;

   void setPlaneOnCourse(String registrationNo, int startNodeId) throws RemoteException;
}
