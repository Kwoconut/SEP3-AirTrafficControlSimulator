package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import airclient.AirRIClient;

public interface AirRIServerRead extends Remote
{
	void sendAirPlanesDTO(AirRIClient client) throws RemoteException;
	
	void sendAirNodesDTO(AirRIClient client) throws RemoteException;

    void sendAirWind(AirRIClient client) throws RemoteException;
}
