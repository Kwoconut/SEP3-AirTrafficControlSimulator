package airclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import model.ATCAirSimulatorModelClientHandler;
import model.NodeDTO;
import model.PlaneDTO;
import model.StaticPosition;
import model.Timer;
import server.AirRIServerRead;
import server.AirRIServerWrite;
import server.ServerAccess;

public class AirClient implements AirIClient, AirRIClient
{

   private ATCAirSimulatorModelClientHandler model;
   private ServerAccess access;

   public AirClient(ATCAirSimulatorModelClientHandler model)
         throws RemoteException, MalformedURLException, NotBoundException
   {
      this.model = model;
      this.model.setClient(this);
      Registry reg = LocateRegistry.getRegistry("localhost", 1099);
      access = (ServerAccess)reg.lookup("server");
     // access = (AirServerAccess) Naming.lookup("rmi://localhost:1099/server");
      UnicastRemoteObject.exportObject(this, 0);
   }

   @Override
   public void getAirPlaneDTOFromServer(PlaneDTO plane) throws RemoteException
   {
      this.model.getPlaneDTOFromServer(plane);

   }

   @Override
   public void getAirPlanesDTOFromServer(ArrayList<PlaneDTO> planes)
         throws RemoteException
   {
      this.model.getAirPlanesDTOFromServer(planes);

   }

   @Override
   public void getAirNodesDTOFromServer(ArrayList<NodeDTO> nodes)
         throws RemoteException
   {
      this.model.getAirNodesDTOFromServer(nodes);

   }

   @Override
   public void getWindFromServer(boolean wind) throws RemoteException
   {
      this.model.getWindFromServer(wind);

   }

   @Override
   public void simulationFailed() throws RemoteException
   {
      this.model.simulationFailed();

   }

   @Override
   public void getTimerFromServer(Timer timer) throws RemoteException
   {
      this.model.getTimerFromServer(timer);

   }

   @Override
   public void reRoutePlane(String registrationNo, StaticPosition position)
   {
      try
      {
         AirRIServerWrite server = this.access.acquireAirWrite();
         server.reRoutePlane(registrationNo, position);
         this.access.releaseWrite();
      }
      catch (RemoteException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   @Override
   public void removeAirPlane(int index) throws RemoteException
   {
      this.model.removePlane(index);
   }

   @Override
   public void simulationStart() throws RemoteException
   {
      this.model.simulationStart();

   }

   @Override
   public void establishConnection() throws RemoteException
   {
      AirRIServerWrite server = access.acquireAirWrite();
      server.addAirClient(this);
      access.releaseWrite();
      AirRIServerRead serverRead = access.acquireAirRead();
      serverRead.sendAirNodesDTO(this);
      serverRead.sendAirPlanesDTO(this);
      serverRead.sendAirWind(this);
      access.releaseRead();

   }

   @Override
   public void setPlaneOnCourse(String registrationNo, int startNodeId)
         throws RemoteException
   {
      AirRIServerWrite server = access.acquireAirWrite();
      server.setPlaneOnCourse(registrationNo,startNodeId);
      access.releaseWrite();
      
   }

}
