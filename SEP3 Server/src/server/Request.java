package server;

import model.Node;
import model.Plane;

public class Request
{
   
   public String Type;
   public Plane[] Planes;
   public Node[] Nodes;
   public model.Edge[] Edges;
   
   public Request(String type)
   {
      this.Type = type;
   }
   
   public Request(String type,Plane[] planes)
   {
      this.Type = type;
      this.Planes = planes;
   }
   
   public Request(String type,Node[] nodes)
   {
      this.Type = type;
      this.Nodes = nodes;
   }
   
   public Request(String type,model.Edge[] edges)
   {
      this.Type = type;
      this.Edges = edges;
   }

}
