package groundclient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import model.NodeDTO;
import model.PlaneDTO;
import model.Timer;

public interface GroundRIClient extends Remote
{
	public void getGroundPlaneDTOFromServer(PlaneDTO plane) throws RemoteException;
	
	public void getGroundPlanesDTOFromServer(ArrayList<PlaneDTO> planes) throws RemoteException;
	
	public void getGroundNodesDTOFromServer(ArrayList<NodeDTO> nodes) throws RemoteException;
		
	public void getWindFromServer(boolean wind) throws RemoteException;

	public void simulationFailed() throws RemoteException;

   public void getTimerFromServer(Timer timer) throws RemoteException;
   
   public void removeGroundPlane(int index) throws RemoteException;
   
   public void simulationStart() throws RemoteException;

}