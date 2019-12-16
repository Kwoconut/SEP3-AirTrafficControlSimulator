package view;

import java.io.IOException;

import viewmodel.GroundRadarStartViewModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class GroundRadarStartView
{
   @FXML
   ImageView loadingPlane;

   @FXML
   Label loadingLabel;

   @FXML
   Button startButton;

   private GroundRadarStartViewModel groundRadarStartViewModel;
   private MainView mainView;
   private Scene scene;

   public void init(GroundRadarStartViewModel groundRadarStartViewModel,
         MainView mainView, Scene scene)
   {
      this.groundRadarStartViewModel = groundRadarStartViewModel;
      this.mainView = mainView;
      this.scene = scene;
      this.loadingLabel.setVisible(false);
      this.loadingPlane.setVisible(false);

      this.groundRadarStartViewModel.getSimulationStarted()
            .addListener((observable, oldValue, newValue) -> {
               if (newValue == true)
               {
                  try
                  {
                     this.scene.getWindow().hide();
                     mainView.openView("GroundRadar");
                  }
                  catch (IOException e)
                  {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
            });

   }

   public void onStartButtonPressed()
   {
      groundRadarStartViewModel.establishConnection();
      this.startButton.setVisible(false);
      this.loadingLabel.setVisible(true);
      this.loadingPlane.setVisible(true);
   }

}
