package viewmodel;

import model.ATCAirSimulator;

public class MainViewViewModel
{
   private ATCAirSimulator model;
   private AirRadarViewModel airRadarViewModel;
   private AirRadarStartViewModel airRadarStartViewModel;

   public MainViewViewModel(ATCAirSimulator model)
   {
      this.model = model;
      this.airRadarViewModel = new AirRadarViewModel(model, model, model);
      this.airRadarStartViewModel = new AirRadarStartViewModel(model);
   }

   public AirRadarViewModel getAirRadarViewModel()
   {
      return airRadarViewModel;
   }

   public AirRadarStartViewModel getAirRadarStartViewModel()
   {
      return airRadarStartViewModel;
   }

}
