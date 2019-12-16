package viewmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import model.GroundRadarModel;
import model.GroundNodeModel;
import model.GroundPlaneModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BoardingState;
import model.NodeDTO;
import model.PlaneDTO;
import model.Timer;

public class GroundRadarViewModel implements PropertyChangeListener
{
   private ObservableList<GroundPlaneViewModel> planes;
   private ObservableList<GroundNodeViewModel> groundNodes;
   private ObjectProperty<GroundNodeViewModel> selectedStartNode;
   private ObjectProperty<GroundNodeViewModel> selectedEndNode;
   private ObjectProperty<GroundPlaneViewModel> selectedPlane;
   private GroundRadarModel model;
   private GroundNodeModel groundNodeViewModel;
   private GroundPlaneModel planeViewModel;
   private BooleanProperty simulationFailed;
   private BooleanProperty windProperty;
   private StringProperty timerProperty;

   public GroundRadarViewModel(GroundRadarModel model,
         GroundNodeModel groundNodeViewModel, GroundPlaneModel planeViewModel)
   {
      this.model = model;
      this.groundNodeViewModel = groundNodeViewModel;
      this.planeViewModel = planeViewModel;
      this.planes = FXCollections.observableArrayList();
      this.groundNodes = FXCollections.observableArrayList();
      this.selectedStartNode = new SimpleObjectProperty<GroundNodeViewModel>();
      this.selectedEndNode = new SimpleObjectProperty<GroundNodeViewModel>();
      this.selectedPlane = new SimpleObjectProperty<GroundPlaneViewModel>();
      this.simulationFailed = new SimpleBooleanProperty(false);
      this.windProperty = new SimpleBooleanProperty(false);
      this.timerProperty = new SimpleStringProperty();
      this.model.addPropertyChangeListener(this);

   }

   public ObservableList<GroundPlaneViewModel> getPlanes()
   {
      return planes;
   }

   public ObservableList<GroundNodeViewModel> getGroundNodes()
   {
      return groundNodes;
   }

   public ObjectProperty<GroundNodeViewModel> getSelectedStartNode()
   {
      return selectedStartNode;
   }

   public ObjectProperty<GroundNodeViewModel> getSelectedEndNode()
   {
      return selectedEndNode;
   }

   public ObjectProperty<GroundPlaneViewModel> getSelectedPlane()
   {

      return selectedPlane;

   }

   public BooleanProperty getWindProperty()
   {
      return windProperty;
   }

   public BooleanProperty getSimulationFailed()
   {
      return simulationFailed;
   }

   public StringProperty getTimerProperty()
   {
      return timerProperty;
   }

   public void setSelectedGroundStartNode(GroundNodeViewModel selectedNode)
   {
      if (selectedNode != null)
      {
         System.out.println(selectedNode.getIDProperty().get() + " ");
      }
      this.selectedStartNode.setValue(selectedNode);

   }

   public void setSelectedGroundEndNode(GroundNodeViewModel selectedNode)
   {
      if (selectedNode != null)
      {
         System.out.println(selectedNode.getIDProperty().get() + " ");
      }
      this.selectedEndNode.setValue(selectedNode);
   }

   public void setSelectedPlane(GroundPlaneViewModel plane)
   {
      if (plane != null)
      {
         System.out.println(plane.getRegistrationNoProperty().get() + " ");
      }
      this.selectedPlane.setValue(plane);
   }
   
   public void stopPlane()
   {
      this.model.stopPlane(selectedPlane.get().getRegistrationNoProperty().get());
   }

   public void changePlaneRoute()
   {
      this.model.changePlaneRoute(
            selectedPlane.get().getRegistrationNoProperty().get(),
            selectedStartNode.get().getIDProperty().get(),
            selectedEndNode.get().getIDProperty().get());
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      if (evt.getPropertyName().equals("nodeADD"))
      {
         @SuppressWarnings("unchecked")
         ArrayList<NodeDTO> nodes = (ArrayList<NodeDTO>) evt.getNewValue();
         for (int i = 0; i < nodes.size(); i++)
         {
            this.groundNodes.add(new GroundNodeViewModel(
                  this.groundNodeViewModel, nodes.get(i)));
         }
      }
      Platform.runLater(() -> {
         if (evt.getPropertyName().equals("planeADD"))
         {
            planes.add(new GroundPlaneViewModel(this.planeViewModel,
                  (PlaneDTO) evt.getNewValue()));
         }
         else if (evt.getPropertyName().equals("windADD"))
         {
            windProperty.set((boolean) evt.getNewValue());
         }
         else if (evt.getPropertyName().equals("timerUPDATE"))
         {
            timerProperty.set(((Timer) evt.getNewValue()).toString());
         }
         else if (evt.getPropertyName().equals("planeREMOVE"))
         {
            planes.remove((int) evt.getNewValue());
         }
         else if (evt.getPropertyName().equals("simulationFAILED"))
         {
            simulationFailed.set((boolean) evt.getNewValue());
         }
         else if (evt.getPropertyName().equals("positionUPDATE"))
         {
            @SuppressWarnings("unchecked")
            ArrayList<PlaneDTO> planes = (ArrayList<PlaneDTO>) evt
                  .getNewValue();
            if (!this.planes.isEmpty())
            {
               for (int i = 0; i < this.planes.size(); i++)
               {
                  this.planes.get(i).getXProperty()
                        .setValue(planes.get(i).getPosition().getXCoordinate());
                  this.planes.get(i).getYProperty()
                        .setValue(planes.get(i).getPosition().getYCoordinate());
                  if (planes.get(i).getPlaneState() instanceof BoardingState)
                  {
                     this.planes.get(i).getStatusProperty().setValue(planes
                           .get(i).getPlaneState().toString() + " - "
                           + ((BoardingState) planes.get(i).getPlaneState())
                                 .getTime().toString());
                  }
                  else
                  {
                     this.planes.get(i).getStatusProperty()
                           .setValue(planes.get(i).getPlaneState().toString());
                  }
                  this.planes.get(i).getTargetProperty()
                        .setValue(planes.get(i).getTarget());
               }
            }
         }
      });

   }

}
