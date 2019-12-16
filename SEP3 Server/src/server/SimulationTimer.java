package server;

import java.rmi.RemoteException;

import model.BoardingState;
import model.EmergencyState;
import model.Timer;

public class SimulationTimer implements Runnable
{
   private SimulationManager simulationManager;
   private ServerModel model;

   public SimulationTimer(SimulationManager simulationManager,
         ServerModel model)
   {
      this.simulationManager = simulationManager;
      this.model = model;
   }

   private synchronized void updateBoardingTimer()
   {
      for (int i = 0; i < model.getSimulationGroundPlanes().size(); i++)
      {
         if (model.getSimulationGroundPlanes().get(i)
               .getPlaneState() instanceof BoardingState)
         {
            ((BoardingState) model.getSimulationGroundPlanes().get(i)
                  .getPlaneState()).decrement();

            if (((BoardingState) model.getSimulationGroundPlanes().get(i)
                  .getPlaneState()).getTime().equals(new Timer(0, 0, 0)))
            {
               model.getSimulationGroundPlanes().get(i)
                     .setReadyForTakeOff(true);
            }
         }
      }
   }

   private synchronized void sendAirPlane()
   {

      for (int i = 0; i < 1; i++)
      {
         if (model.getAirPlanes().get(i).getFlightPlan().getArrivalTime()
               .timeNow().equals(model.getTimer()))
         {
            for (int j = 0; j < simulationManager.getServer().getAirClients()
                  .size(); j++)
            {
               try
               {
                  model.getSimulationAirPlanes()
                        .add(model.getAirPlanes().get(i));
                  simulationManager.getServer()
                        .sendAirPlaneDTO(
                              model.getSimulationAirPlanes()
                                    .get(model.getSimulationAirPlanes().size()
                                          - 1)
                                    .convertToDTO(),
                              simulationManager.getServer().getAirClients()
                                    .get(j));
               }
               catch (RemoteException e)
               {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
         }
      }
   }

   private synchronized void updateEmergencyTimer() throws RemoteException
   {
      for (int i = 0; i < model.getSimulationAirPlanes().size(); i++)
      {
         if (model.getSimulationAirPlanes().get(i)
               .getPlaneState() instanceof EmergencyState)
         {
            ((EmergencyState) model.getSimulationAirPlanes().get(i)
                  .getPlaneState()).decrement();

            if (((EmergencyState) model.getSimulationAirPlanes().get(i)
                  .getPlaneState()).getTime().equals(new Timer(0, 0, 0)))
            {
               for (int j = 0; j < this.simulationManager.getServer()
                     .getAirClients().size(); j++)
               {
                  this.simulationManager.getServer().getAirClients().get(j)
                        .simulationFailed();
               }
               for (int j = 0; j < this.simulationManager.getServer()
                     .getGroundClients().size(); j++)
               {
                  this.simulationManager.getServer().getGroundClients().get(j)
                        .simulationFailed();
               }
               this.simulationManager.exitPlaneDispatcher();
               this.simulationManager.exitSimulationTimer();
            }

         }

      }
   }

   private synchronized void sendUpdatedTimer() throws RemoteException
   {
      model.incrementTimer();

      for (int i = 0; i < this.simulationManager.getServer().getAirClients()
            .size(); i++)
      {
         this.simulationManager.getServer().getAirClients().get(i)
               .getTimerFromServer(model.getTimer());
      }

      for (int i = 0; i < this.simulationManager.getServer().getGroundClients()
            .size(); i++)
      {
         this.simulationManager.getServer().getGroundClients().get(i)
               .getTimerFromServer(model.getTimer());
      }
   }

   @Override
   public void run()
   {
      while (true)
      {
         try
         {
            sendAirPlane();
            updateBoardingTimer();
            updateEmergencyTimer();
            sendUpdatedTimer();
            Thread.sleep(0250);
         }
         catch (RemoteException | InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }
}
