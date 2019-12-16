package model;

import java.io.Serializable;

public class LandingState implements PlaneState, Serializable
{

   @Override
   public void setNextState(Plane plane)
   {
      plane.setState(new LandedState());
      plane.setSpeed(0);
      plane.setReadyForTakeOff(false);
   }

   @Override
   public String toString()
   {
      return "Landing";
   }

}
