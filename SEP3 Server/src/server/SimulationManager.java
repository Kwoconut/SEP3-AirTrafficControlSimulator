package server;

public class SimulationManager
{

   private Server server;
   private boolean exitPlaneDispatcher;
   private boolean exitSimulationTimer;
   private boolean aloha = false;

   public SimulationManager(Server server)
   {
      this.server = server;
   }

   public void startThreads()
   {
      simulationTimerRun();
      simulationStateRun();
      this.aloha = true;
   }

   public void airPlaneDispatcherRun()
   {
      AirPlaneDispatcher airPlaneDispatcher = new AirPlaneDispatcher(this,
            server.getModel());
      Thread planeDispatcherThread = new Thread(airPlaneDispatcher);
      planeDispatcherThread.start();
   }

   public void groundPlaneDispatcherRun()
   {
      GroundPlaneDispatcher groundPlaneDispatcher = new GroundPlaneDispatcher(
            this, this.server.getModel());
      Thread planeDispatcherThread = new Thread(groundPlaneDispatcher);
      planeDispatcherThread.start();
   }

   public void simulationStateRun()
   {
      SimulationState simulationState = new SimulationState(this,
            this.server.getModel());
      Thread simulationStateThread = new Thread(simulationState);
      simulationStateThread.start();
   }

   public void simulationTimerRun()
   {
      SimulationTimer simulationTimer = new SimulationTimer(this,
            this.server.getModel());
      Thread simulationTimerThread = new Thread(simulationTimer);
      simulationTimerThread.start();
   }

   public Server getServer()
   {
      return server;
   }

   public boolean getAloha()
   {
      return aloha;
   }

   public boolean getExitPlaneDispatcher()
   {
      return exitPlaneDispatcher;
   }

   public void exitPlaneDispatcher()
   {
      exitPlaneDispatcher = true;
   }

   public boolean getSimulationTimer()
   {
      return exitSimulationTimer;
   }

   public void exitSimulationTimer()
   {
      exitSimulationTimer = true;
   }
}
