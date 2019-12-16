package startup;
import javafx.application.Application;

public class GroundClientStart
{
   public static void main(String[] args)
   {
     new Thread(() -> Application.launch(GroundClientSetUp.class)).start();
   }
}
