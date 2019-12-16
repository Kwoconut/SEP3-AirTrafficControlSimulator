package server;

import java.rmi.RemoteException;

import model.StaticPosition;

public class AirPlaneDispatcher implements Runnable
{

   private SimulationManager manager;
   private ServerModel model;

   AirPlaneDispatcher(SimulationManager manager, ServerModel model)
   {
      this.manager = manager;
      this.model = model;
   }

   private synchronized void sendAirPlane()
   {

      for (int i = 0; i < 2; i++)
      {
         for (int j = 0; j < manager.getServer().getAirClients().size(); j++)
         {
            try
            {
               model.getSimulationAirPlanes().add(model.getAirPlanes().get(i));
               manager.getServer().sendAirPlaneDTO(
                     model.getSimulationAirPlanes().get(i).convertToDTO(),
                     manager.getServer().getAirClients().get(j));
               // manager.getServer().getModel().getGroundPlanes().add(manager.getServer().getModel().getSimulationAirPlanes().get(i));
               // manager.getServer().getModel().getSimulationAirPlanes().remove(i);
            }
            catch (RemoteException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         try
         {
            //
            //
            //
            Thread.sleep(10000);
         }
         catch (InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   @Override
   public void run()
   {
      System.out.println("AirPlaneDispatcher Started");
      sendAirPlane();

   }

}
