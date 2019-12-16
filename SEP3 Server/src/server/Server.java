package server;

import java.io.IOException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import airclient.AirRIClient;
import groundclient.GroundRIClient;
import model.PlaneDTO;
import model.StaticPosition;

public class Server implements GroundRIServerWrite, AirRIServerWrite
{
   private ServerModel model;
   private SimulationManager manager;
   private ArrayList<GroundRIClient> groundClients;
   private ArrayList<AirRIClient> airClients;

   public Server(ServerModel model) throws IOException
   {
      this.model = model;
      groundClients = new ArrayList<GroundRIClient>();
      airClients = new ArrayList<AirRIClient>();
      UnicastRemoteObject.exportObject(this, 0);
      manager = new SimulationManager(this);
   }

   public void addGroundClient(GroundRIClient client) throws RemoteException
   {
      if (groundClients.size() >= 1)
      {
         for (int i = 0; i < model.getSimulationGroundPlanes().size(); i++)
         {
            sendGroundPlaneDTO(model.getSimulationGroundPlanesDTO().get(i),
                  client);
         }
      }
      groundClients.add(client);
      if (airClients.size() >= 1 && groundClients.size() >= 1
            && manager.getAloha() == false)
      {
         startSimulation();
         manager.startThreads();
      }
      else if (manager.getAloha() == true)
      {
         client.simulationStart();
      }
   }


   public void addAirClient(AirRIClient client) throws RemoteException
   {
      if (airClients.size() >= 1)
      {
         for (int i = 0; i < model.getSimulationAirPlanes().size(); i++)
         {
            sendAirPlaneDTO(model.getSimulationAirPlanesDTO().get(i), client);
         }
      }
      airClients.add(client);
      if (airClients.size() >= 1 && groundClients.size() >= 1
            && manager.getAloha() == false)
      {
         startSimulation();
         manager.startThreads();
      }
      else if (manager.getAloha() == true)
      {
         client.simulationStart();
      }
   }
   
   public void startSimulation() throws RemoteException
   {
      for (int i = 0; i < groundClients.size(); i++)
      {
         groundClients.get(i).simulationStart();
      }
      for (int i = 0; i < airClients.size(); i++)
      {
         airClients.get(i).simulationStart();
      }
   }

   public ArrayList<GroundRIClient> getGroundClients()
   {
      return groundClients;
   }

   public ArrayList<AirRIClient> getAirClients()
   {
      return airClients;
   }

   public ServerModel getModel()
   {
      return model;
   }

   @Override
   public void sendGroundPlanesDTO(GroundRIClient client) throws RemoteException
   {
      client.getGroundPlanesDTOFromServer(model.getSimulationGroundPlanesDTO());
   }

   @Override
   public void sendAirPlanesDTO(AirRIClient client) throws RemoteException
   {
      client.getAirPlanesDTOFromServer(model.getSimulationAirPlanesDTO());
   }

   public void sendGroundNodesDTO(GroundRIClient client) throws RemoteException
   {
      client.getGroundNodesDTOFromServer(model.getGroundNodesDTO());
   }

   public void sendAirNodesDTO(AirRIClient client) throws RemoteException
   {
      client.getAirNodesDTOFromServer(model.getAirNodesDTO());
   }

   @Override
   public void sendGroundWind(GroundRIClient client) throws RemoteException
   {
      client.getWindFromServer(model.getWind());
   }

   @Override
   public void sendAirWind(AirRIClient client) throws RemoteException
   {
      client.getWindFromServer(model.getWind());
   }

   public void sendGroundPlaneDTO(PlaneDTO plane, GroundRIClient client)
         throws RemoteException
   {
      client.getGroundPlaneDTOFromServer(plane);

   }

   public void removeGroundPlane(GroundRIClient client, int index)
         throws RemoteException
   {
      client.removeGroundPlane(index);
   }

   public void removeAirPlane(AirRIClient client, int index)
         throws RemoteException
   {
      client.removeAirPlane(index);
   }

   public void sendAirPlaneDTO(PlaneDTO plane, AirRIClient client)
         throws RemoteException
   {
      client.getAirPlaneDTOFromServer(plane);

   }

   public void simulationFailed(GroundRIClient client) throws RemoteException
   {
      client.simulationFailed();
   }

   public void airSimulationFailed(AirRIClient client) throws RemoteException
   {
      client.simulationFailed();
   }

   @Override
   public void changeGroundPlaneRoute(String registrationNo, int startNodeId,
         int endNodeId)
   {
      model.changeGroundPlaneRoute(registrationNo, startNodeId, endNodeId);
   }

   public void execute() throws IOException
   {
      System.out.println("Starting socket part");
      System.out.println("Waiting for clients ...");
      Socket socket = new Socket("10.152.218.92", 6789);
      Thread t = new Thread(new ServerSocketHandler(model, socket));
      t.start();
   }

   public static void main(String[] args) throws IOException
   {
      try
      {
         LocateRegistry.createRegistry(1099);
         ServerModel model = new ServerModel();
         Server server = new Server(model);
         ServerAccess threadSafeServer = new ThreadSafeServer(server);
         Naming.rebind("server", threadSafeServer);
         System.out.println("Starting RMI part");
         server.execute();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   @Override
   public void reRoutePlane(String callSign, StaticPosition position)
         throws RemoteException
   {
      this.model.reRoutePlane(callSign, position);

   }

   @Override
   public void stopPlane(String registrationNo)
   {
      this.model.stopPlane(registrationNo);
      
   }

   @Override
   public void setPlaneOnCourse(String registrationNo, int startNodeId)
         throws RemoteException
   {
      this.model.setPlaneOnCourse(registrationNo, startNodeId);
      
   }
}
