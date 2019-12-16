package groundclient;

import java.rmi.RemoteException;

public interface GroundIClient {
	void changeGroundPlaneRoute(String registrationNo, int startNodeId, int endNodeId) throws RemoteException;

	void establishConnection() throws RemoteException;

	void stopPlane(String registrationNo) throws RemoteException;
}
