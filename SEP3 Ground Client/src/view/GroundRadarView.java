package view;

import java.util.NoSuchElementException;

import viewmodel.GroundRadarViewModel;
import viewmodel.GroundPlaneViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GroundRadarView
{
   @FXML
   private Pane mainPane;

   @FXML
   private Label timerLabel;

   @FXML
   private ImageView flagImage;

   @FXML
   private TableView<GroundPlaneViewModel> planeListTable;

   @FXML
   private TableColumn<GroundPlaneViewModel, String> callSignColumn;

   @FXML
   private TableColumn<GroundPlaneViewModel, String> statusColumn;

   @FXML
   private TableColumn<GroundPlaneViewModel, String> targetColumn;

   @FXML
   private Pane failPane;

   private GroundRadarViewModel viewModel;

   private ObservableList<Circle> groundNodes;

   private ObservableList<Pane> groundPlanes;

   private Pane selectedPlane;

   private MainView mainView;

   private Scene scene;

   public void init(GroundRadarViewModel groundRadarViewModel,
         MainView mainView, Scene scene)
   {
      this.mainView = mainView;
      this.viewModel = groundRadarViewModel;
      this.scene = scene;
      this.groundNodes = FXCollections.observableArrayList();
      this.groundPlanes = FXCollections.observableArrayList();
      this.failPane.setVisible(false);
      this.timerLabel.textProperty().bind(this.viewModel.getTimerProperty());
      for (int i = 0; i < this.viewModel.getGroundNodes().size() - 2; i++)
      {
         Circle circle = new Circle(10);
         circle.centerXProperty()
               .bind(this.viewModel.getGroundNodes().get(i).getXProperty());
         circle.centerYProperty()
               .bind(this.viewModel.getGroundNodes().get(i).getYProperty());
         circle.setFill(Color.YELLOW);
         circle.setStroke(Color.BLACK);
         groundNodes.add(circle);
         mainPane.getChildren().add(circle);
      }

      callSignColumn.setCellValueFactory(
            cellData -> cellData.getValue().getRegistrationNoProperty());
      statusColumn.setCellValueFactory(
            cellData -> cellData.getValue().getStatusProperty());
      targetColumn.setCellValueFactory(
            cellData -> cellData.getValue().getTargetProperty());
      planeListTable.setItems(this.viewModel.getPlanes());

      // LISTENER CHECKING IF SIMULATION FAILED
      // FAIL PANE BECOMES VISIBLE IF TRUE

      this.viewModel.getSimulationFailed()
            .addListener((observable, oldValue, newValue) -> {
               if (newValue == true)
               {
                  failPane.setVisible(true);
               }
            });

      flagImage.setImage(new Image("Right.png"));

      this.viewModel.getWindProperty()
            .addListener((observable, oldValue, newValue) -> {
               if (newValue == true)
               {
                  flagImage.setImage(new Image("Left.png"));
               }
               else
               {
                  flagImage.setImage(new Image("Right.png"));
               }
            });

      mainPane.addEventFilter(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>()
            {
               public void handle(MouseEvent e)
               {

                  if (viewModel.getSelectedPlane().get() != null)
                  {
                     try
                     {
                        Circle circle = (Circle) mainPane.getChildren().stream()
                              .filter(node -> node instanceof Circle
                                    && node.getBoundsInParent().contains(
                                          e.getSceneX(), e.getSceneY()))
                              .findFirst().get();
                        viewModel.setSelectedGroundEndNode(viewModel
                              .getGroundNodes().stream()
                              .filter(groundNode -> groundNode.getXProperty()
                                    .get() == circle.getCenterX()
                                    && groundNode.getYProperty().get() == circle
                                          .getCenterY())
                              .findFirst().get());
                        viewModel.changePlaneRoute();
                     }
                     catch (NoSuchElementException error)
                     {
                        Circle circle = null;
                     }
                     finally
                     {
                        mainPane.getChildren().stream()
                              .filter(node -> node instanceof Pane
                                    && node.equals(selectedPlane))
                              .findFirst().get().setEffect(null);
                        viewModel.setSelectedPlane(null);
                        viewModel.setSelectedGroundStartNode(null);
                        viewModel.setSelectedGroundEndNode(null);
                     }

                  }
                  else
                  {
                     for (int i = 0; i < groundPlanes.size(); i++)
                     {
                        if (groundPlanes.get(i).getBoundsInParent()
                              .contains(e.getSceneX(), e.getSceneY())
                              && !viewModel.getPlanes().get(i)
                                    .getStatusProperty().get().equals("Landing")
                              && !viewModel.getPlanes().get(i)
                                    .getStatusProperty().get()
                                    .startsWith("Boarding"))
                        {
                           selectedPlane = groundPlanes.get(i);
                           int depth = 70;
                           DropShadow borderGlow = new DropShadow();
                           borderGlow.setOffsetY(0f);
                           borderGlow.setOffsetX(0f);
                           borderGlow.setColor(Color.RED);
                           borderGlow.setWidth(depth);
                           borderGlow.setHeight(depth);

                           groundPlanes.get(i).setEffect(borderGlow);

                           Circle circle = findNearestGroundNode(
                                 mainPane.getChildren(),
                                 groundPlanes.get(i).getTranslateX(),
                                 groundPlanes.get(i).getTranslateY());
                           viewModel.setSelectedGroundStartNode(viewModel
                                 .getGroundNodes().stream()
                                 .filter(node -> node.getXProperty()
                                       .get() == circle.centerXProperty().get()
                                       && node.getYProperty().get() == circle
                                             .centerYProperty().get())
                                 .findFirst().get());
                           viewModel.setSelectedPlane(
                                 viewModel.getPlanes().get(i));
                           System.out.println(viewModel.getSelectedPlane().get()
                                 .getRegistrationNoProperty().get());
                           i = groundPlanes.size();
                        }
                        else
                        {
                           viewModel.setSelectedPlane(null);
                           viewModel.setSelectedGroundStartNode(null);
                           viewModel.setSelectedGroundEndNode(null);
                        }
                     }

                  }

               }
            });

      this.scene.setOnKeyPressed(new EventHandler<KeyEvent>()
      {

         @Override
         public void handle(KeyEvent event)
         {
            if (event.getCode() == KeyCode.BACK_SPACE)
            {
               if (viewModel.getSelectedPlane().get() != null)
               {
                  stopPlane();
               }
            }

         }

      });

      // LISTENER FOR ADDING OR REMOVING A PLANE FROM THE MAP
      // WHEN AN PLANE IS ADDED A NEW PANE IS CREATED WITH THE TEXT HAVING THE
      // CALLSIGN BOUND AND THE LOCATION BOUND

      this.viewModel.getPlanes()
            .addListener((ListChangeListener<GroundPlaneViewModel>) change ->

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
                     callSignText
                           .setFont(Font.font("System", FontWeight.BOLD, 12));
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

                     groundPlanes.add(pane);
                     mainPane.getChildren().add(pane);
                  }
                  else if (change.wasRemoved())
                  {
                     for (int i = 0; i < groundPlanes.size(); i++)
                     {
                        if (change.getRemoved().get(0)
                              .getRegistrationNoProperty().get()
                              .equals(((Text) groundPlanes.get(i).getChildren()
                                    .get(1)).getText()))
                        {
                           mainPane.getChildren().remove(groundPlanes.get(i));
                           groundPlanes.remove(i);
                           i = groundPlanes.size();
                        }
                     }
                  }
               }
            });

   }

   private Circle findNearestGroundNode(ObservableList<Node> nodes, double x,
         double y)
   {
      Point2D pClick = new Point2D(x, y);
      Circle nearestNode = null;
      double closestDistance = Double.POSITIVE_INFINITY;

      for (Node node : nodes)
      {
         if (node instanceof Circle)
         {
            Bounds bounds = node.getBoundsInParent();
            Point2D[] corners = new Point2D[] {
                  new Point2D(bounds.getMinX(), bounds.getMinY()),
                  new Point2D(bounds.getMaxX(), bounds.getMinY()),
                  new Point2D(bounds.getMaxX(), bounds.getMaxY()),
                  new Point2D(bounds.getMinX(), bounds.getMaxY()), };

            for (Point2D pCompare : corners)
            {
               double nextDist = pClick.distance(pCompare);
               if (nextDist < closestDistance)
               {
                  closestDistance = nextDist;
                  nearestNode = (Circle) node;
               }
            }
         }
      }

      return nearestNode;
   }

   private void stopPlane()
   {
      this.viewModel.stopPlane();
   }

}
