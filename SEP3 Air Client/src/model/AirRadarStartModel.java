package model;

import java.beans.PropertyChangeListener;

public interface AirRadarStartModel
{
   void establishConnection();

   void addPropertyChangeListener(PropertyChangeListener listener);

}
