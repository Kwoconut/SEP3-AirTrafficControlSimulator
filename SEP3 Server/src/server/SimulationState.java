package server;

import java.rmi.RemoteException;

import javafx.scene.shape.Rectangle;
import model.BoardingState;
import model.InAirState;
import model.LandedState;
import model.StaticPosition;
import model.TakeoffState;
import model.TaxiState;

public class SimulationState implements Runnable
{
   private SimulationManager manager;
   private ServerModel model;

   SimulationState(SimulationManager manager, ServerModel model)
   {
      this.manager = manager;
      this.model = model;
   }

   private synchronized void takeOffPlanes() throws RemoteException
   {

      // SETTING THE PLANE FOR TAKEOFF FROM 1 SIDE OF THE RUNWAY DEPENDING ON
      // THE ENTRY ON THE RUNWAY
      // CHECKS IF THE PLANE IS READY FOR TAKEOFF
      // IF (TRUE)
      // SETS COURSE FOR TAKEOFF
      // IF (FALSE)
      // DOESN'T ALLOW THE PLANE TO TAKEOFF

      for (int i = 0; i < model.getSimulationGroundPlanes().size(); i++)
      {
         if (model.getSimulationGroundPlanes().get(i).isReadyForTakeOff())
         {
            if (model.getSimulationGroundPlanes().get(i).getPosition()
                  .equals(model.getGroundNodes().stream()
                        .filter(node -> node.getNodeId() == 9).findFirst().get()
                        .getPosition()))
            {
               model.getSimulationGroundPlanes().get(i)
                     .takeOffPlane(model.getGroundNodes().stream()
                           .filter(node -> node.getNodeId() == 21).findFirst()
                           .get());
            }
            else if (model.getSimulationGroundPlanes().get(i).getPosition()
                  .equals(model.getGroundNodes().stream()
                        .filter(node -> node.getNodeId() == 16).findFirst()
                        .get().getPosition()))
            {
               model.getSimulationGroundPlanes().get(i)
                     .takeOffPlane(model.getGroundNodes().stream()
                           .filter(node -> node.getNodeId() == 20).findFirst()
                           .get());
            }
         }
      }

      // SETTING THE COURSE OF THE PLANE AFTER TAKEOFF DEPENDING ON THE
      // FLIGHTPLAN
      // DJIKSTRA ALGORITHM WILL FIND THE OPTIMAL PATHWAWY FOR FLYING

      for (int i = 0; i < model.getSimulationGroundPlanes().size(); i++)
      {
         if (model.getSimulationGroundPlanes().get(i).getPosition()
               .equals(model.getGroundNodes().get(21).getPosition()))
         {
            model.getSimulationGroundPlanes().get(i).setState(new InAirState());

            model.getSimulationGroundPlanes().get(i).getPosition()
                  .setPosition(model.getAirNodes().stream()
                        .filter(node -> node.getNodeId() == 22).findFirst()
                        .get().getPosition());

            model.getSimulationAirPlanes()
                  .add(model.getSimulationGroundPlanes().get(i));

            model.setTakeoffRoute(
                  model.getSimulationAirPlanes()
                        .get(model.getSimulationAirPlanes().size() - 1)
                        .getRegistrationNo(),
                  22, model
                        .getApproachNodesByDirection(
                              model.getSimulationGroundPlanes().get(i)
                                    .getFlightPlan().getStartLocation())
                        .getNodeId(),
                  false);

            for (int j = 0; j < manager.getServer().getAirClients().size(); j++)
            {
               manager.getServer().sendAirPlaneDTO(
                     model.getSimulationGroundPlanes().get(i).convertToDTO(),
                     manager.getServer().getAirClients().get(j));
            }

            model.getSimulationGroundPlanes().remove(i);

            for (int j = 0; j < manager.getServer().getGroundClients()
                  .size(); j++)
            {
               manager.getServer().removeGroundPlane(
                     manager.getServer().getGroundClients().get(j), i);
            }
         }
         else if (model.getSimulationGroundPlanes().get(i).getPosition()
               .equals(model.getGroundNodes().stream()
                     .filter(node -> node.getNodeId() == 20).findFirst().get()
                     .getPosition()))
         {
            model.getSimulationGroundPlanes().get(i).setState(new InAirState());

            model.getSimulationGroundPlanes().get(i).getPosition()
                  .setPosition(model.getAirNodes().stream()
                        .filter(node -> node.getNodeId() == 22).findFirst()
                        .get().getPosition());

            model.getSimulationAirPlanes()
                  .add(model.getSimulationGroundPlanes().get(i));

            model.setTakeoffRoute(
                  model.getSimulationAirPlanes()
                        .get(model.getSimulationAirPlanes().size() - 1)
                        .getRegistrationNo(),
                  22, model
                        .getApproachNodesByDirection(
                              model.getSimulationGroundPlanes().get(i)
                                    .getFlightPlan().getStartLocation())
                        .getNodeId(),
                  true);

            for (int j = 0; j < manager.getServer().getAirClients().size(); j++)
            {
               manager.getServer().sendAirPlaneDTO(
                     model.getSimulationGroundPlanes().get(i).convertToDTO(),
                     manager.getServer().getAirClients().get(j));
            }
            model.getSimulationGroundPlanes().remove(i);
            for (int j = 0; j < manager.getServer().getGroundClients()
                  .size(); j++)
            {
               manager.getServer().removeGroundPlane(
                     manager.getServer().getGroundClients().get(j), i);
            }
         }
      }
   }

   private synchronized void landPlanes() throws RemoteException
   {
      for (int i = 0; i < model.getSimulationAirPlanes().size(); i++)
      {
         if (!model.getSimulationAirPlanes().get(i).isReadyForTakeOff())
         {
            if (model.getSimulationAirPlanes().get(i).getPosition()
                  .equals(model.getAirNodes().stream()
                        .filter(node -> node.getNodeId() == 22).findFirst()
                        .get().getPosition()))
            {
               model.getSimulationAirPlanes().get(i)
                     .landPlane(model.getLandingNode(model.getWind()));
               model.getSimulationGroundPlanes()
                     .add(model.getSimulationAirPlanes().get(i));
               for (int j = 0; j < manager.getServer().getGroundClients()
                     .size(); j++)
               {
                  manager.getServer().sendGroundPlaneDTO(
                        model.getSimulationAirPlanes().get(i).convertToDTO(),
                        manager.getServer().getGroundClients().get(j));
               }
               for (int j = 0; j < manager.getServer().getAirClients()
                     .size(); j++)
               {
                  manager.getServer().removeAirPlane(
                        manager.getServer().getAirClients().get(j), i);
               }
               model.getSimulationAirPlanes().remove(i);
               i = model.getSimulationAirPlanes().size();
            }
         }
      }
   }

   private synchronized void updateStateOnGroundLocation()
   {
      for (int i = 0; i < model.getSimulationGroundPlanes().size(); i++)
      {
         if (!model.getSimulationGroundPlanes().get(i).isReadyForTakeOff())
         {
            for (int j = 0; j < model.getGateNodes().size(); j++)
            {
               if (model.getGateNodes().get(j).getPosition().equals(
                     model.getSimulationGroundPlanes().get(i).getPosition())
                     && !(model.getSimulationGroundPlanes().get(i)
                           .getPlaneState() instanceof BoardingState))
               {
                  model.getSimulationGroundPlanes().get(i)
                        .setState(new BoardingState());
                  model.getSimulationGroundPlanes().get(i).setSpeed(0);
               }
            }
         }
         else
         {
            for (int j = 0; j < model.getGateNodes().size(); j++)
            {
               if (!(model.getSimulationGroundPlanes().get(i)
                     .getPlaneState() instanceof TaxiState))
               {
                  if (model.getGateNodes().get(j).getPosition().equals(
                        model.getSimulationGroundPlanes().get(i).getPosition())
                        && !(model.getSimulationGroundPlanes().get(i)
                              .getPlaneState() instanceof LandedState))
                  {
                     model.getSimulationGroundPlanes().get(i)
                           .setState(new LandedState());
                     model.getSimulationGroundPlanes().get(i).setSpeed(0);
                  }
               }
            }
         }
      }
   }

   private synchronized void movePlanes() throws RemoteException
   {
      for (int i = 0; i < model.getSimulationGroundPlanes().size(); i++)
      {
         model.getSimulationGroundPlanes().get(i).movePlane();
      }
      for (int i = 0; i < model.getSimulationAirPlanes().size(); i++)
      {
         model.getSimulationAirPlanes().get(i).movePlane();
      }

      for (int i = 0; i < manager.getServer().getGroundClients().size(); i++)
      {
         manager.getServer().sendGroundPlanesDTO(
               manager.getServer().getGroundClients().get(i));
      }

      for (int i = 0; i < manager.getServer().getAirClients().size(); i++)
      {
         manager.getServer()
               .sendAirPlanesDTO(manager.getServer().getAirClients().get(i));
      }
   }

   private synchronized void removePlanes() throws RemoteException
   {
      for (int i = 0; i < model.getSimulationAirPlanes().size(); i++)
      {
         for (int j = 0; j < model.getAirNodes().size(); j++)
            if (model.getAirNodes().get(j).getNodeId() >= 35
                  && model.getSimulationAirPlanes().get(i).getPosition()
                        .equals(model.getAirNodes().get(j).getPosition())
                  && model.getSimulationAirPlanes().get(i).isReadyForTakeOff())
            {
               for (int x = 0; x < manager.getServer().getAirClients()
                     .size(); x++)
               {
                  manager.getServer().getAirClients().get(x).removeAirPlane(i);
               }
               model.getSimulationAirPlanes().remove(i);
               j = model.getAirNodes().size();
               i = model.getSimulationAirPlanes().size();
            }
      }
   }

   private synchronized boolean checkCollision() throws RemoteException
   {
      for (int i = 0; i < model.getSimulationGroundPlanes().size(); i++)
      {
         for (int j = i + 1; j < model.getSimulationGroundPlanes().size(); j++)
         {
            Rectangle rectangle1 = new Rectangle(
                  model.getSimulationGroundPlanes().get(i).getPosition()
                        .getXCoordinate() + 10,
                  model.getSimulationGroundPlanes().get(i).getPosition()
                        .getYCoordinate() + 10,
                  20, 20);
            Rectangle rectangle2 = new Rectangle(
                  model.getSimulationGroundPlanes().get(j).getPosition()
                        .getXCoordinate() + 10,
                  model.getSimulationGroundPlanes().get(j).getPosition()
                        .getYCoordinate() + 10,
                  20, 20);
            if (rectangle1.intersects(rectangle2.getBoundsInLocal()))
            {
               return true;
            }
         }
      }

      for (int i = 0; i < model.getSimulationAirPlanes().size(); i++)
      {
         for (int j = i + 1; j < model.getSimulationAirPlanes().size(); j++)
         {
            Rectangle rectangle1 = new Rectangle(
                  model.getSimulationAirPlanes().get(i).getPosition()
                        .getXCoordinate() + 10,
                  model.getSimulationAirPlanes().get(i).getPosition()
                        .getYCoordinate() + 10,
                  20, 20);
            Rectangle rectangle2 = new Rectangle(
                  model.getSimulationAirPlanes().get(j).getPosition()
                        .getXCoordinate() + 10,
                  model.getSimulationAirPlanes().get(j).getPosition()
                        .getYCoordinate() + 10,
                  20, 20);
            if (rectangle1.intersects(rectangle2.getBoundsInLocal()))
            {
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public void run()
   {
      System.out.println("SimulationState started");
      while (true)
      {
         try
         {

            removePlanes();
            updateStateOnGroundLocation();
            takeOffPlanes();
            landPlanes();
            movePlanes();
         }
         catch (RemoteException e)
         {
            e.printStackTrace();
         }
         try
         {
            if (checkCollision())
            {
               manager.exitSimulationTimer();
               System.out.println("Simulation State Thread Stopped");
               break;
            }
         }
         catch (RemoteException e)
         {
            e.printStackTrace();
         }

         try
         {
            Thread.sleep(0100);
         }
         catch (InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      for (int x = 0; x < manager.getServer().getGroundClients().size(); x++)
      {
         try
         {
            manager.getServer().simulationFailed(
                  manager.getServer().getGroundClients().get(x));
         }
         catch (RemoteException e)
         {
            e.printStackTrace();
         }
      }
      for (int x = 0; x < manager.getServer().getAirClients().size(); x++)
      {
         try
         {
            manager.getServer().airSimulationFailed(
                  manager.getServer().getAirClients().get(x));
         }
         catch (RemoteException e)
         {
            e.printStackTrace();
         }
      }
   }

}
