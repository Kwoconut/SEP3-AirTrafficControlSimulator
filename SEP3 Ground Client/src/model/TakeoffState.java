package model;

import java.io.Serializable;

public class TakeoffState implements PlaneState, Serializable
{

   @Override
   public void setNextState(Plane plane)
   {
      plane.setState(new InAirState());
      plane.setSpeed(10);

   }

   @Override
   public String toString()
   {
      return "Takeoff";
   }

}
