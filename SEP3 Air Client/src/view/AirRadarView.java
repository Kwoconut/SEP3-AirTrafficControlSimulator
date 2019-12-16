package view;

import viewmodel.AirPlaneViewModel;
import viewmodel.AirRadarViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AirRadarView
{

   @FXML
   private Pane mainPane;

   @FXML
   private Label timerLabel;

   @FXML
   private Pane failPane;

   @FXML
   private Label windLabel;

   private AirRadarViewModel viewModel;

   private ObservableList<Circle> airNodes;

   private ObservableList<Pane> airPlanes;

   private Pane selectedPlane;

   private MainView mainView;

   public void init(AirRadarViewModel airRadarViewModel, MainView mainView)
   {
      this.viewModel = airRadarViewModel;
      this.mainView = mainView;
      this.airNodes = FXCollections.observableArrayList();
      this.airPlanes = FXCollections.observableArrayList();
      this.failPane.setVisible(false);
      this.timerLabel.textProperty().bind(this.viewModel.getTimerProperty());

      for (int i = 0; i < this.viewModel.getAirNodes().size(); i++)
      {
         Circle circle = new Circle(10);

         circle.centerXProperty()
               .bind(this.viewModel.getAirNodes().get(i).getXProperty());
         circle.centerYProperty()
               .bind(this.viewModel.getAirNodes().get(i).getYProperty());
         circle.setFill(Color.YELLOW);
         circle.setStroke(Color.BLACK);

         if (this.viewModel.getAirNodes().get(i).getIDProperty().get() == 22
               || this.viewModel.getAirNodes().get(i).getIDProperty()
                     .get() >= 35)
         {
            Label label = new Label(
                  this.viewModel.getAirNodes().get(i).getName().get());
            label.setTextFill(Color.web("#002F5F"));
            label.translateXProperty().set(circle.centerXProperty().get() + 20);
            label.translateYProperty().set(circle.centerYProperty().get() - 10);
            mainPane.getChildren().add(label);
         }
         airNodes.add(circle);
         mainPane.getChildren().add(circle);
      }
      this.failPane.toFront();

      this.viewModel.getSimulationFailed()
            .addListener((observable, oldValue, newValue) -> {
               if (newValue == true)
               {
                  failPane.setVisible(true);
               }
            });

      this.windLabel.setText("right");

      this.viewModel.getWindProperty()
            .addListener((observable, oldValue, newValue) -> {
               if (newValue == true)
               {
                  this.windLabel.setText("left");
               }
               else
               {
                  this.windLabel.setText("right");
               }
            });

      mainPane.addEventFilter(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>()
            {
               public void handle(MouseEvent e)
               {

                  if (viewModel.getSelectedPlane().get() != null)
                  {
                     boolean planeOnCourse = false;
                     for (int i = 0; i < airNodes.size(); i++)
                     {
                        if (airNodes.get(i).getBoundsInParent()
                              .contains(e.getSceneX(), e.getSceneY()))
                        {
                           viewModel.setSelectedNode(
                                 viewModel.getAirNodes().get(i));
                           viewModel.setPlaneOnCourse();
                           planeOnCourse = true;
                        }
                     }
                     if (!planeOnCourse)
                     {
                        viewModel.reRoutePlane(e.getSceneX(), e.getSceneY());
                     }
                     mainPane.getChildren().stream()
                           .filter(node -> node instanceof Pane
                                 && node.equals(selectedPlane))
                           .findFirst().get().setEffect(null);
                     viewModel.setSelectedPlane(null);
                  }
                  else
                  {
                     for (int i = 0; i < airPlanes.size(); i++)
                     {
                        if (airPlanes.get(i).getBoundsInParent()
                              .contains(e.getSceneX(), e.getSceneY())
                              && !viewModel.getPlanes().get(i)
                                    .getStatusProperty().get().equals("Landing")
                              && !viewModel.getPlanes().get(i)
                                    .getStatusProperty().get()
                                    .startsWith("Boarding"))
                        {
                           selectedPlane = airPlanes.get(i);
                           int depth = 70;
                           DropShadow borderGlow = new DropShadow();
                           borderGlow.setOffsetY(0f);
                           borderGlow.setOffsetX(0f);
                           borderGlow.setColor(Color.RED);
                           borderGlow.setWidth(depth);
                           borderGlow.setHeight(depth);

                           airPlanes.get(i).setEffect(borderGlow);
                           viewModel.setSelectedPlane(
                                 viewModel.getPlanes().get(i));
                           i = airPlanes.size();
                        }
                        else
                        {
                           viewModel.setSelectedPlane(null);
                        }
                     }

                  }

               }
            });

      // LISTENER FOR ADDING OR REMOVING A PLANE FROM THE MAP
      // WHEN AN PLANE IS ADDED A NEW PANE IS CREATED WITH THE TEXT HAVING THE
      // CALLSIGN BOUND AND THE LOCATION BOUND

      this.viewModel.getPlanes()
            .addListener((ListChangeListener<AirPlaneViewModel>) change ->

            {
               while (change.next())
               {
                  if (change.wasAdded())
                  {
                     Pane pane = new Pane();
                     Text callSignText = new Text();
                     callSignText.textProperty().bind(change.getAddedSubList()
                           .get(0).getRegistrationNoProperty());
                     callSignText.setFill(Color.web("#002F5F"));
                     ;
                     callSignText.translateYProperty().setValue(-12);
                     callSignText.translateXProperty().setValue(10);
                     Rectangle square = new Rectangle();
                     square.scaleXProperty().setValue(20);
                     square.scaleYProperty().setValue(20);
                     square.setStroke(Color.web("#002F5F"));
                     pane.getChildren().addAll(square, callSignText);
                     pane.translateXProperty().bind(
                           change.getAddedSubList().get(0).getXProperty());
                     pane.translateYProperty().bind(
                           change.getAddedSubList().get(0).getYProperty());

                     airPlanes.add(pane);
                     mainPane.getChildren().add(pane);
                  }
                  else if (change.wasRemoved())
                  {
                     for (int i = 0; i < airPlanes.size(); i++)
                     {
                        if (change.getRemoved().get(0)
                              .getRegistrationNoProperty().get()
                              .equals(((Text) airPlanes.get(i).getChildren()
                                    .get(1)).getText()))
                        {
                           mainPane.getChildren().remove(airPlanes.get(i));
                           airPlanes.remove(i);
                           i = airPlanes.size();
                        }
                     }
                  }
               }
            });

   }

}
