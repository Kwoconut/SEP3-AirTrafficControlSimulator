package model;

import java.io.Serializable;

public class StaticPosition implements Serializable
{
   private double XCoordinate;
   private double YCoordinate;

   public StaticPosition(double XCoordinate, double YCoordinate)
   {
      this.XCoordinate = XCoordinate;
      this.YCoordinate = YCoordinate;
   }

   public double getXCoordinate()
   {
      return XCoordinate;
   }

   public double getYCoordinate()
   {
      return YCoordinate;
   }

   public void setXCoordinate(double XCoordinate)
   {
      this.XCoordinate = XCoordinate;
   }

   public void setYCoordinate(double YCoordinate)
   {
      this.YCoordinate = YCoordinate;
   }

   public void setPosition(StaticPosition staticPosition)
   {
      this.XCoordinate = staticPosition.getXCoordinate();
      this.YCoordinate = staticPosition.getYCoordinate();
   }

   public boolean equals(Object obj)
   {
      if (!(obj instanceof StaticPosition))
      {
         return false;
      }
      StaticPosition other = (StaticPosition) obj;
      return other.getXCoordinate() == XCoordinate
            && other.getYCoordinate() == YCoordinate;
   }

   public String toString()
   {
      return this.XCoordinate + ", " + this.YCoordinate;
   }

}
