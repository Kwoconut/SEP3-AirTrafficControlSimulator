package viewmodel;

import model.AirPlaneModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.PlaneDTO;

public class AirPlaneViewModel
{
   private StringProperty registrationNoProperty;
   private StringProperty statusProperty;
   private DoubleProperty xProperty;
   private DoubleProperty yProperty;
   private StringProperty targetProperty;
   private AirPlaneModel model;

   public AirPlaneViewModel(AirPlaneModel model, PlaneDTO plane)
   {
      this.model = model;
      registrationNoProperty = new SimpleStringProperty(
            plane.getRegistrationNo());
      statusProperty = new SimpleStringProperty(
            plane.getPlaneState().toString());
      xProperty = new SimpleDoubleProperty(
            plane.getPosition().getXCoordinate());
      yProperty = new SimpleDoubleProperty(
            plane.getPosition().getYCoordinate());
      targetProperty = new SimpleStringProperty(plane.getTarget());
   }

   public StringProperty getTargetProperty()
   {
      return targetProperty;
   }

   public StringProperty getRegistrationNoProperty()
   {
      return registrationNoProperty;
   }

   public StringProperty getStatusProperty()
   {
      return statusProperty;
   }

   public DoubleProperty getXProperty()
   {
      return xProperty;
   }

   public DoubleProperty getYProperty()
   {
      return yProperty;
   }

   public boolean equals(Object obj)
   {
      if (!(obj instanceof AirPlaneViewModel))
      {
         return false;
      }
      AirPlaneViewModel other = (AirPlaneViewModel) obj;
      return other.getRegistrationNoProperty().get()
            .equals(this.getRegistrationNoProperty().get())
            && other.getStatusProperty().get()
                  .equals(this.statusProperty.get());
   }

}
