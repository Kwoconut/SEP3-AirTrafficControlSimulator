package groundclient;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import model.ATCGroundSimulatorModelClientHandler;
import model.NodeDTO;
import model.PlaneDTO;
import model.Timer;
import server.GroundRIServerRead;
import server.GroundRIServerWrite;
import server.ServerAccess;

public class GroundClient implements GroundRIClient, GroundIClient, Serializable
{
   private static final long serialVersionUID = 1L;
   private ATCGroundSimulatorModelClientHandler model;
   private ServerAccess access;

   public GroundClient(ATCGroundSimulatorModelClientHandler model)
         throws RemoteException, NotBoundException, MalformedURLException
   {
      this.model = model;
      this.model.setClient(this);
      access = (ServerAccess) Naming.lookup("rmi://localhost:1099/server");
      UnicastRemoteObject.exportObject(this, 0);
   }

   @Override
   public void getGroundPlaneDTOFromServer(PlaneDTO plane)
         throws RemoteException
   {
      model.getPlaneDTOFromServer(plane);
   }

   @Override
   public void getGroundPlanesDTOFromServer(ArrayList<PlaneDTO> planes)
         throws RemoteException
   {
      model.getGroundPlanesDTOFromServer(planes);
   }

   @Override
   public void getGroundNodesDTOFromServer(ArrayList<NodeDTO> nodes)
         throws RemoteException
   {
      model.getGroundNodesDTOFromServer(nodes);

   }

   @Override
   public void getWindFromServer(boolean wind) throws RemoteException
   {
      model.getWindFromServer(wind);

   }

   @Override
   public void simulationFailed() throws RemoteException
   {
      model.simulationFailed();
   }

   @Override
   public void changeGroundPlaneRoute(String registrationNo, int startNodeId,
         int endNodeId) throws RemoteException
   {
      GroundRIServerWrite server = access.acquireGroundWrite();
      server.changeGroundPlaneRoute(registrationNo, startNodeId, endNodeId);
      access.releaseWrite();
   }

   @Override
   public void getTimerFromServer(Timer timer) throws RemoteException
   {
      this.model.getTimerFromServer(timer);
   }

   @Override
   public void removeGroundPlane(int index) throws RemoteException
   {
      this.model.removePlane(index);
   }

   public void establishConnection() throws RemoteException
   {
      GroundRIServerWrite server = access.acquireGroundWrite();
      server.addGroundClient(this);
      access.releaseWrite();
      GroundRIServerRead serverRead = access.acquireGroundRead();
      serverRead.sendGroundNodesDTO(this);
      serverRead.sendGroundPlanesDTO(this);
      serverRead.sendGroundWind(this);
      access.releaseRead();
   }

   @Override
   public void simulationStart() throws RemoteException
   {
      this.model.simulationStart();
   }

   @Override
   public void stopPlane(String registrationNo) throws RemoteException
   {
      GroundRIServerWrite server = access.acquireGroundWrite();
      server.stopPlane(registrationNo);
      access.releaseWrite();

   }
}
