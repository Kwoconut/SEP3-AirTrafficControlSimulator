package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.ArrayList;

import airclient.AirClient;
import airclient.AirIClient;
import model.NodeDTO;
import model.PlaneDTO;
import model.StaticPosition;
import model.Timer;

public class ATCAirSimulatorModel implements ATCAirSimulator
{

   private AirIClient client;
   private PropertyChangeSupport support = new PropertyChangeSupport(this);

   public ATCAirSimulatorModel()
   {

   }

   @Override
   public void setClient(AirClient airClient)
   {
      this.client = airClient;

   }

   @Override
   public void getPlaneDTOFromServer(PlaneDTO plane)
   {
      support.firePropertyChange("planeADD", " ", plane);

   }

   @Override
   public void getAirPlanesDTOFromServer(ArrayList<PlaneDTO> planes)
   {
      support.firePropertyChange("positionUPDATE", " ", planes);

   }

   @Override
   public void getAirNodesDTOFromServer(ArrayList<NodeDTO> nodes)
   {
      support.firePropertyChange("nodeADD", " ", nodes);

   }

   @Override
   public void getWindFromServer(boolean wind)
   {
      support.firePropertyChange("windUPDATE", " ", wind);

   }

   @Override
   public void simulationFailed()
   {
      support.firePropertyChange("simulationFAILED", " ", true);

   }

   @Override
   public void getTimerFromServer(Timer timer)
   {
      support.firePropertyChange("timerUPDATE", " ", timer);

   }

   @Override
   public void addPropertyChangeListener(PropertyChangeListener listener)
   {
      support.addPropertyChangeListener(listener);

   }

   @Override
   public void reRoutePlane(String registrationNo, StaticPosition position)
   {
      try
      {
         client.reRoutePlane(registrationNo, position);
      }
      catch (RemoteException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Override
   public void removePlane(int index)
   {
      support.firePropertyChange("planeREMOVE", " ", index);

   }

   @Override
   public void simulationStart()
   {
      support.firePropertyChange("simulationSTART", " ", true);

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
   public void setPlaneOnCourse(String registrationNo, int startNodeId)
   {
      try
      {
         client.setPlaneOnCourse(registrationNo,startNodeId);
      }
      catch (RemoteException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
