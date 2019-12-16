package model;

import java.io.Serializable;

public class LandedState implements PlaneState,Serializable
{

   @Override
   public void setNextState(Plane plane)
   {
      plane.setState(new TaxiState());
      plane.setSpeed(2);
      

   }

   @Override
   public String toString()
   {
      return "Landed";
   }

}
