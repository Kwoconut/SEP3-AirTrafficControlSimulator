package model;

import java.util.ArrayList;

import airclient.AirClient;
import model.NodeDTO;
import model.PlaneDTO;
import model.Timer;

public interface ATCAirSimulatorModelClientHandler {
   
   void setClient(AirClient airClient);
   
   void getPlaneDTOFromServer(PlaneDTO plane);
   
   void getAirPlanesDTOFromServer(ArrayList<PlaneDTO> planes);
   
   void getAirNodesDTOFromServer(ArrayList<NodeDTO> nodes);
   
   void getWindFromServer(boolean wind);
   
   void simulationFailed();

   void getTimerFromServer(Timer timer);
   
   void removePlane(int index);
   
   void simulationStart();

}
