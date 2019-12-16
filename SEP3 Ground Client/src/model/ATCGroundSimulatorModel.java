package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import groundclient.GroundClient;
import groundclient.GroundIClient;
import model.NodeDTO;
import model.PlaneDTO;
import model.Timer;

public class ATCGroundSimulatorModel implements ATCGroundSimulator, Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 3218559717712719996L;
   private GroundIClient client;
   private PropertyChangeSupport support = new PropertyChangeSupport(this);

   public ATCGroundSimulatorModel()
   {

   }

   @Override
   public void getPlaneDTOFromServer(PlaneDTO plane)
   {
      support.firePropertyChange("planeADD", " ", plane);
   }

   @Override
   public void setClient(GroundClient groundClient)
   {
      this.client = groundClient;
   }

   public void changePlaneRoute(String registrationNo, int startNodeId,
         int endNodeId)
   {
      try
      {
         client.changeGroundPlaneRoute(registrationNo, startNodeId, endNodeId);
      }
      catch (RemoteException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Override
   public void getGroundPlanesDTOFromServer(ArrayList<PlaneDTO> planes)
   {
      support.firePropertyChange("positionUPDATE", " ", planes);

   }

   @Override
   public void addPropertyChangeListener(PropertyChangeListener listener)
   {
      support.addPropertyChangeListener(listener);

   }

   @Override
   public void simulationFailed()
   {
      support.firePropertyChange("simulationFAILED", " ", true);

   }

   @Override
   public void getGroundNodesDTOFromServer(ArrayList<NodeDTO> nodes)
   {
      support.firePropertyChange("nodeADD", " ", nodes);
   }

   @Override
   public void getWindFromServer(boolean wind)
   {
      support.firePropertyChange("windADD", " ", wind);
   }

   @Override
   public void getTimerFromServer(Timer timer)
   {
      support.firePropertyChange("timerUPDATE", " ", timer);
   }

   @Override
   public void removePlane(int index)
   {
      support.firePropertyChange("planeREMOVE", " ", index);

   }

   @Override
   public void establishConnection()
   {
      try
      {
         client.establishConnection();
      }
      catch (RemoteException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Override
   public void simulationStart()
   {
      support.firePropertyChange("simulationSTART", " ", true);

   }

   @Override
   public void stopPlane(String registrationNo)
   {
      try
      {
         client.stopPlane(registrationNo);
      }
      catch (RemoteException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }
}
