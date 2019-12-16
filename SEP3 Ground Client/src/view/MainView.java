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

      if ("GroundRadar".equals(viewToOpen))
      {
         loader.setLocation(getClass().getResource("GroundRadarView.fxml"));
         root = loader.load();
         scene = new Scene(root);
         GroundRadarView view = loader.getController();
         view.init(mvViewModel.getGroundRadarViewModel(), this, scene);
      }
      else if ("Start".equals(viewToOpen))
      {
         loader.setLocation(getClass().getResource("GroundRadarStart.fxml"));
         root = loader.load();
         scene = new Scene(root);
         GroundRadarStartView view = loader.getController();
         view.init(mvViewModel.getGroundRadarStartViewModel(), this, scene);

      }
      stage.setTitle("GroundRadar");

      Stage localStage = new Stage();

      localStage.setScene(scene);
      localStage.show();
   }

}
