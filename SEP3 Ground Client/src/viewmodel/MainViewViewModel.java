package viewmodel;

import model.ATCGroundSimulator;

public class MainViewViewModel
{

   private ATCGroundSimulator model;
   private GroundRadarViewModel groundRadarViewModel;
   private GroundRadarStartViewModel groundRadarStartViewModel;

   public MainViewViewModel(ATCGroundSimulator model)
   {
      this.model = model;
      this.groundRadarViewModel = new GroundRadarViewModel(model, model, model);
      this.groundRadarStartViewModel = new GroundRadarStartViewModel(model);
   }

   public GroundRadarViewModel getGroundRadarViewModel()
   {
      return groundRadarViewModel;
   }

   public GroundRadarStartViewModel getGroundRadarStartViewModel()
   {
      return groundRadarStartViewModel;
   }

}
