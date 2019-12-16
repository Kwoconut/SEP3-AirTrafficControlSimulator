package model;

public class MovingPosition extends StaticPosition
{

   public MovingPosition(double XCoordinate, double YCoordinate)
   {
      super(XCoordinate, YCoordinate);
   }

   public synchronized boolean movePosition(StaticPosition position, double speed)
   {
      double distance = Math
            .sqrt((position.getXCoordinate() - super.getXCoordinate())
                  * (position.getXCoordinate() - super.getXCoordinate())
                  + (position.getYCoordinate() - super.getYCoordinate())
                        * (position.getYCoordinate() - super.getYCoordinate()));

      if (distance > 2)
      {
         double deltaX = position.getXCoordinate() - super.getXCoordinate();
         double deltaY = position.getYCoordinate() - super.getYCoordinate();
         double angle = Math.atan2(deltaY, deltaX);
         super.setXCoordinate(super.getXCoordinate() + speed * Math.cos(angle));
         super.setYCoordinate(super.getYCoordinate() + speed * Math.sin(angle));
         return false;
      }
      else
      {
         super.setXCoordinate(position.getXCoordinate());
         super.setYCoordinate(position.getYCoordinate());
         return true;
      }
   }

}
