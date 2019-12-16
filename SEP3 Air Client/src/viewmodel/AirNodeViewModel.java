package viewmodel;

import model.AirNodeModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.NodeDTO;

public class AirNodeViewModel
{
   private DoubleProperty xProperty;
   private DoubleProperty yProperty;
   private IntegerProperty idProperty;
   private StringProperty nameProperty;
   private AirNodeModel model;

   public AirNodeViewModel(AirNodeModel model, NodeDTO node)
   {
      this.model = model;
      xProperty = new SimpleDoubleProperty(node.getPosition().getXCoordinate());
      yProperty = new SimpleDoubleProperty(node.getPosition().getYCoordinate());
      idProperty = new SimpleIntegerProperty(node.getNodeId());
      nameProperty = new SimpleStringProperty(node.getName());
   }

   public DoubleProperty getXProperty()
   {
      return xProperty;
   }

   public DoubleProperty getYProperty()
   {
      return yProperty;
   }

   public IntegerProperty getIDProperty()
   {
      return idProperty;
   }

   public StringProperty getName()
   {
      return nameProperty;
   }

   public boolean equals(Object obj)
   {
      if (!(obj instanceof AirNodeViewModel))
      {
         return false;
      }
      AirNodeViewModel other = (AirNodeViewModel) obj;
      return other.getIDProperty().get() == idProperty.get()
            && other.getYProperty().get() == yProperty.get()
            && other.getXProperty().get() == xProperty.get();
   }

}
