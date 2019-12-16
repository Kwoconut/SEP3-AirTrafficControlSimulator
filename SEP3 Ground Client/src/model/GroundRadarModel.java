package model;

import java.beans.PropertyChangeListener;

public interface GroundRadarModel
{
   void addPropertyChangeListener(PropertyChangeListener listener);

   void changePlaneRoute(String registrationNo, int startNodeId, int endNodeId);

   void stopPlane(String registrationNo);

}
