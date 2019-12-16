package model;

import java.io.Serializable;

public class FlightPlan implements Serializable
{
   private String CallSign;
   private int FlightNumber;
   private FlightDate DepartureTime;
   private FlightDate ArrivalTime;
   private Timer Delay;
   private String StartLocation;
   private String EndLocation;

   public FlightPlan(String callSign,int flightNumber, FlightDate departureTime, FlightDate arrivalTime,
         Timer delay, String startLocation, String endLocation)
   {
      this.CallSign = callSign;
      this.FlightNumber = flightNumber;
      this.DepartureTime = departureTime;
      this.ArrivalTime = arrivalTime;
      this.Delay = delay;
      this.StartLocation = startLocation;
      this.EndLocation = endLocation;
   }

   public String getStartLocation()
   {
      return StartLocation;
   }

   public String getEndLocation()
   {
      return EndLocation;
   }

   public Timer getDelay()
   {
      return Delay;
   }

   public FlightDate getDepartureTime()
   {
      return DepartureTime;
   }

   public FlightDate getArrivalTime()
   {
      return ArrivalTime;
   }

   public int getFlightNumber()
   {
      return FlightNumber;
   }
   
   public String getCallSign()
   {
      return CallSign;
   }

   public String toString()
   {
      return CallSign + " " + FlightNumber + " " + StartLocation + " " + EndLocation + " " + DepartureTime
            + " " + ArrivalTime + " " + Delay;
   }

}
