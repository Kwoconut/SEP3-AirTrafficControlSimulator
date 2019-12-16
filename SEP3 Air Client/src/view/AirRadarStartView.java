package view;

import java.io.IOException;

import viewmodel.AirRadarStartViewModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class AirRadarStartView
{
   @FXML
   ImageView loadingPlane;

   @FXML
   Label loadingLabel;

   @FXML
   Button startButton;

   private AirRadarStartViewModel airRadarStartViewModel;
   private MainView mainView;
   private Scene scene;

   public void init(AirRadarStartViewModel airRadarStartViewModel,
         MainView mainView, Scene scene)
   {
      this.scene = scene;
      this.airRadarStartViewModel = airRadarStartViewModel;
      this.mainView = mainView;
      this.loadingLabel.setVisible(false);
      this.loadingPlane.setVisible(false);

      this.airRadarStartViewModel.getSimulationStarted()
            .addListener((observable, oldValue, newValue) -> {
               if (newValue == true)
               {
                  try
                  {
                     this.scene.getWindow().hide();
                     mainView.openView("AirRadar");
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
      airRadarStartViewModel.establishConnection();
      this.startButton.setVisible(false);
      this.loadingLabel.setVisible(true);
      this.loadingPlane.setVisible(true);
   }

}
