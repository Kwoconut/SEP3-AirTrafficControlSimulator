package model;

import java.io.Serializable;

public class InAirState implements PlaneState,Serializable
{

   @Override
   public void setNextState(Plane plane)
   {
      plane.setState(new LandingState());
      plane.setSpeed(4);
   }

   @Override
   public String toString()
   {
      return "In Air";
   }
}
