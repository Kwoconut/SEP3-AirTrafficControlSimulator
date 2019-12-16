package startup;
import javafx.application.Application;

public class AirClientStart 
{
	   public static void main(String[] args)
	   {
	     new Thread(() -> Application.launch(AirClientSetUp.class)).start();
	   }
}
