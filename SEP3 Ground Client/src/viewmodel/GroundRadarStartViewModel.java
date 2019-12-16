package viewmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import model.GroundRadarStartModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GroundRadarStartViewModel implements PropertyChangeListener
{

   private GroundRadarStartModel model;
   private BooleanProperty simulationStarted;

   public GroundRadarStartViewModel(GroundRadarStartModel model)
   {
      this.model = model;
      this.simulationStarted = new SimpleBooleanProperty(false);
      this.model.addPropertyChangeListener(this);
   }

   public BooleanProperty getSimulationStarted()
   {
      return simulationStarted;
   }

   public void establishConnection()
   {
      this.model.establishConnection();
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      Platform.runLater(() -> {
         if (evt.getPropertyName().equals("simulationSTART"))
         {
            simulationStarted.setValue((boolean) evt.getNewValue());
         }
      });
   }

}
