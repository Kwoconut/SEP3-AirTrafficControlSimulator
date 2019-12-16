package model;

import java.io.Serializable;

public class PlaneDTO implements Serializable
{
   private String registrationNo;
   private StaticPosition staticPosition;
   private PlaneState state;
   private String target;

   public PlaneDTO(String registrationNo, PlaneState state, StaticPosition staticPosition,String target)
   {
      this.registrationNo = registrationNo;
      this.state = state;
      this.staticPosition = staticPosition;
      this.target = target;
   }
   
   public String getTarget()
   {
      return target;
   }

   public String getRegistrationNo()
   {
      return registrationNo;
   }

   public PlaneState getPlaneState()
   {
      return state;
   }
   
   public StaticPosition getPosition()
   {
      return staticPosition;
   }


}
