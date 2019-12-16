package model;

import java.io.Serializable;

public class NodeDTO implements Serializable
{
   private int nodeId;
   private String name;
   private StaticPosition position;

   public NodeDTO(int nodeId, StaticPosition position, String name)
   {
      this.nodeId = nodeId;
      this.position = position;
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public int getNodeId()
   {
      return nodeId;
   }

   public StaticPosition getPosition()
   {
      return position;
   }

}
