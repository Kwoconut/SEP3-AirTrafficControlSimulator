package view;

import java.io.IOException;

import viewmodel.MainViewViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView
{
   private Stage stage;
   private MainViewViewModel mvViewModel;

   public MainView(Stage stage, MainViewViewModel mvViewModel)
   {
      this.stage = stage;
      this.mvViewModel = mvViewModel;
   }

   public void start() throws Exception
   {
      openView("Start");
   }

   public void openView(String viewToOpen) throws IOException
   {
      Scene scene = null;
      FXMLLoader loader = new FXMLLoader();
      Parent root = null;

      if ("AirRadar".equals(viewToOpen))
      {
         loader.setLocation(getClass().getResource("AirRadarView.fxml"));
         root = loader.load();
         scene = new Scene(root);
         AirRadarView view = loader.getController();
         view.init(mvViewModel.getAirRadarViewModel(), this);
      }
      else if ("Start".equals(viewToOpen))
      {
         loader.setLocation(getClass().getResource("AirRadarStart.fxml"));
         root = loader.load();
         scene = new Scene(root);
         AirRadarStartView view = loader.getController();
         view.init(mvViewModel.getAirRadarStartViewModel(), this, scene);
      }
      stage.setTitle("AirRadar");

      Stage localStage = new Stage();

      localStage.setScene(scene);
      localStage.show();
   }

}
