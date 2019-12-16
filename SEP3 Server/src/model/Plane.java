package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Plane implements Serializable
{
   private String RegistrationNo;
   private String Model;
   private String Company;
   private FlightPlan FlightPlan;
   private PlaneState PlaneState;
   private MovingPosition PlanePosition;
   private StaticPosition Target;
   private ArrayList<Node> Route;
   private double Speed;
   private boolean ReadyForTakeOff;

   public Plane(String registrationNo, String model, String company,
         MovingPosition position, StaticPosition target, FlightPlan flightPlan)
   {
      this.RegistrationNo = registrationNo;
      this.Model = model;
      this.Company = company;
      this.FlightPlan = flightPlan;
      this.PlanePosition = position;
      this.Target = target;
      this.Route = new ArrayList<Node>();
      this.PlaneState = new LandedState();
   }

   public double getSpeed()
   {
      return Speed;
   }

   public StaticPosition getTarget()
   {
      return Target;
   }

   public synchronized void movePlane()
   {
      if (!(PlaneState instanceof LandedState
            || PlaneState instanceof BoardingState))
      {
         boolean targetReached = PlanePosition.movePosition(Target, Speed);
         if (targetReached)
         {
            Route.remove(0);
            if (Route.isEmpty())
            {
               setState(new LandedState());
               this.Speed = 0;
            }
            else
            {
               Target.setPosition(Route.get(0).getPosition());
            }

         }
      }

   }

   public PlaneState getPlaneState()
   {
      return PlaneState;
   }

   public void setState(PlaneState PlaneState)
   {
      this.PlaneState = PlaneState;
   }

   public void setSpeed(double Speed)
   {
      this.Speed = Speed;
   }

   public ArrayList<Node> getRoute()
   {
      return Route;
   }

   public void setRoute(ArrayList<Node> Route)
   {
      if (!(PlaneState instanceof InAirState)
            && !(PlaneState instanceof EmergencyState))
      {
         this.Speed = 2;
         this.PlaneState = new TaxiState();
      }
      else
      {
         this.Speed = 1;
      }

      System.out.println(Route);

      this.Route = Route;
      this.Target = Route.get(0).getPosition();
   }

   public void stopPlane()
   {
      this.PlaneState = new LandedState();
      this.Speed = 0;
   }

   public String getRegistrationNo()
   {
      return RegistrationNo;
   }

   public String getModel()
   {
      return Model;
   }

   public String getCompany()
   {
      return Company;
   }

   public boolean isReadyForTakeOff()
   {
      return ReadyForTakeOff;
   }

   public void setReadyForTakeOff(boolean ReadyForTakeOff)
   {
      this.ReadyForTakeOff = ReadyForTakeOff;
   }

   public PlaneDTO convertToDTO()
   {
      String route = "";
      try
      {
         route = Route.get(Route.size() - 1).getName();
      }
      catch (IndexOutOfBoundsException e)
      {
         route = "No target";
      }
      return new PlaneDTO(this.RegistrationNo, this.PlaneState,
            this.PlanePosition, route);
   }

   public FlightPlan getFlightPlan()
   {
      return FlightPlan;
   }

   public StaticPosition getPosition()
   {
      return this.PlanePosition;
   }

   public void landPlane(Node node)
   {
      if (node.getNodeId() == 9)
      {
         this.PlanePosition.setPosition(new StaticPosition(1550, 114));
      }
      else
      {
         this.PlanePosition.setPosition(new StaticPosition(0, 114));
      }

      ArrayList<Node> route = new ArrayList<Node>();
      route.add(node);
      setRoute(route);
      setState(new LandingState());
      setSpeed(10);
   }

   public void takeOffPlane(Node node)
   {
      ArrayList<Node> route = new ArrayList<Node>();
      route.add(node);
      setRoute(route);
      setState(new TakeoffState());
      setSpeed(10);
   }

   public void departPlane(Node node)
   {
      ArrayList<Node> route = new ArrayList<Node>();
      route.add(node);
      setRoute(route);
      setState(new InAirState());
      setSpeed(1);

   }

}
