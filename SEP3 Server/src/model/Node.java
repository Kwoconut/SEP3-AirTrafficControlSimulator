package model;

import java.util.ArrayList;

public class Node
{
   private String Name;
   private int DistanceFromSource = Integer.MAX_VALUE;
   private int NodeId;
   private boolean IsVisited;
   final private StaticPosition Position;
   private ArrayList<Edge> Edges;
   private ArrayList<Node> ShortestPath;
   private boolean IsGroundNode;

   public Node(String name, int nodeId, StaticPosition staticPosition)
   {
      this.Name = name;
      this.NodeId = nodeId;
      this.Position = staticPosition;
      this.Edges = new ArrayList<Edge>();
      this.ShortestPath = new ArrayList<Node>();
      this.IsGroundNode = false;
   }

   public boolean IsGroundNode()
   {
      return IsGroundNode;
   }

   public String getName()
   {
      return Name;
   }

   public int getNodeId()
   {
      return NodeId;
   }

   public StaticPosition getPosition()
   {
      return Position;
   }

   public ArrayList<Edge> getJointEdges()
   {
      return Edges;
   }

   public void setJointEdges(ArrayList<Edge> jointEdges)
   {
      this.Edges = jointEdges;
   }

   public boolean isVisited()
   {
      return IsVisited;
   }

   public void setIsVisited(boolean isVisited)
   {
      this.IsVisited = isVisited;
   }

   public void setDistanceFromSource(int distanceFromSource)
   {
      this.DistanceFromSource = distanceFromSource;
   }

   public int getDistanceFromSource()
   {
      return DistanceFromSource;
   }

   public void addShortDistanceNode(Node node)
   {
      this.ShortestPath.add(node);
   }

   public ArrayList<Node> getShortestPath()
   {
      return ShortestPath;
   }

   public void setNodeId(int NodeId)
   {
      this.NodeId = NodeId;
   }

   public void setShortestPath(ArrayList<Node> shortestPath)
   {
      this.ShortestPath = shortestPath;
   }

   public NodeDTO convertToDTO()
   {
      return new NodeDTO(this.NodeId, this.Position, this.Name);
   }

   public String toString()
   {
      String s = "";

      s += Name + "  ";
      s += NodeId;

      return s;
   }

   public boolean equals(Object obj)
   {
      if (!(obj instanceof Node))
      {
         return false;
      }
      Node other = (Node) obj;
      return other.getNodeId() == this.NodeId;
   }
}
