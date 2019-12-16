package server;

import java.rmi.RemoteException;

public class GroundPlaneDispatcher implements Runnable
{

   private SimulationManager manager;
   private ServerModel model;

   GroundPlaneDispatcher(SimulationManager manager, ServerModel model)
   {
      this.manager = manager;
      this.model = model;
   }

   private synchronized void sendGroundPlane()
   {

      for (int i = 0; i < 1; i++)
      {
         for (int j = 0; j < manager.getServer().getGroundClients().size(); j++)
         {
            try
            {
               model.getSimulationGroundPlanes()
                     .add(model.getGroundPlanes().get(i));
               manager.getServer().sendGroundPlaneDTO(
                     model.getSimulationGroundPlanes().get(i).convertToDTO(),
                     manager.getServer().getGroundClients().get(j));
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
      System.out.println("GroundPlaneDispatcher Started");
      sendGroundPlane();
   }
}