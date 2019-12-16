package model;

import java.beans.PropertyChangeListener;

import model.StaticPosition;

public interface AirRadarModel
{
   void addPropertyChangeListener(PropertyChangeListener listener);

   void reRoutePlane(String registrationNo, StaticPosition position);

   void setPlaneOnCourse(String registrationNo, int startNodeId);

}
