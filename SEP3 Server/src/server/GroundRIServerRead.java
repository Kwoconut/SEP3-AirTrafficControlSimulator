	package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import groundclient.GroundRIClient;

public interface GroundRIServerRead extends Remote
{
	void sendGroundPlanesDTO(GroundRIClient client) throws RemoteException;
	
	void sendGroundNodesDTO(GroundRIClient client) throws RemoteException;

    void sendGroundWind(GroundRIClient client) throws RemoteException;
}
