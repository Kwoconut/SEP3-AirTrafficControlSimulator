package model;

import java.io.Serializable;
import java.util.Random;

public class BoardingState implements PlaneState, Serializable
{

   private Timer timer;
   private Random random = new Random();

   public BoardingState()
   {
      timer = new Timer(0, random.nextInt(4) + 1, 0);
   }

   @Override
   public void setNextState(Plane plane)
   {
      plane.setState(new LandedState());
      plane.setSpeed(0);
      plane.setReadyForTakeOff(true);
   }

   @Override
   public String toString()
   {
      return "Boarding";
   }

   public void decrement()
   {
      timer.decrement();
   }

   public Timer getTime()
   {
      return timer;
   }

}
