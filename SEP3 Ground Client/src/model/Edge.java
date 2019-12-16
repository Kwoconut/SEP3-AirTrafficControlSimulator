package model;

public class Edge
{
   private int FromNodeIndex;
   private int ToNodeIndex;
   private int Length;
   private boolean IsGroundEdge;

   public Edge(int fromNodeIndex, int toNodeIndex, int length)
   {
      this.FromNodeIndex = fromNodeIndex;
      this.ToNodeIndex = toNodeIndex;
      this.Length = length;
      this.IsGroundEdge = false;
   }

   public void setFromNodeIndex(int fromNodeIndex)
   {
      this.FromNodeIndex = fromNodeIndex;
   }
   
   public void setLength(int Length)
   {
      this.Length = Length;
   }

   public void setToNodeIndex(int toNodeIndex)
   {
      this.ToNodeIndex = toNodeIndex;
   }

   public int getFromNodeIndex()
   {
      return FromNodeIndex;
   }

   public int getToNodeIndex()
   {
      return ToNodeIndex;
   }

   public int getLength()
   {
      return Length;
   }

   public boolean isGroundEdge()
   {
      return IsGroundEdge;
   }

   public int getNeighbourIndex(int nodeIndex)
   {
      if (this.FromNodeIndex == nodeIndex)
      {
         return this.ToNodeIndex;
      }
      else
      {
         return this.FromNodeIndex;
      }
   }

   public String toString()
   {
      return FromNodeIndex + " " + ToNodeIndex + " " + Length;
   }

}
