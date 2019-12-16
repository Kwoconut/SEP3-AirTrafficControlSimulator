package model;

public class EmergencyState implements PlaneState
{

   private Timer timer;

   public EmergencyState()
   {
      timer = new Timer(0, 30, 0);
   }

   @Override
   public void setNextState(Plane plane)
   {
      plane.setState(new LandingState());
      plane.setSpeed(4);
   }

   public String toString()
   {
      return "Emergency";
   }

   public Timer getTime()
   {
      return timer;
   }

   public void decrement()
   {
      timer.decrement();
   }

}
